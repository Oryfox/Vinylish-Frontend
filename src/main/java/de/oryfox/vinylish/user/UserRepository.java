package de.oryfox.vinylish.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("select u.id from User u where u.email = :email")
    Long findIdByEmail(String email);
}
