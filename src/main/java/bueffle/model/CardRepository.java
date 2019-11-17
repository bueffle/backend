package bueffle.model;

import bueffle.db.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    public List<Card> findByCollectionId(Long collectionId);

}
