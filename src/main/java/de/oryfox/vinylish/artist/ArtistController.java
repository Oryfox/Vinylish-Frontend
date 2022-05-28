package de.oryfox.vinylish.artist;

import de.oryfox.vinylish.ImageType;
import de.oryfox.vinylish.album.AlbumRepository;
import de.oryfox.vinylish.lastfm.LastFM;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Objects;

@RestController
@RequestMapping("artist")
@AllArgsConstructor
public class ArtistController {
    final ArtistRepository artistRepository;
    final AlbumRepository albumRepository;
    final LastFM lastFM;

    @GetMapping
    public Artist artist(@RequestParam Long id) {
        var opt = artistRepository.findById(id);
        if (opt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return opt.get();
    }

    @SneakyThrows
    @GetMapping("image")
    public ResponseEntity<InputStreamResource> getImage(@RequestParam Long id) {
        var opt = albumRepository.findFirstByArtistId(id);
        if (opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Album does not exist");
        }

        if (opt.get().getImageType() == null) opt.get().setImageType(ImageType.NONE);
        switch (opt.get().getImageType()) {
            case NONE -> {
                try {
                    var urls = lastFM.getAlbumInfo(opt.get().getArtist().getName(), opt.get().getTitle()).getImage();
                    ImageIO.write(ImageIO.read(new URL(urls.get(urls.size() - 1).getText())), "png", new File("images/" + opt.get().getId()));
                    opt.get().setImageType(ImageType.CUSTOM);
                    albumRepository.save(opt.get());
                    return getImage(id);
                } catch (FileNotFoundException ignored) {
                    opt.get().setImageType(ImageType.DEFAULT);
                    albumRepository.save(opt.get());
                    return ResponseEntity.ok(new InputStreamResource(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png"))));
                }
            }
            case CUSTOM -> {
                var imgFile = new FileSystemResource("images/" + opt.get().getId());
                return ResponseEntity.ok(new InputStreamResource(imgFile.getInputStream()));
            }
            default -> {
                return ResponseEntity.ok(new InputStreamResource(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png"))));
            }
        }
    }
}
