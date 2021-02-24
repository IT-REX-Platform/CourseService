package de.uni_stuttgart.it_rex.course.repository.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.domain.written.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data  repository for the Course entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    /**
     * Bla.
     *
     * @return List of courses
     */
    @Query(value = "SELECT * FROM course WHERE DATE(start_date) <= "
        + "DATE(NOW()) AND DATE(end_date) >= DATE(NOW())",
        nativeQuery = true)
    List<Course> findAllActive();

    /**
     * Bla.
     *
     * @param publishState The publish state to search for.
     * @return List of courses
     */
    @Query(value = "SELECT * FROM course WHERE DATE(start_date) <= "
        + "DATE(NOW()) AND DATE(end_date) >= DATE(NOW()) "
        + "AND publish_state = :publishState",
        nativeQuery = true)
    List<Course> findAllActiveWithPublishState(@Param("publishState")
                                               PUBLISHSTATE publishState);
}
