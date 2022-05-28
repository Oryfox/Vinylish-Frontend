package de.oryfox.vinylish.record;

import de.oryfox.vinylish.ImageType;
import de.oryfox.vinylish.track.Track;
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
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String artist;
    private String title;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Track> tracks;
    private Integer releaseYear;
    private ImageType imageType = ImageType.NONE;
    private String color;
    private boolean limited;
    private boolean bootleg;
}
