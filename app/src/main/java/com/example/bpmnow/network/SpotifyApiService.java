package com.example.bpmnow.network;

import com.example.bpmnow.models.SpotifySearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface SpotifyApiService {
    
    @GET("search")
    Call<SpotifySearchResponse> searchTracks(
            @Query("q") String query,
            @Query("type") String type,
            @Query("limit") int limit,
            @Header("Authorization") String authorization
    );
}
