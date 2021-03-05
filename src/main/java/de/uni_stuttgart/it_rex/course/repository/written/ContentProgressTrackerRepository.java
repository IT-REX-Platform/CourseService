package de.uni_stuttgart.it_rex.course.repository.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentProgressTracker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContentProgressTrackerRepository extends JpaRepository<ContentProgressTracker, UUID> {
}
