package com.example.bpmnow.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.example.bpmnow.models.ResponseRefreshToken;
import com.example.bpmnow.models.SpotifySearchResponse;
import com.example.bpmnow.network.SpotifyClient;
import com.example.bpmnow.network.SpotifyRetrofitClientToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpotifyManager {
    private static final String TAG = "SpotifyManager";
    private static final String PREFS_NAME = "spotify_prefs";
    private static SpotifyManager instance;
    private final SharedPreferences prefs;
    private final Context context;

    private SpotifyManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = this.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SpotifyManager getInstance(Context context) {
        if (instance == null) {
            instance = new SpotifyManager(context);
        }
        return instance;
    }

    /**
     * Get the current access token
     */
    public String getAccessToken() {
        return prefs.getString("access_token", null);
    }

    /**
     * Get the refresh token
     */
    private String getRefreshToken() {
        return prefs.getString("refresh_token", null);
    }

    /**
     * Check if token is expired
     */
    public boolean isTokenExpired() {
        long expiresAt = prefs.getLong("expires_at", 0);
        return System.currentTimeMillis() >= expiresAt;
    }

    /**
     * Check if user is authenticated
     */
    public boolean isAuthenticated() {
        return getAccessToken() != null && getRefreshToken() != null;
    }

    /**
     * Get authorization header for API calls
     */
    public String getAuthorizationHeader() {
        String token = getAccessToken();
        return token != null ? "Bearer " + token : null;
    }

    /**
     * Refresh the access token if needed
     */
    public void refreshTokenIfNeeded(final TokenCallback callback) {
        if (!isAuthenticated()) {
            callback.onFailure("Not authenticated");
            return;
        }

        if (!isTokenExpired()) {
            callback.onSuccess(getAccessToken());
            return;
        }

        // Token is expired, refresh it
        refreshToken(callback);
    }

    /**
     * Force refresh the access token
     */
    public void refreshToken(final TokenCallback callback) {
        String refreshToken = getRefreshToken();
        if (refreshToken == null) {
            callback.onFailure("No refresh token available");
            return;
        }

        // Create Basic Auth header
        String credentials = Constants.SPOTIFY_CLIENT_ID + ":" + Constants.SPOTIFY_CLIENT_SECRET;
        String basicAuth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        SpotifyRetrofitClientToken.getInstance()
                .getApiService()
                .refreshToken("refresh_token", refreshToken, basicAuth)
                .enqueue(new Callback<ResponseRefreshToken>() {
                    @Override
                    public void onResponse(Call<ResponseRefreshToken> call, Response<ResponseRefreshToken> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ResponseRefreshToken body = response.body();
                            
                            // Save new tokens
                            prefs.edit()
                                    .putString("access_token", body.getAccess_token())
                                    .putString("refresh_token", body.getRefresh_token() != null ? 
                                            body.getRefresh_token() : getRefreshToken())
                                    .putLong("expires_at", System.currentTimeMillis() + (body.getExpires_in() * 1000L))
                                    .apply();

                            Log.d(TAG, "Token refreshed successfully");
                            callback.onSuccess(body.getAccess_token());
                        } else {
                            Log.e(TAG, "Failed to refresh token: " + response.code());
                            callback.onFailure("Failed to refresh token");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseRefreshToken> call, Throwable t) {
                        Log.e(TAG, "Token refresh error", t);
                        callback.onFailure(t.getMessage());
                    }
                });
    }

    /**
     * Search for tracks on Spotify
     */
    public void searchTracks(final String query, final int limit, final SearchCallback callback) {
        refreshTokenIfNeeded(new TokenCallback() {
            @Override
            public void onSuccess(String token) {
                String authHeader = "Bearer " + token;
                
                SpotifyClient.getInstance()
                        .getApiService()
                        .searchTracks(query, "track", limit, authHeader)
                        .enqueue(new Callback<SpotifySearchResponse>() {
                            @Override
                            public void onResponse(Call<SpotifySearchResponse> call, Response<SpotifySearchResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    callback.onSuccess(response.body());
                                } else {
                                    Log.e(TAG, "Search failed: " + response.code());
                                    callback.onFailure("Search failed");
                                }
                            }

                            @Override
                            public void onFailure(Call<SpotifySearchResponse> call, Throwable t) {
                                Log.e(TAG, "Search error", t);
                                callback.onFailure(t.getMessage());
                            }
                        });
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        });
    }

    /**
     * Clear all stored tokens (logout)
     */
    public void clearTokens() {
        prefs.edit()
                .remove("access_token")
                .remove("refresh_token")
                .remove("expires_at")
                .remove("code_verifier")
                .apply();
    }

    // Callback interfaces
    public interface TokenCallback {
        void onSuccess(String token);
        void onFailure(String error);
    }

    public interface SearchCallback {
        void onSuccess(SpotifySearchResponse response);
        void onFailure(String error);
    }
}
