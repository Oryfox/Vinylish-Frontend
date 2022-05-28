package de.oryfox.vinylish.album;

import de.oryfox.vinylish.lastfm.LastFM;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import de.oryfox.vinylish.ImageType;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("album")
@AllArgsConstructor
public class AlbumController {
    final AlbumRepository albumRepository;
    final LastFM lastFM;

    @PostMapping
    public Album postAlbum(@RequestBody Album album) {
        System.out.println(album);
        return albumRepository.save(album);
    }

    @GetMapping("all")
    public List<Album> getAll() {
        return albumRepository.findAll();
    }

    @GetMapping
    public Album getAlbum(@RequestParam Long id) {
        return albumRepository.findByIdAndDisabledNot(id).orElseThrow();
    }

    @PutMapping
    public Album updateAlbum(@RequestBody Album album) {
        return albumRepository.save(album);
    }

    @DeleteMapping
    public void deleteAlbum(@RequestParam Long id) {
        var album = albumRepository.findByIdAndDisabledNot(id).orElseThrow();
        album.setDisabled(true);
        albumRepository.save(album);
    }

    @SneakyThrows
    @GetMapping("image")
    public ResponseEntity<InputStreamResource> getImage(@RequestParam Long id) {
        var opt = albumRepository.findById(id);
        if (opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Album does not exist");
        }

        if (opt.get().getImageType() == null) opt.get().setImageType(ImageType.NONE);
        switch (opt.get().getImageType()) {
            case NONE -> {
                try {
                    var urls = lastFM.getAlbumInfo(opt.get().getArtist().getName(), opt.get().getTitle()).getImage();
                    ImageIO.write(ImageIO.read(new URL(urls.get(urls.size() - 1).getText())), "png", new File("images/" + id));
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
                var imgFile = new FileSystemResource("images/" + id);
                return ResponseEntity.ok(new InputStreamResource(imgFile.getInputStream()));
            }
            default -> {
                return ResponseEntity.ok(new InputStreamResource(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png"))));
            }
        }
    }

    @PostMapping("image")
    public void addImage(@RequestParam("id") Long id, @RequestPart("image") MultipartFile file) {
        var albumOpt = albumRepository.findById(id);
        if (albumOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Album does not exist");
        }
        try (var fos = new FileOutputStream("images/" + id)) {
            file.getInputStream().transferTo(fos);
            fos.close();
            albumOpt.get().setImageType(ImageType.CUSTOM);
            albumRepository.save(albumOpt.get());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PutMapping("image")
    public void setImage(@RequestParam("id") Long id, @RequestPart("image") MultipartFile file) {
        var albumOpt = albumRepository.findById(id);
        if (albumOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Album does not exist");
        }
        try (var fos = new FileOutputStream("images/" + id)) {
            file.getInputStream().transferTo(fos);
            fos.close();
            albumOpt.get().setImageType(ImageType.CUSTOM);
            albumRepository.save(albumOpt.get());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @DeleteMapping("image")
    public void removeImage(@RequestParam("id") Long id) {
        var albumOpt = albumRepository.findById(id);
        if (albumOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Album does not exist");
        }
        Files.deleteIfExists(new File("images/" + albumOpt.get().getId()).toPath());
        albumOpt.get().setImageType(ImageType.DEFAULT);
        albumRepository.save(albumOpt.get());
    }
}
