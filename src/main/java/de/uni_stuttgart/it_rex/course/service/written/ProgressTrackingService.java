package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentProgressTracker;
import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.domain.written_entities.CourseProgressTracker;
import de.uni_stuttgart.it_rex.course.repository.written.ContentProgressTrackerRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseProgressTrackerRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentProgressTrackerDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseProgressTrackerDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.ContentProgressTrackerMapper;
import de.uni_stuttgart.it_rex.course.service.mapper.written.ContentReferenceMapper;
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
     * Content tracker mapper.
     */
    private final ContentProgressTrackerMapper contentProgressTrackerMapper;

    /**
     * ContentReference mapper.
     */
    private final ContentReferenceMapper contentReferenceMapper;

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
        final ContentProgressTrackerRepository contentProgressTrackerRepository,
        final ContentProgressTrackerMapper contentProgressTrackerMapper,
        final ContentReferenceMapper contentReferenceMapper) {
        this.courseProgressTrackerRepository = courseProgressTrackerRepository;
        this.courseProgressTrackerMapper = courseProgressTrackerMapper;
        this.contentProgressTrackerRepository =
            contentProgressTrackerRepository;
        this.contentProgressTrackerMapper = contentProgressTrackerMapper;
        this.contentReferenceMapper = contentReferenceMapper;
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
    public CourseProgressTrackerDTO startCourseProgressTracking(final UUID courseId, final UUID userId) {
        LOGGER.debug("Start course progress tracking for user {} in course {}.", userId, courseId);

        // create a new one
        CourseProgressTracker newTracker = new CourseProgressTracker(
            courseId, userId);
        courseProgressTrackerRepository.saveAndFlush(newTracker);
        LOGGER.debug("Created new CourseProgressTracker: {}.", newTracker);

        return courseProgressTrackerMapper.toDTO(newTracker);
    }

    /**
     * Initialize the content progress tracking for a user regarding one specific content reference.
     *
     * To be used only once per user to create a tracker for the content reference.
     *
     * @param contentReference
     * @param userId
     * @return the created {@link de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO}
     */
    public ContentProgressTrackerDTO startContentProgressTracking(final ContentReference contentReference, final UUID userId){
        LOGGER.debug("Start course progress tracking for user {} regarding content ref {}.", userId, contentReference);

        ContentProgressTracker newTracker = new ContentProgressTracker(userId, contentReference);
        contentProgressTrackerRepository.saveAndFlush(newTracker);
        LOGGER.debug("Created new ContentProgressTracker: {}.", newTracker);

        return contentProgressTrackerMapper.toDTO(newTracker);
    }

    /**
     * Find a DTO for the course-wide progress of the current user.
     * If the course progress is not yet tracked for the current user, create a tracker.
     *
     * @param courseId Course to find the progress for
     * @param userId id of user
     * @return Progress DTO for the course
     */
    public CourseProgressTrackerDTO findCourseProgressInRepositoryOrStartFresh(
        final UUID courseId, final UUID userId) {

        Optional<CourseProgressTracker> result = courseProgressTrackerRepository.findOne(getCourseProgressSpec(courseId, userId));
        LOGGER.debug("Result: {}", result);

        if (result.isEmpty()) {
            return startCourseProgressTracking(courseId, userId);
        }

        return courseProgressTrackerMapper.toDTO(result.get());
    }

    /**
     * Retrieve or create a ContentProgressTracker for a user and a content reference.
     * If the progress is not yet tracked for the current user, create a tracker.
     *
     * @param contentReference
     * @param userId
     * @return a tracker
     */
    public ContentProgressTrackerDTO findContentProgressInRepositoryOrStartFresh(
        final ContentReference contentReference, final UUID userId) {

        Optional<ContentProgressTracker> result =
            contentProgressTrackerRepository
                .findOne(getContentProgressSpec(contentReference, userId));
        LOGGER.debug("Result: {}", result);

        if (result.isEmpty()) {
            return startContentProgressTracking(contentReference, userId);
        }

        return contentProgressTrackerMapper.toDTO(result.get());
    }

    /**
     * Method generates a specification that describes our desired tracker
     * properties.
     *
     * @param courseId a course id
     * @param userId a user id
     * @return A specification describing said tracker
     */
    private Specification<CourseProgressTracker> getCourseProgressSpec(
        final UUID courseId, final UUID userId) {
        return (root, query, builder) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(builder.equal(root.get("courseId"), courseId));
            predicates.add(builder.equal(root.get("userId"), userId));

            return builder.and(predicates.toArray(
                new Predicate[predicates.size()]));
        };
    }

    /**
     * Method generates a specification that describes our desired tracker
     * properties.
     *
     * @param contentReference a content reference id
     * @param userId a user id
     * @return A specification describing said tracker
     */
    private Specification<ContentProgressTracker> getContentProgressSpec(
        final ContentReference contentReference, final UUID userId) {
        return (root, query, builder) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(builder.equal(root.get("contentReference"), contentReference));
            predicates.add(builder.equal(root.get("userId"), userId));

            return builder.and(predicates.toArray(
                new Predicate[predicates.size()]));
        };
    }
}
