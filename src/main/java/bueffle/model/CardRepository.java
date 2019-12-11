package bueffle.model;

import bueffle.db.entity.Card;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("select q from Card q where ?1 is null or upper(q.question) like concat('%', upper(?1), '%')")
    List<Card> findByQuestion(String question, final Pageable pageable);

    List<Card> findCardsByOwnerId(Long userId, final Pageable pageable);

}
