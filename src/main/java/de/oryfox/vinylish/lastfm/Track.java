package de.oryfox.vinylish.lastfm;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Track {
    private String name;
    private Integer duration;
    private String url;
    @JsonAlias({"@attr"})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Attribute attribute;

    @JsonGetter("rank")
    public Integer getRank() {
        return attribute.getRank();
    }
}
