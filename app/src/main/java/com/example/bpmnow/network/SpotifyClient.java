package com.example.bpmnow.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyClient {
    private static final String BASE_URL = "https://api.spotify.com/v1/";
    private static SpotifyClient instance;
    private final Retrofit retrofit;

    private SpotifyClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized SpotifyClient getInstance() {
        if (instance == null) {
            instance = new SpotifyClient();
        }
        return instance;
    }

    public SpotifyApiService getApiService() {
        return retrofit.create(SpotifyApiService.class);
    }
}
