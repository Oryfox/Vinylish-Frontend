package de.oryfox.vinylish.user;

import de.oryfox.vinylish.ImageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table
@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String email;
    private String displayName;
    private String password;
    private boolean isDisabled;
    private ImageType imageType;
}
