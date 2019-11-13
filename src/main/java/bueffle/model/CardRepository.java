package bueffle.model;

import bueffle.db.entity.Card;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CardRepository extends PagingAndSortingRepository<Card, Long> {

}
