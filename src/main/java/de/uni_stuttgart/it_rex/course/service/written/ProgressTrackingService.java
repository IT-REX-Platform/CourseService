package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.CourseProgressTracker;
import de.uni_stuttgart.it_rex.course.repository.written.ContentProgressTrackerRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseProgressTrackerRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseProgressTrackerDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.CourseProgressTrackerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @return Progress DTO for the course
     */
    public CourseProgressTrackerDTO findCourseProgress(
        final UUID courseId, final UUID userId) {

        Optional<CourseProgressTracker> optTracker =
            findCourseProgressInRepository(courseId, userId);

        return courseProgressTrackerMapper.toDTO(optTracker.get());
    }

    /**
     * Initialize the progress tracking for a user in a course.
     * <p>
     * To be used only once when a user joins a course.
     * If there is already an entry for the given courseId and userId combination, no action will be taken.
     *
     * @param courseId
     * @param userId
     */

    public void startCourseProgressTracking(final UUID courseId, final UUID userId) {
        LOGGER.debug("Start course progress tracking for user {} in course {}.", userId, courseId);
        Optional<CourseProgressTracker> optTracker =
            findCourseProgressInRepository(courseId, userId);
        if (optTracker.isEmpty()) {
            // create a new one
            CourseProgressTracker newTracker = new CourseProgressTracker(
                courseId, userId);
            courseProgressTrackerRepository.saveAndFlush(newTracker);
            LOGGER.debug("Created new CourseProgressTracker: {}.", newTracker);
        }
    }

    /**
     * Retrieve a CourseProgressTracker based on the attributes courseId and userId.
     *
     * @param courseId
     * @param userId
     * @return Optional.empty() if no progress is tracked yet, else an Optional containing the Tracker
     */
    private Optional<CourseProgressTracker> findCourseProgressInRepository(
        final UUID courseId, final UUID userId) {
        CourseProgressTracker example = new CourseProgressTracker(courseId,
            userId);
        LOGGER.debug("Find course prog in repo with example: {}.", example);
        LOGGER.debug("Find course prog in repo with example: {}.", Example.of(example));
        Optional<CourseProgressTracker> result = courseProgressTrackerRepository.findOne(Example.of(example));
        LOGGER.debug("Result: {}", result);
        return result;
    }
}
