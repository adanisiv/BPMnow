package com.example.bpmnow.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SpotifySearchResponse {
    @SerializedName("tracks")
    private Tracks tracks;

    public Tracks getTracks() {
        return tracks;
    }

    public static class Tracks {
        @SerializedName("items")
        private List<Track> items;

        @SerializedName("total")
        private int total;

        public List<Track> getItems() {
            return items;
        }

        public int getTotal() {
            return total;
        }
    }

    public static class Track {
        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("uri")
        private String uri;

        @SerializedName("duration_ms")
        private int durationMs;

        @SerializedName("artists")
        private List<Artist> artists;

        @SerializedName("album")
        private Album album;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUri() {
            return uri;
        }

        public int getDurationMs() {
            return durationMs;
        }

        public List<Artist> getArtists() {
            return artists;
        }

        public Album getAlbum() {
            return album;
        }

        public String getArtistNames() {
            StringBuilder sb = new StringBuilder();
            if (artists != null && !artists.isEmpty()) {
                for (int i = 0; i < artists.size(); i++) {
                    sb.append(artists.get(i).getName());
                    if (i < artists.size() - 1) {
                        sb.append(", ");
                    }
                }
            }
            return sb.toString();
        }
    }

    public static class Artist {
        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public static class Album {
        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("images")
        private List<Image> images;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<Image> getImages() {
            return images;
        }

        public String getAlbumArtUrl() {
            if (images != null && !images.isEmpty()) {
                // Return medium size image (usually index 1)
                if (images.size() > 1) {
                    return images.get(1).getUrl();
                }
                return images.get(0).getUrl();
            }
            return null;
        }
    }

    public static class Image {
        @SerializedName("url")
        private String url;

        @SerializedName("height")
        private int height;

        @SerializedName("width")
        private int width;

        public String getUrl() {
            return url;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }
}
