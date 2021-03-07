package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.CourseProgressTracker;
import de.uni_stuttgart.it_rex.course.repository.written.ContentProgressTrackerRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseProgressTrackerRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseProgressTrackerDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.CourseProgressTrackerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ProgressTrackingService {

    /**
     * Logger.
     */
    private static final Logger LOGGER
        = LoggerFactory.getLogger(ProgressTrackingService.class);

    /**
     * Tracker Repository.
     */
    private final CourseProgressTrackerRepository
        courseProgressTrackerRepository;

    /**
     * Tracker mapper.
     */
    private final CourseProgressTrackerMapper courseProgressTrackerMapper;

    /**
     * Content tracker repository.
     */
    private final ContentProgressTrackerRepository
        contentProgressTrackerRepository;

    /**
     * Constructor.
     *
     * @param courseProgressTrackerRepository
     * @param courseProgressTrackerMapper
     * @param contentProgressTrackerRepository
     */
    @Autowired
    public ProgressTrackingService(
        final CourseProgressTrackerRepository courseProgressTrackerRepository,
        final CourseProgressTrackerMapper courseProgressTrackerMapper,
        final ContentProgressTrackerRepository contentProgressTrackerRepository) {
        this.courseProgressTrackerRepository = courseProgressTrackerRepository;
        this.courseProgressTrackerMapper = courseProgressTrackerMapper;
        this.contentProgressTrackerRepository =
            contentProgressTrackerRepository;
    }

    /**
     * Find a DTO for the course-wide progress of the current user.
     *
     * @param courseId Course to find the progress for
     * @param userId id of user
     * @return Progress DTO for the course
     */
    public CourseProgressTrackerDTO findCourseProgress(
        final UUID courseId, final UUID userId) {

        CourseProgressTracker tracker =
            findCourseProgressInRepositoryOrStartFresh(courseId, userId);

        return courseProgressTrackerMapper.toDTO(tracker);
    }

    /**
     * Initialize the progress tracking for a user in a course.
     * <p>
     * To be used only once when a user joins a course.
     * If there is already an entry for the given courseId and userId combination, no action will be taken.
     *
     * @param courseId
     * @param userId
     * @return the newly created tracking entity
     */

    public CourseProgressTracker startCourseProgressTracking(final UUID courseId, final UUID userId) {
        LOGGER.debug("Start course progress tracking for user {} in course {}.", userId, courseId);

            // create a new one
            CourseProgressTracker newTracker = new CourseProgressTracker(
                courseId, userId);
            courseProgressTrackerRepository.saveAndFlush(newTracker);
            LOGGER.debug("Created new CourseProgressTracker: {}.", newTracker);

            return newTracker;
    }

    /**
     * Retrieve a CourseProgressTracker based on the attributes courseId and userId.
     *
     * @param courseId
     * @param userId
     * @return a tracker
     */
    private CourseProgressTracker findCourseProgressInRepositoryOrStartFresh(
        final UUID courseId, final UUID userId) {

        Optional<CourseProgressTracker> result = courseProgressTrackerRepository.findOne(getSpec(courseId, userId));
        LOGGER.debug("Result: {}", result);

        if (result.isEmpty()) {
            return startCourseProgressTracking(courseId, userId);
        }

        return result.get();
    }

    /**
     * Method generates a specification that describes our desired tracker
     * properties.
     *
     * @param courseId a course id
     * @param userId a user id
     * @return A specification describing said tracker
     */
    private Specification<CourseProgressTracker> getSpec(
        final UUID courseId, final UUID userId) {
        return (root, query, builder) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(builder.equal(root.get("courseId"), courseId));
            predicates.add(builder.equal(root.get("userId"), userId));

            return builder.and(predicates.toArray(
                new Predicate[predicates.size()]));
        };
    }
}
