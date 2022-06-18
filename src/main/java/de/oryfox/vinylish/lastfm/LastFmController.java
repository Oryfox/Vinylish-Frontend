package de.oryfox.vinylish.lastfm;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("lastfm")
@AllArgsConstructor
public class LastFmController {
    final LastFM lastFM;

    @SneakyThrows
    @GetMapping("info")
    public AlbumInfo getAlbumInfo(String artist, String album) {
        return lastFM.getAlbumInfo(artist, album);
    }

    @GetMapping("search")
    public List<AlbumInfo> searchAlbum(String query) {
        return lastFM.search(query);
    }
}
