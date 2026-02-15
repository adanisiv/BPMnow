package com.example.bpmnow.ui.clubber;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bpmnow.R;
import com.example.bpmnow.adapters.SongAdapter;
import com.example.bpmnow.models.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link favoritesClubber#newInstance} factory method to
 * create an instance of this fragment.
 */
public class favoritesClubber extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView favoriteSongsRecyclerView;
    private SongAdapter songAdapter;
    private List<Song> favoriteSongs = new ArrayList<>();
    private TextView emptyStateText;

    public favoritesClubber() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment favoritesClubber.
     */
    // TODO: Rename and change types and number of parameters
    public static favoritesClubber newInstance(String param1, String param2) {
        favoritesClubber fragment = new favoritesClubber();
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
        return inflater.inflate(R.layout.fragment_favorites_clubber, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favoriteSongsRecyclerView = view.findViewById(R.id.favoriteSongsRecyclerView);
        emptyStateText = view.findViewById(R.id.emptyStateText);

        setupRecyclerView();
        loadFavoriteSongs();
    }

    private void setupRecyclerView() {
        favoriteSongsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        songAdapter = new SongAdapter(favoriteSongs);
        favoriteSongsRecyclerView.setAdapter(songAdapter);

        // Set click listeners
        songAdapter.setOnSongClickListener((song, position) -> {
            // TODO: Implement song playback or detail view
            Toast.makeText(getContext(), "Playing: " + song.getTitle(), Toast.LENGTH_SHORT).show();
        });

        songAdapter.setOnFavoriteClickListener((song, position) -> {
            // Remove from favorites
            if (!song.isFavorite()) {
                removeSongFromFavorites(song, position);
            }
        });
    }

    private void loadFavoriteSongs() {
        // TODO: Load favorite songs from Firebase or SharedPreferences
        // For now, adding mock data
        loadMockData();
        updateEmptyState();
    }

    private void loadMockData() {
        favoriteSongs.clear();
        
        favoriteSongs.add(new Song(
                "1",
                "Blinding Lights",
                "The Weeknd",
                "After Hours",
                "",
                "spotify:track:0VjIjW4GlUZAMYd2vXMi3b",
                200040
        ));
        favoriteSongs.get(0).setFavorite(true);

        favoriteSongs.add(new Song(
                "2",
                "One More Time",
                "Daft Punk",
                "Discovery",
                "",
                "spotify:track:0DiWol3AO6WpXZgp0goxAV",
                320067
        ));
        favoriteSongs.get(1).setFavorite(true);

        favoriteSongs.add(new Song(
                "3",
                "Titanium",
                "David Guetta ft. Sia",
                "Nothing but the Beat",
                "",
                "spotify:track:0lYBSQXN6rCTvUZvg9S0lU",
                245040
        ));
        favoriteSongs.get(2).setFavorite(true);

        favoriteSongs.add(new Song(
                "4",
                "Levels",
                "Avicii",
                "Levels",
                "",
                "spotify:track:5ZrrGtGZgm4hWVLFfNGmoX",
                202440
        ));
        favoriteSongs.get(3).setFavorite(true);

        favoriteSongs.add(new Song(
                "5",
                "Don't You Worry Child",
                "Swedish House Mafia",
                "Until Now",
                "",
                "spotify:track:7fvHQAvTMppJ9tYPFHXMxL",
                213800
        ));
        favoriteSongs.get(4).setFavorite(true);

        songAdapter.updateData(favoriteSongs);
    }

    private void removeSongFromFavorites(Song song, int position) {
        // TODO: Remove from Firebase or SharedPreferences
        songAdapter.removeSong(position);
        Toast.makeText(getContext(), song.getTitle() + " removed from favorites", Toast.LENGTH_SHORT).show();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (favoriteSongs.isEmpty()) {
            emptyStateText.setVisibility(View.VISIBLE);
            favoriteSongsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateText.setVisibility(View.GONE);
            favoriteSongsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
