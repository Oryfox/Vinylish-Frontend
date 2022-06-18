package de.oryfox.vinylish.record;

import de.oryfox.vinylish.ImageType;
import de.oryfox.vinylish.lastfm.LastFM;
import de.oryfox.vinylish.track.Track;
import de.oryfox.vinylish.user.UserController;
import de.oryfox.vinylish.user.UserRepository;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("record")
public class RecordController {

    final RecordRepository recordRepository;
    final UserRepository userRepository;
    final UserController userController;
    final LastFM lastFM;

    @Value("${genius.secret}")
    private String geniusSecret;

    public RecordController(RecordRepository recordRepository, UserRepository userRepository, UserController userController, LastFM lastFM) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
        this.userController = userController;
        this.lastFM = lastFM;
    }

    @PostMapping
    public Record addRecord(@RequestHeader String token, @RequestBody Record record) {
        record.setCreator(userController.check(token));
        return recordRepository.save(record);
    }

    @GetMapping("auto")
    public Record retrieveRecordViaLastFM(@RequestHeader String token, @RequestParam String artist, @RequestParam String title) {
        userController.check(token);
        var aInfo = lastFM.getAlbumInfo(artist, title);
        var record = new Record();
        record.setArtist(aInfo.getArtist());
        record.setTitle(aInfo.getName());
        record.setColor("Black");
        record.setBootleg(false);
        record.setLimited(false);
        record.setReleaseYear(aInfo.getTracks() != null ? getReleaseYear(aInfo.getArtist(), aInfo.getTracks().stream().findFirst().orElse(new de.oryfox.vinylish.lastfm.Track()).getName()) : 2000);
        record.setTracks(aInfo.getTracks() != null ? aInfo.getTracks().stream().map(track -> new Track(track.getName(), track.getRank())).toList() : null);
        return record;
    }

    @GetMapping
    public ResponseEntity<Object> listRecord(@RequestHeader String token, @RequestParam(required = false) Long id, @RequestParam(required = false) String sort) {
        var user = userController.check(token);
        if (id != null) {
            var opt = recordRepository.findById(id, user);
            if (opt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            return ResponseEntity.ok(opt.get());
        } else {
            if (sort != null)
                switch (sort) {
                    case "title" -> {
                        return ResponseEntity.ok(recordRepository.findAllByCreatorOrderByTitle(user));
                    }
                    case "artist" -> {
                        return ResponseEntity.ok(recordRepository.findAllByCreatorOrderByArtist(user));
                    }
                }
            return ResponseEntity.ok(recordRepository.findAllByCreator(user));
        }
    }

    @DeleteMapping
    public void deleteRecord(@RequestHeader String token, @RequestParam Long id) {
        var user = userController.check(token);
        var opt = recordRepository.findById(id, user);
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
    public Record editRecord(@RequestHeader String token, @RequestBody Record record) {
        var user = userController.check(token);
        var opt = recordRepository.findById(record.getId(), user);
        if (opt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        record.setCreator(user);
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
                } catch (MalformedURLException | FileNotFoundException ignored) {
                    opt.get().setImageType(ImageType.DEFAULT);
                    recordRepository.save(opt.get());
                    return ResponseEntity.ok(new InputStreamResource(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png"))));
                }
            }
            case CUSTOM -> {
                var imgFile = new FileSystemResource("images/" + id);
                try {
                    return ResponseEntity.ok(new InputStreamResource(imgFile.getInputStream()));
                } catch (FileNotFoundException ignored) {
                    opt.get().setImageType(ImageType.NONE);
                    recordRepository.save(opt.get());
                    return getImage(id);
                }
            }
            default -> {
                return ResponseEntity.ok(new InputStreamResource(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png"))));
            }
        }
    }

    @SneakyThrows
    @PostMapping("image")
    public void getUserImage(@RequestParam String token, @RequestParam Long id, @RequestPart MultipartFile file) {
        var user = userController.check(token);
        var record = recordRepository.findById(id, user);
        if (record.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (file.getContentType() == null || !file.getContentType().toLowerCase().contains("image"))
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Please provide image");
        Files.write(new File("images/" + record.get().getId()).toPath(), file.getBytes());
        record.get().setImageType(ImageType.CUSTOM);
        recordRepository.save(record.get());
    }

    @SneakyThrows
    @DeleteMapping("image")
    public void deleteImage(@RequestParam Long id, @RequestParam(required = false) boolean none, @RequestHeader String token) {
        var user = userController.check(token);
        var record = recordRepository.findById(id, user);
        if (record.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        record.get().setImageType(none ? ImageType.NONE : ImageType.DEFAULT);
        recordRepository.save(record.get());
        Files.deleteIfExists(new File("images/" + record.get().getId()).toPath());
    }

    public int getReleaseYear(String artist, String title) {
        if (title == null)
            return 2000;

        try {
            var request = HttpRequest.newBuilder(URI.create("https://api.genius.com/search?q=" + URLEncoder.encode(artist + " " + title, StandardCharsets.UTF_8)))
                    .header("Authorization", "Bearer " + geniusSecret).GET().build();
            var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200)
                return new JSONObject(response.body())
                        .getJSONObject("response")
                        .getJSONArray("hits")
                        .getJSONObject(0)
                        .getJSONObject("result")
                        .getJSONObject("release_date_components")
                        .getInt("year");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 2000;
    }
}
