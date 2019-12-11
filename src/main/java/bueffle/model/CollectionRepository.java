package bueffle.model;

import bueffle.db.entity.Collection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    @Query("select q from Collection q where ?1 is null or upper(q.name) like concat('%', upper(?1), '%')")
    List<Collection> findByName(String name, final Pageable pageable);

    List<Collection> findCardsByOwnerId(Long userId, final Pageable pageable);

}
