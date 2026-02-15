package com.example.bpmnow.models;

public class Song {
    private String id;
    private String title;
    private String artist;
    private String album;
    private String albumArtUrl;
    private String spotifyUri;
    private int durationMs;
    private boolean isFavorite;

    public Song() {
        // Required empty constructor for Firebase
    }

    public Song(String id, String title, String artist, String album, String albumArtUrl, String spotifyUri, int durationMs) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumArtUrl = albumArtUrl;
        this.spotifyUri = spotifyUri;
        this.durationMs = durationMs;
        this.isFavorite = false;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getAlbumArtUrl() {
        return albumArtUrl;
    }

    public String getSpotifyUri() {
        return spotifyUri;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setAlbumArtUrl(String albumArtUrl) {
        this.albumArtUrl = albumArtUrl;
    }

    public void setSpotifyUri(String spotifyUri) {
        this.spotifyUri = spotifyUri;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    // Helper method to format duration
    public String getFormattedDuration() {
        int minutes = durationMs / 60000;
        int seconds = (durationMs % 60000) / 1000;
        return String.format("%d:%02d", minutes, seconds);
    }
}
