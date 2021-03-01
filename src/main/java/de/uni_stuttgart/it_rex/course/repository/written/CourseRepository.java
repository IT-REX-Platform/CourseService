package de.uni_stuttgart.it_rex.course.repository.written;

import de.uni_stuttgart.it_rex.course.domain.written.Course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data  repository for the Course entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseRepository extends JpaRepository<Course, UUID>,
    JpaSpecificationExecutor<Course> { }
