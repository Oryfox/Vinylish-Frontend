package de.oryfox.vinylish.record;

import de.oryfox.vinylish.album.Album;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Album album;
    private String color;
    private boolean limited;
    private boolean bootleg;
}
