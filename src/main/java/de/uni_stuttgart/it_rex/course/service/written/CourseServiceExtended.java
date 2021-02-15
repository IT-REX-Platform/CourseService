package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.domain.written.Course;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Class contains services to be called by endpoints in
 * CourseResourceExtended.java.
 */
@Service
@Transactional
public class CourseServiceExtended {
  /**
   * Class logger.
   */
  private final Logger log =
      LoggerFactory.getLogger(CourseServiceExtended.class);

  /**
   * Course repository with all courses.
   */
  private final CourseRepository courseRepository;

  /**
   * Entity name.
   */
  private static final String ENTITY_NAME = "courseServiceExtended";

  /**
   * Constructor of CourseServiceExtended class.
   *
   * @param courseRepo Course repository.
   */
  @Autowired
  public CourseServiceExtended(final CourseRepository courseRepo) {
    this.courseRepository = courseRepo;
  }

  /**
   * Update a course without overwriting it.
   *
   * @param course the entity to use to update a created entity.
   * @return the persisted entity.
   */
  public Course patch(final Course course) {
    log.debug("Request to update Course : {}", course);
    Optional<Course> oldCourse =
        courseRepository.findById(course.getId());

    if (oldCourse.isPresent()) {
      Course oldCourseEntity = oldCourse.get();

      return courseRepository.save(oldCourseEntity);
    }
    return null;
  }


  /**
   * Method finds all courses and filters them by given parameters.
   *
   * @param publishState Publish state of course.
   * @return A list of courses that fit the given parameters.
   */
  public List<Course> findAllFiltered(
      final Optional<PUBLISHSTATE> publishState) {
    log.debug("Request to get filtered Courses");

    log.trace("Applying filters.");
    Course courseExample = applyFiltersToExample(publishState);

    return courseRepository
        .findAll(Example.of(courseExample));
  }

  /**
   * Method applies filters to an example instance of course,
   * which is used for running a search over all courses.
   * <p>
   * More filters should be added here. @s.pastuchov 30.01.21
   *
   * @param publishState Filter publish state.
   * @return Example course with applied filters for search.
   */
  private Course applyFiltersToExample(
      final Optional<PUBLISHSTATE> publishState) {
    Course course = new Course();
    publishState.ifPresent(course::setPublishState);
    return course;
  }
}
