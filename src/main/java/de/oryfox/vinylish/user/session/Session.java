package de.oryfox.vinylish.user.session;

import de.oryfox.vinylish.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Table
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    private String token = UUID.randomUUID().toString();

    public Session(User user) {
        this.user = user;
    }
}
