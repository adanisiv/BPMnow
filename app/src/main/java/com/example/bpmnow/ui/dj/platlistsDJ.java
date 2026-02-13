package com.example.bpmnow.ui.dj;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bpmnow.R;
import com.example.bpmnow.models.ResponseRefreshToken;
import com.example.bpmnow.network.SpotifyRetrofitClientToken;
import com.example.bpmnow.utils.Constants;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link platlistsDJ#newInstance} factory method to
 * create an instance of this fragment.
 */
public class platlistsDJ extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public platlistsDJ() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment platlistsDJ.
     */
    // TODO: Rename and change types and number of parameters
    public static platlistsDJ newInstance(String param1, String param2) {
        platlistsDJ fragment = new platlistsDJ();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_platlists_d_j, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences prefs = requireContext().getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();
        readSharedPreferences(allEntries);
        refreshToken();
    }

    private void readSharedPreferences(Map<String, ?> allEntries) {
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Object value = entry.getValue();

            if (value instanceof String) {
                Log.d("SharedPrefs", entry.getKey() + " is a String: " + value);
            } else if (value instanceof Integer) {
                Log.d("SharedPrefs", entry.getKey() + " is an Integer: " + value);
            } else if (value instanceof Boolean) {
                Log.d("SharedPrefs", entry.getKey() + " is a Boolean: " + value);
            } else if (value instanceof Float) {
                Log.d("SharedPrefs", entry.getKey() + " is a Float: " + value);
            } else if (value instanceof Long) {
                Log.d("SharedPrefs", entry.getKey() + " is a Long: " + value);
            }
        }
    }

    public void refreshToken() {
        SharedPreferences prefs = requireContext().getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE);
        String refreshToken = prefs.getString("refresh_token", "");

// Spotify requires "Basic base64(clientId:clientSecret)" as the Authorization header
        String credentials = Constants.SPOTIFY_CLIENT_ID + ":" + Constants.SPOTIFY_CLIENT_SECRET;
        String basicAuth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        SpotifyRetrofitClientToken.getInstance()
                .getApiService()
                .refreshToken("refresh_token", refreshToken, basicAuth)
                .enqueue(new Callback<ResponseRefreshToken>() {
                    @Override
                    public void onResponse(Call<ResponseRefreshToken> call, Response<ResponseRefreshToken> response) {
                        if (response.isSuccessful()) {
                            ResponseRefreshToken body = response.body();

                            prefs.edit()
                                    .putString("access_token", body.getAccess_token())
                                    .putString("refresh_token", body.getRefresh_token())
                                    .apply();
                            Log.d("SharedPrefs", "access_token" + body.getAccess_token() + "  " + "refresh_token" + body.getRefresh_token());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseRefreshToken> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

    }
}