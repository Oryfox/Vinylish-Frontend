package de.oryfox.vinylish.artist;

import de.oryfox.vinylish.ImageType;
import de.oryfox.vinylish.record.Record;
import de.oryfox.vinylish.record.RecordRepository;
import de.oryfox.vinylish.user.UserController;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/artist")
public class ArtistController {

    final RecordRepository recordRepository;
    final ArtistRepository artistRepository;
    final UserController userController;

    @Value("${genius.secret}")
    private String geniusSecret;

    public ArtistController(RecordRepository recordRepository, ArtistRepository artistRepository, UserController userController) {
        this.recordRepository = recordRepository;
        this.artistRepository = artistRepository;
        this.userController = userController;
    }

    @GetMapping("/{name}")
    public List<Record> getRecordsByArtistName(@PathVariable String name, @RequestHeader String token) {
        return recordRepository.findAllByCreatorAndArtist(userController.check(token), name);
    }

    @GetMapping
    public List<String> getArtists(@RequestHeader String token) {
        return recordRepository.findAllArtists(userController.check(token));
    }

    @GetMapping(value = "/{name}/image")
    public ResponseEntity<Object> getArtistImage(@PathVariable String name) {
        var opt = artistRepository.findFirstByName(name);
        if (opt.isEmpty()) {
            getImage(name);
            return getArtistImage(name);
        } else {
            if (opt.get().getImageType() == ImageType.CUSTOM) {
                var imgFile = new FileSystemResource("images/" + opt.get().getId());
                try {
                    return ResponseEntity.ok(new InputStreamResource(imgFile.getInputStream()));
                } catch (IOException ignored) {
                    opt.get().setImageType(ImageType.NONE);
                    artistRepository.delete(opt.get());
                    return getArtistImage(name);
                }
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @SneakyThrows
    public void getImage(String name) {
        var request = HttpRequest
                .newBuilder(URI.create("https://api.genius.com/search?q=" + URLEncoder.encode(name, StandardCharsets.UTF_8)))
                .header("Authorization", "Bearer " + geniusSecret)
                .GET()
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            var artist = new Artist();
            artist.setName(name);
            artist = artistRepository.save(artist);
            var jsonRoot = new JSONObject(response.body());
            var url = jsonRoot
                    .getJSONObject("response")
                    .getJSONArray("hits")
                    .getJSONObject(0)
                    .getJSONObject("result")
                    .getJSONObject("primary_artist")
                    .getString("image_url");

            try {
                ImageIO.write(ImageIO.read(new URL(url)), "png", new File("images/" + artist.getId()));
                artist.setImageType(ImageType.CUSTOM);
                artistRepository.save(artist);
            } catch (FileNotFoundException ignored) {
                artist.setImageType(ImageType.DEFAULT);
                artistRepository.save(artist);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
