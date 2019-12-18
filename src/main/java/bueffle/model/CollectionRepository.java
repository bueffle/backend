package bueffle.model;

import bueffle.entity.Collection;
import bueffle.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    @Query("select q from Collection q where ?1 is null or upper(q.name) like concat('%', upper(?1), '%')")
    List<Collection> findByName(String name, final Pageable pageable);

    List<Collection> findCollectionsByOwnerId(Long userId, final Pageable pageable);

    List<Collection> findByisPublicTrue();

    List<Collection> findByOwner(User owner);

}
