package de.oryfox.vinylish.track;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("track")
@AllArgsConstructor
public class TrackController {
    final TrackRepository trackRepository;

    @GetMapping
    public List<Track> getByArtist(String artist) {
        return trackRepository.findAllByAlbumArtistNameContainingIgnoreCase(artist);
    }

    @GetMapping("all")
    public List<Track> getAllTracks() {
        return trackRepository.findAll();
    }
}
