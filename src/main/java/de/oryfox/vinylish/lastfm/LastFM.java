package de.oryfox.vinylish.lastfm;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class LastFM {

    private static final String baseUrl = "https://ws.audioscrobbler.com/2.0";
    
    @Value("${lastfm.apikey}")
    private String key;

    @SneakyThrows
    public AlbumInfo getAlbumInfo(String artist, String album) {
        try {
            return new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(new URL(
                                    String.format("%s?api_key=%s&artist=%s&album=%s&format=json&method=album.getInfo",
                                            baseUrl,
                                            key,
                                            enc(artist),
                                            enc(album))),
                            AlbumInfo.AlbumInfoResponse.class)
                    .getAlbum();
        } catch (FileNotFoundException ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @SneakyThrows
    public List<AlbumInfo> search(String query) {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(new URL(
                                String.format("%s?api_key=%s&album=%s&format=json&method=album.search",
                                        baseUrl,
                                        key,
                                        enc(query))),
                        AlbumSearch.class)
                .getResults()
                .getAlbummatches().getAlbum();
    }



    private String enc(String unenc) {
        return URLEncoder.encode(unenc, StandardCharsets.UTF_8);
    }
}
