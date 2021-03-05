package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.domain.written_entities.CourseProgressTracker;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseProgressTrackerRepository;
import de.uni_stuttgart.it_rex.course.security.written.RexAuthz;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseProgressTrackerDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.ChapterMapper;
import de.uni_stuttgart.it_rex.course.service.mapper.written.CourseProgressTrackerMapper;
import de.uni_stuttgart.it_rex.course.web.rest.written.ProgressResource;
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
public class ProgressService {

    /**
     * Logger.
     */
    private static final Logger LOGGER
        = LoggerFactory.getLogger(ProgressService.class);

    /**
     * Tracker Repository.
     */
    private final CourseProgressTrackerRepository trackerRepository;

    /**
     * Tracker mapper.
     */
    private final CourseProgressTrackerMapper trackerMapper;

    /**
     * Constructor.
     *
     * @param newTrackerRepository
     * @param newTrackerMapper
     */
    @Autowired
    public ProgressService(
        final CourseProgressTrackerRepository newTrackerRepository,
        final CourseProgressTrackerMapper newTrackerMapper) {
        this.trackerRepository = newTrackerRepository;
        this.trackerMapper = newTrackerMapper;
    }

    /**
     * Find a DTO for the course-wide progress of the current user.
     *
     * @param courseId Course to find the progress for
     * @return Progress DTO for the course
     */
    public CourseProgressTrackerDTO findCourseProgress(
        final UUID courseId) {

        CourseProgressTracker example = new CourseProgressTracker();
        example.setId(null);
        example.setCourseId(courseId);
        example.setUserId(RexAuthz.getUserId());

        Optional<CourseProgressTracker> optTracker =
            trackerRepository.findOne(Example.of(example));

        if (!optTracker.isPresent()) {
            // create a new one
            CourseProgressTracker newTracker = new CourseProgressTracker();
            newTracker.setCourseId(courseId);
            newTracker.setUserId(RexAuthz.getUserId());
            trackerRepository.saveAndFlush(newTracker);

            return trackerMapper.toDTO(newTracker);
        }

        return trackerMapper.toDTO(optTracker.get());
    }
}
