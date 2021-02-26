package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.domain.written.Course;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.mapper.written.CourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link Course}.
 */
@Service
@Transactional
public class CourseService {

    /**
     * Logger.
     */
    private static final Logger LOGGER
        = LoggerFactory.getLogger(CourseService.class);

    /**
     * CourseRepository.
     */
    private final CourseRepository courseRepository;

    /**
     * Course mapper.
     */
    private CourseMapper courseMapper;

    /**
     * Constructor.
     *
     * @param newCourseRepository the course repository.
     * @param newCourseMapper     the course mapper.
     */
    @Autowired
    public CourseService(final CourseRepository newCourseRepository,
                         final CourseMapper newCourseMapper) {
        this.courseRepository = newCourseRepository;
        this.courseMapper = newCourseMapper;
    }


    /**
     * Save a course.
     *
     * @param course the entity to save.
     * @return the persisted entity.
     */
    public Course save(final Course course) {
        LOGGER.debug("Request to save Course : {}", course);
        return courseRepository.save(course);
    }

    /**
     * Get all the courses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Course> findAll() {
        LOGGER.debug("Request to get all Courses");
        return courseRepository.findAll();
    }


    /**
     * Get one course by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Course> findOne(final UUID id) {
        LOGGER.debug("Request to get Course : {}", id);
        return courseRepository.findById(id);
    }

    /**
     * Delete the course by id.
     *
     * @param id the id of the entity.
     */
    public void delete(final UUID id) {
        LOGGER.debug("Request to delete Course : {}", id);
        courseRepository.deleteById(id);
    }

    /**
     * Update a course without overwriting it.
     *
     * @param course the entity to use to update a created entity.
     * @return the persisted entity.
     */
    public Course patch(final Course course) {
        LOGGER.debug("Request to update Course : {}", course);
        Optional<Course> oldCourse =
            courseRepository.findById(course.getId());

        if (oldCourse.isPresent()) {
            Course oldCourseEntity = oldCourse.get();
            courseMapper.updateCourseFromCourse(course, oldCourseEntity);
            return courseRepository.save(oldCourseEntity);
        }
        return null;
    }

    /**
     * Method finds all courses and filters them by given parameters.
     *
     * @param publishState Publish state of course.
     * @param activeOnly   Set true to only include active courses (current time
     *                     between course start and end date + offset).
     * @return A list of courses that fit the given parameters.
     */
    public List<Course> findAll(
        final Optional<PUBLISHSTATE> publishState,
        final Optional<Boolean> activeOnly) {
        LOGGER.debug("Request to get filtered Courses");
        LOGGER.trace("Applying filters.");

        List<Course> courses = null;

        Example courseExample = Example.of(applyFiltersToExample(publishState));
        Specification<Course> spec =
            getSpecFromActiveAndExample(activeOnly, courseExample);
        courses = courseRepository.findAll(spec);

        return courses;
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

    /**
     * Method generates a specification that describes Course objects according
     * to an example that also optionally have an endDate greater than or equal
     * to the current date.
     *
     * @param activeOnly (Optional) set endDate >= LocalDate.now()
     * @param example    Example course object
     * @return A specification describing said Course object(s).
     */
    private Specification<Course> getSpecFromActiveAndExample(
        final Optional<Boolean> activeOnly, final Example<Course> example) {
        return (Specification<Course>) (root, query, builder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (activeOnly.isPresent() && activeOnly.get()) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("endDate"),
                    LocalDate.now()));
            }

            predicates.add(QueryByExamplePredicateBuilder
                .getPredicate(root, builder, example));

            return builder.and(predicates.toArray(
                new Predicate[predicates.size()]));
        };
    }
}
