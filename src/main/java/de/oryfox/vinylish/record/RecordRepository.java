package de.oryfox.vinylish.record;

import de.oryfox.vinylish.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    @Query("select distinct r.color from Record r where r.color is not null")
    List<String> getAllColors();

    @Query("select r from Record r where r.creator = :creator and r.id = :id")
    Optional<Record> findById(Long id, User creator);

    List<Record> findAllByCreator(User creator);

    List<Record> findAllByCreatorAndArtist(User creator, String artist);

    @Query("select distinct r.artist from Record r where r.creator = :user")
    List<String> findAllArtists(User user);
}
