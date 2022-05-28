package de.oryfox.vinylish.lastfm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class AlbumSearch {
    private Result results;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Result {
        private AlbumMatches albummatches;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class AlbumMatches {
        private List<AlbumInfo> album;
    }
}
