package de.oryfox.vinylish.lastfm;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class AlbumInfo {
    private String artist;
    private String playcount;
    private List<ImageRepresentation> image;
    private String url;
    private String name;
    private TrackResponse tracks;

    @JsonGetter("tracks")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<Track> getTracks() {
        return tracks != null ? tracks.getTrack() : null;
    }

    @NoArgsConstructor
    @Setter
    @Getter
    public static class AlbumInfoResponse {
        private AlbumInfo album;
    }

    @NoArgsConstructor
    @Setter
    @Getter
    public static class TrackResponse {
        private List<Track> track;
    }
}
