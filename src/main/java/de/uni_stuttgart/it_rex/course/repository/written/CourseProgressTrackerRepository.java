package de.uni_stuttgart.it_rex.course.repository.written;

import de.uni_stuttgart.it_rex.course.domain.written.CourseProgressTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseProgressTrackerRepository
    extends JpaRepository<CourseProgressTracker, UUID>,
    JpaSpecificationExecutor<CourseProgressTracker> {
}

