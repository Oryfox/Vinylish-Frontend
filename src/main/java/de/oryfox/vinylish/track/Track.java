package de.oryfox.vinylish.track;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.oryfox.vinylish.album.Album;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "album_id", insertable = false, updatable = false)
    private Album album;
    private String title;
    private Integer rank;

    @JsonGetter("album")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getJsonAlbum() {
        return album != null ? album.getTitle() : null;
    }
}
