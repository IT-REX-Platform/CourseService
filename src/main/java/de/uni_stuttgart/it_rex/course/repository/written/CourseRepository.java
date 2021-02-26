package de.uni_stuttgart.it_rex.course.repository.written;

import de.uni_stuttgart.it_rex.course.domain.written.Course;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data  repository for the Course entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseRepository extends JpaRepository<Course, UUID>,
    JpaSpecificationExecutor<Course> {

    /**
     * Bla.
     *
     * @return List of courses
    */
    @Query(value = "SELECT * FROM course WHERE end_date >= CURRENT_DATE()",
        nativeQuery = true)
    List<Course> findAllActive();

    /**
     * Bla.
     *
     * @param publishState The publish state to search for.
     * @return List of courses
     */
    @Query(value = "SELECT * FROM course WHERE end_date >= CURRENT_DATE() "
        + "AND publish_state = ?1",
        nativeQuery = true)
    List<Course> findAllActiveWithPublishState(@Param("publishState")
                                                   String publishState);
}
