package de.oryfox.vinylish.album;

import de.oryfox.vinylish.artist.Artist;
import de.oryfox.vinylish.track.Track;
import de.oryfox.vinylish.ImageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Table
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(cascade = {CascadeType.ALL})
    private Artist artist;
    private String title;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "album_id")
    private List<Track> tracks;
    private Integer releaseYear;
    private ImageType imageType = ImageType.NONE;
    private boolean isDisabled;
}
