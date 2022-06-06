package de.oryfox.vinylish.user.session;

import de.oryfox.vinylish.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByUser(User user);
    Optional<Session> findByToken(String token);
    @Query("select s.user from Session s where s.token = :token")
    Optional<User> findUserByToken(String token);
    void deleteAllByUser(User user);
}
