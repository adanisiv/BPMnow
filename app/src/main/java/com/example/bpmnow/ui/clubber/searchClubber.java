package com.example.bpmnow.ui.clubber;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bpmnow.R;
import com.example.bpmnow.adapters.SongAdapter;
import com.example.bpmnow.models.Song;
import com.example.bpmnow.models.SpotifySearchResponse;
import com.example.bpmnow.utils.SpotifyManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for searching songs using Spotify API
 */
public class searchClubber extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int SEARCH_DELAY_MS = 500; // Debounce delay

    private String mParam1;
    private String mParam2;

    private TextInputEditText searchInput;
    private RecyclerView searchResultsRecyclerView;
    private SongAdapter songAdapter;
    private List<Song> searchResults = new ArrayList<>();
    private TextView emptyStateText;
    private ProgressBar loadingIndicator;
    
    private SpotifyManager spotifyManager;
    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    public searchClubber() {
        // Required empty public constructor
    }

    public static searchClubber newInstance(String param1, String param2) {
        searchClubber fragment = new searchClubber();
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
        
        spotifyManager = SpotifyManager.getInstance(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_clubber, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchInput = view.findViewById(R.id.searchInput);
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView);
        emptyStateText = view.findViewById(R.id.emptyStateText);
        loadingIndicator = view.findViewById(R.id.loadingIndicator);

        setupRecyclerView();
        setupSearch();
        checkSpotifyAuth();
    }

    private void setupRecyclerView() {
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        songAdapter = new SongAdapter(searchResults);
        searchResultsRecyclerView.setAdapter(songAdapter);

        // Set click listeners
        songAdapter.setOnSongClickListener((song, position) -> {
            // TODO: Implement song playback or detail view
            Toast.makeText(getContext(), "Selected: " + song.getTitle(), Toast.LENGTH_SHORT).show();
        });

        songAdapter.setOnFavoriteClickListener((song, position) -> {
            // Add/remove from favorites
            if (song.isFavorite()) {
                addSongToFavorites(song);
            } else {
                removeSongFromFavorites(song);
            }
        });
    }

    private void setupSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                
                // Cancel previous search
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
                
                if (query.length() >= 2) {
                    // Debounce: delay search by SEARCH_DELAY_MS
                    searchRunnable = () -> performSearch(query);
                    searchHandler.postDelayed(searchRunnable, SEARCH_DELAY_MS);
                } else if (query.isEmpty()) {
                    clearSearchResults();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void checkSpotifyAuth() {
        if (!spotifyManager.isAuthenticated()) {
            emptyStateText.setText("Please sign in to search for songs");
            emptyStateText.setVisibility(View.VISIBLE);
        }
    }

    private void performSearch(String query) {
        if (!spotifyManager.isAuthenticated()) {
            Toast.makeText(getContext(), "Please sign in to Spotify first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading indicator
        loadingIndicator.setVisibility(View.VISIBLE);
        emptyStateText.setVisibility(View.GONE);
        searchResultsRecyclerView.setVisibility(View.GONE);

        // Perform Spotify search
        spotifyManager.searchTracks(query, 20, new SpotifyManager.SearchCallback() {
            @Override
            public void onSuccess(SpotifySearchResponse response) {
                if (getActivity() == null) return;
                
                getActivity().runOnUiThread(() -> {
                    loadingIndicator.setVisibility(View.GONE);
                    
                    if (response.getTracks() != null && 
                        response.getTracks().getItems() != null && 
                        !response.getTracks().getItems().isEmpty()) {
                        
                        convertAndDisplayResults(response.getTracks().getItems());
                    } else {
                        showNoResults();
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                if (getActivity() == null) return;
                
                getActivity().runOnUiThread(() -> {
                    loadingIndicator.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Search failed: " + error, Toast.LENGTH_SHORT).show();
                    showNoResults();
                });
            }
        });
    }

    private void convertAndDisplayResults(List<SpotifySearchResponse.Track> tracks) {
        searchResults.clear();
        
        for (SpotifySearchResponse.Track track : tracks) {
            Song song = new Song(
                    track.getId(),
                    track.getName(),
                    track.getArtistNames(),
                    track.getAlbum().getName(),
                    track.getAlbum().getAlbumArtUrl(),
                    track.getUri(),
                    track.getDurationMs()
            );
            
            // TODO: Check if song is in favorites and set isFavorite accordingly
            
            searchResults.add(song);
        }
        
        songAdapter.updateData(searchResults);
        searchResultsRecyclerView.setVisibility(View.VISIBLE);
        emptyStateText.setVisibility(View.GONE);
    }

    private void showNoResults() {
        searchResults.clear();
        songAdapter.updateData(searchResults);
        emptyStateText.setText(R.string.no_results);
        emptyStateText.setVisibility(View.VISIBLE);
        searchResultsRecyclerView.setVisibility(View.GONE);
    }

    private void clearSearchResults() {
        searchResults.clear();
        songAdapter.updateData(searchResults);
        emptyStateText.setVisibility(View.VISIBLE);
        emptyStateText.setText("Start typing to search for songs...");
        searchResultsRecyclerView.setVisibility(View.GONE);
    }

    private void addSongToFavorites(Song song) {
        // TODO: Add to Firebase or SharedPreferences
        Toast.makeText(getContext(), song.getTitle() + " added to favorites", Toast.LENGTH_SHORT).show();
    }

    private void removeSongFromFavorites(Song song) {
        // TODO: Remove from Firebase or SharedPreferences
        Toast.makeText(getContext(), song.getTitle() + " removed from favorites", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clean up handler
        if (searchHandler != null && searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
    }
}
