package de.oryfox.vinylish.record;

import de.oryfox.vinylish.ImageType;
import de.oryfox.vinylish.lastfm.LastFM;
import de.oryfox.vinylish.track.Track;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("record")
@AllArgsConstructor
public class RecordController {

    final RecordRepository recordRepository;
    final LastFM lastFM;

    @PostMapping
    public Record addRecord(@RequestBody Record record) {
        return recordRepository.save(record);
    }

    @GetMapping("auto")
    public Record retrieveRecordViaLastFM(@RequestParam String artist, @RequestParam String title) {
        var aInfo = lastFM.getAlbumInfo(artist,title);
        var record = new Record();
        record.setArtist(aInfo.getArtist());
        record.setTitle(aInfo.getName());
        record.setColor("Black");
        record.setBootleg(false);
        record.setLimited(false);
        record.setReleaseYear(2000);
        record.setTracks(aInfo.getTracks().stream().map(track -> new Track(track.getName(), track.getRank())).toList());
        return record;
    }

    @GetMapping
    public ResponseEntity<Object> listRecord(@RequestParam(required = false) Long id) {
        if (id != null) {
            var opt = recordRepository.findById(id);
            if (opt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            return ResponseEntity.ok(opt.get());
        } else {
            return ResponseEntity.ok(recordRepository.findAll());
        }
    }

    @DeleteMapping
    public void deleteRecord(@RequestParam Long id) {
        var opt = recordRepository.findById(id);
        if (opt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        try {
            Files.deleteIfExists(new File("images/" + opt.get().getId()).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        recordRepository.delete(opt.get());
    }

    @GetMapping("colors")
    public List<String> listColors() {
        return recordRepository.getAllColors();
    }

    @PutMapping
    public Record editRecord(@RequestBody Record record) {
        var opt = recordRepository.findById(record.getId());
        if (opt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return recordRepository.save(record);
    }

    @SneakyThrows
    @GetMapping("image")
    public ResponseEntity<InputStreamResource> getImage(@RequestParam Long id) {
        var opt = recordRepository.findById(id);
        if (opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Album does not exist");
        }

        if (opt.get().getImageType() == null) opt.get().setImageType(ImageType.NONE);
        switch (opt.get().getImageType()) {
            case NONE -> {
                try {
                    var urls = lastFM.getAlbumInfo(opt.get().getArtist(), opt.get().getTitle()).getImage();
                    ImageIO.write(ImageIO.read(new URL(urls.get(urls.size() - 1).getText())), "png", new File("images/" + id));
                    opt.get().setImageType(ImageType.CUSTOM);
                    recordRepository.save(opt.get());
                    return getImage(id);
                } catch (FileNotFoundException ignored) {
                    opt.get().setImageType(ImageType.DEFAULT);
                    recordRepository.save(opt.get());
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
        var recordOpt = recordRepository.findById(id);
        if (recordOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Album does not exist");
        }
        try (var fos = new FileOutputStream("images/" + id)) {
            file.getInputStream().transferTo(fos);
            fos.close();
            recordOpt.get().setImageType(ImageType.CUSTOM);
            recordRepository.save(recordOpt.get());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PutMapping("image")
    public void setImage(@RequestParam("id") Long id, @RequestPart("image") MultipartFile file) {
        var recordOpt = recordRepository.findById(id);
        if (recordOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Album does not exist");
        }
        try (var fos = new FileOutputStream("images/" + id)) {
            file.getInputStream().transferTo(fos);
            fos.close();
            recordOpt.get().setImageType(ImageType.CUSTOM);
            recordRepository.save(recordOpt.get());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @DeleteMapping("image")
    public void removeImage(@RequestParam("id") Long id) {
        var recordOpt = recordRepository.findById(id);
        if (recordOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Album does not exist");
        }
        Files.deleteIfExists(new File("images/" + recordOpt.get().getId()).toPath());
        recordOpt.get().setImageType(ImageType.DEFAULT);
        recordRepository.save(recordOpt.get());
    }
}
