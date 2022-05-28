package de.oryfox.vinylish.record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    @Query("select distinct r.color from Record r where r.color is not null")
    List<String> getAllColors();
}
