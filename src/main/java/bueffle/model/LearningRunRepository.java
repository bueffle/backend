package bueffle.model;

import bueffle.db.entity.LearningRun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningRunRepository extends JpaRepository<LearningRun, Long> {

}
