package com.example.bpmnow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpmnow.R;
import com.example.bpmnow.models.Song;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private List<Song> songs;
    private OnSongClickListener listener;
    private OnFavoriteClickListener favoriteListener;

    public interface OnSongClickListener {
        void onSongClick(Song song, int position);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Song song, int position);
    }

    public SongAdapter(List<Song> songs) {
        this.songs = songs != null ? songs : new ArrayList<>();
    }

    public void setOnSongClickListener(OnSongClickListener listener) {
        this.listener = listener;
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.favoriteListener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song_card, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        holder.bind(songs.get(position), position);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void updateData(List<Song> newSongs) {
        this.songs = newSongs != null ? newSongs : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void removeSong(int position) {
        if (position >= 0 && position < songs.size()) {
            songs.remove(position);
            notifyItemRemoved(position);
        }
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        private ImageView albumArt;
        private TextView title;
        private TextView artist;
        private TextView duration;
        private ImageButton favoriteButton;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            albumArt = itemView.findViewById(R.id.songAlbumArt);
            title = itemView.findViewById(R.id.songTitle);
            artist = itemView.findViewById(R.id.songArtist);
            duration = itemView.findViewById(R.id.songDuration);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
        }

        public void bind(Song song, int position) {
            title.setText(song.getTitle());
            artist.setText(song.getArtist());
            duration.setText(song.getFormattedDuration());

            // Update favorite button icon
            updateFavoriteIcon(song.isFavorite());

            // Set click listener for the whole item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSongClick(song, position);
                }
            });

            // Set click listener for favorite button
            favoriteButton.setOnClickListener(v -> {
                if (favoriteListener != null) {
                    song.setFavorite(!song.isFavorite());
                    updateFavoriteIcon(song.isFavorite());
                    favoriteListener.onFavoriteClick(song, position);
                }
            });

            // Load album art
            loadAlbumArt(song.getAlbumArtUrl());
        }

        private void loadAlbumArt(String albumArtUrl) {
            if (albumArtUrl != null && !albumArtUrl.isEmpty()) {
                // TODO: Add Glide dependency to build.gradle:
                // implementation 'com.github.bumptech.glide:glide:4.16.0'
                // annotationProcessor 'com.github.bumptech.glide:compiler:4.16.0'
                
                // Then uncomment this code:
                /*
                Glide.with(itemView.getContext())
                        .load(albumArtUrl)
                        .placeholder(R.drawable.ic_music_note)
                        .error(R.drawable.ic_music_note)
                        .centerCrop()
                        .into(albumArt);
                */
                
                // For now, use placeholder
                albumArt.setImageResource(R.drawable.ic_music_note);
            } else {
                albumArt.setImageResource(R.drawable.ic_music_note);
            }
        }

        private void updateFavoriteIcon(boolean isFavorite) {
            if (isFavorite) {
                favoriteButton.setImageResource(R.drawable.ic_favorite);
                favoriteButton.setColorFilter(itemView.getContext().getColor(R.color.neon_magenta));
            } else {
                favoriteButton.setImageResource(R.drawable.ic_favorite_border);
                favoriteButton.setColorFilter(itemView.getContext().getColor(R.color.on_background_tertiary));
            }
        }
    }
}
