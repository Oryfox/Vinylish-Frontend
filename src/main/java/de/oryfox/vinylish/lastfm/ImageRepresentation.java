package de.oryfox.vinylish.lastfm;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ImageRepresentation {
    private String size;
    @JsonAlias("#text")
    private String text;
}
