package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.domain.written.ContentReference;
import de.uni_stuttgart.it_rex.course.domain.written.ContentProgressTracker;
import de.uni_stuttgart.it_rex.course.domain.written.CourseProgressTracker;
import de.uni_stuttgart.it_rex.course.repository.written.ContentProgressTrackerRepository;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseProgressTrackerRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentProgressTrackerDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseProgressTrackerDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.ContentProgressTrackerMapper;
import de.uni_stuttgart.it_rex.course.service.mapper.written.CourseProgressTrackerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import javax.ws.rs.NotFoundException;
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
     * Required in order to attach content progress trackers to existing content references only.
     */
    private final ContentReferenceRepository contentReferenceRepository;

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
        final ContentReferenceRepository contentReferenceRepository) {
        this.courseProgressTrackerRepository = courseProgressTrackerRepository;
        this.courseProgressTrackerMapper = courseProgressTrackerMapper;
        this.contentProgressTrackerRepository =
            contentProgressTrackerRepository;
        this.contentProgressTrackerMapper = contentProgressTrackerMapper;
        this.contentReferenceRepository = contentReferenceRepository;
    }

    /**
     * Initialize the content progress tracking for a user regarding one specific content reference.
     *
     * To be used only once per user to create a tracker for the content reference.
     *
     * As all ContentProgressTrackers logically belong to one @{@link CourseProgressTracker} as all contents
     * belong to a course, from a business logic perspective, the relationship is already implicitly defined.
     * However, for fast access from a @{@link CourseProgressTracker} to all its ContentProgressTrackers, we
     * explicitly store this relationship.
     *
     * @param contentReferenceDTO the content reference to track - must be valid
     * @param userId the user to start the tracking for
     * @param courseProgressTrackerId the ID of the course progress tracker that the content reference belongs to,
     *                                must be a valid and existing ID
     * @return the created {@link de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO}
     *
     * @throws NotFoundException if either the content reference or the course progress tracker do not exist
     */
    @Transactional
    public ContentProgressTrackerDTO startContentProgressTracking(final ContentReferenceDTO contentReferenceDTO, final UUID userId, final UUID courseProgressTrackerId){
        Optional<CourseProgressTracker> courseProgressTrackerOptional = courseProgressTrackerRepository.findById(courseProgressTrackerId);
        if (courseProgressTrackerOptional.isEmpty()){
            throw new NotFoundException(String.format("There is no CourseProgressTracker with the id %s", courseProgressTrackerId));
        }

        Optional<ContentReference> contentReferenceOptional = contentReferenceRepository.findById(contentReferenceDTO.getId());
        if (contentReferenceOptional.isEmpty()){
            throw new NotFoundException(String.format("There is no ContentReference with the id %s", contentReferenceDTO.getId()));
        }

        CourseProgressTracker courseProgressTracker = courseProgressTrackerOptional.get();
        ContentReference contentReference = contentReferenceOptional.get();

        LOGGER.debug("Start course progress tracking for user {} regarding content ref {}.", userId, contentReference.getId());

        ContentProgressTracker newTracker = new ContentProgressTracker(userId, contentReference, courseProgressTracker);
        contentProgressTrackerRepository.saveAndFlush(newTracker);
        LOGGER.debug("Created new ContentProgressTracker: {}.", newTracker);

        return contentProgressTrackerMapper.toDTO(newTracker);
    }

    /**
     * Update the state of a content progress tracker to "COMPLETED".
     *
     * @param trackerId the tracker to complete, is required to be a valid, existing trackerId.
     *
     * @throws NotFoundException if the trackerId is invalid/tracker does not exist
     */
    @Transactional
    public ContentProgressTrackerDTO completeContentProgressTracker(final UUID trackerId){
        Optional<ContentProgressTracker> trackerOptional = contentProgressTrackerRepository.findById(trackerId);
        if (trackerOptional.isPresent()){
            ContentProgressTracker tracker = trackerOptional.get();
            tracker.complete();
            contentProgressTrackerRepository.saveAndFlush(tracker);
            return contentProgressTrackerMapper.toDTO(tracker);
        } else {
            throw new NotFoundException(String.format("The given tracker id %s does not exist!", trackerId));
        }
    }

    /**
     * Update the progress of a content progress tracker.
     *
     * @param trackerId the tracker to update, is required to be a valid, existing trackerId.
     * @param progress the progress to store
     *
     * @throws NotFoundException if the trackerId is invalid/tracker does not exist
     */
    @Transactional
    public ContentProgressTrackerDTO updateContentProgress(final UUID trackerId, final float progress){
        Optional<ContentProgressTracker> trackerOptional = contentProgressTrackerRepository.findById(trackerId);
        if (trackerOptional.isPresent()){
            ContentProgressTracker tracker = trackerOptional.get();
            tracker.setProgress(progress);
            contentProgressTrackerRepository.saveAndFlush(tracker);
            return contentProgressTrackerMapper.toDTO(tracker);
        } else {
            throw new NotFoundException(String.format("The given tracker id %s does not exist!", trackerId));
        }
    }

    /**
     * Get ContentTracker by id
     *
     * @param trackerId id of ContentTracker
     * @return the ContentTracker
     */
    @Transactional(readOnly = true)
    public Optional<ContentProgressTrackerDTO> findContentProgressTracker(final UUID trackerId) {
        LOGGER.debug("Request to get ContentTracker : {}", trackerId);
        return contentProgressTrackerMapper.toDTO(contentProgressTrackerRepository.findById(trackerId));
    }


    /**
     * Find a DTO for the course-wide progress of the current user.
     * If the course progress is not yet tracked for the current user, create a tracker.
     *
     * @param courseId Course to find the progress for
     * @param userId id of user
     * @return Progress DTO for the course
     */
    @Transactional
    public CourseProgressTrackerDTO findOrCreateCourseProgressTracker(
        final UUID courseId, final UUID userId) {

        Optional<CourseProgressTracker> result = courseProgressTrackerRepository.findOne(getCourseProgressSpec(courseId, userId));

        if (result.isEmpty()) {
            return startCourseProgressTracking(courseId, userId);
        }

        return courseProgressTrackerMapper.toDTO(result.get());
    }

    /**
     * Set the last accessed contentReference for a user.
     *
     * This information is stored per user per course, and for this purpose there is
     * exactly one @{@link CourseProgressTracker}-object per user per course.
     * For faster access, the CourseProgressTracker is identified by its Id.
     *
     * @param courseTrackerId a valid, existing Id of a course tracker
     * @param contentReferenceDTO the last accessed contentReferenceDTO to store
     *
     * @return the updated {@link CourseProgressTrackerDTO}-object containing the new reference
     *
     * @throws NotFoundException if either the tracker Id or the content reference Id are invalid
     */
    @Transactional
    public CourseProgressTrackerDTO updateLastAccessedContentReference(
        final UUID courseTrackerId,
        final ContentReferenceDTO contentReferenceDTO
    ){
        Optional<CourseProgressTracker> courseProgressTrackerOptional = courseProgressTrackerRepository.findById(courseTrackerId);
        if (courseProgressTrackerOptional.isEmpty()){
            throw new NotFoundException(String.format("There is no CourseProgressTracker with the id %s", courseTrackerId));
        }
        Optional<ContentReference> contentReferenceOptional = contentReferenceRepository.findById(contentReferenceDTO.getId());
        if (contentReferenceOptional.isEmpty()){
            throw new NotFoundException(String.format("There not ContentReference with the id %s", contentReferenceDTO.getId()));
        }

        CourseProgressTracker courseProgressTracker = courseProgressTrackerOptional.get();
        ContentReference lastAccessedContentReference = contentReferenceOptional.get();

        courseProgressTracker.setLastContentReference(lastAccessedContentReference);
        courseProgressTrackerRepository.saveAndFlush(courseProgressTracker);

        return courseProgressTrackerMapper.toDTO(courseProgressTracker);
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
    private CourseProgressTrackerDTO startCourseProgressTracking(final UUID courseId, final UUID userId) {
        LOGGER.debug("Start course progress tracking for user {} in course {}.", userId, courseId);

        // create a new one
        CourseProgressTracker newTracker = new CourseProgressTracker(
            courseId, userId);
        courseProgressTrackerRepository.saveAndFlush(newTracker);
        LOGGER.debug("Created new CourseProgressTracker: {}.", newTracker);

        return courseProgressTrackerMapper.toDTO(newTracker);
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
}
