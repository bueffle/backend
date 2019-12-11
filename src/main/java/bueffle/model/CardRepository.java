package bueffle.model;

import bueffle.db.entity.Card;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByQuestion(String question, Pageable pageable);

}
