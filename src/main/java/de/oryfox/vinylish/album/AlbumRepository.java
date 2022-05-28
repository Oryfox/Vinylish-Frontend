package de.oryfox.vinylish.album;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    @Query("select r from Record r where r.album.isDisabled is null or r.album.isDisabled = false")
    Optional<Album> findByIdAndDisabledNot(Long id);

    @Query("select a from Album a where a.isDisabled is null or a.isDisabled = false and a.artist.id = :id")
    Optional<Album> findFirstByArtistId(Long id);
}
