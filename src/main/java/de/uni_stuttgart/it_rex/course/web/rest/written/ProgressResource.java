package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.security.written.RexAuthz;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseProgressTrackerDTO;
import de.uni_stuttgart.it_rex.course.service.written.CourseService;
import de.uni_stuttgart.it_rex.course.service.written.ProgressTrackingService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing {@link ChapterDTO}.
 */
@RestController
@RequestMapping("/api/progress")
public class ProgressResource {

    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(ChapterResource.class);

    /**
     * Entity name.
     */
    private static final String ENTITY_NAME_COURSEPT = "CourseProgressTracker";

    /**
     * Application name.
     */
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * Used progress service.
     */
    private final ProgressTrackingService progressTrackingService;

    /**
     * Used course service.
     */
    private final CourseService courseService;

    /**
     * Constructor.
     *
     * @param newProgressTrackingService the progress service.
     * @param newCourseService the course service.
     */
    public ProgressResource(final ProgressTrackingService newProgressTrackingService,
                            final CourseService newCourseService) {
        this.progressTrackingService = newProgressTrackingService;
        this.courseService = newCourseService;
    }

    /**
     * {@code GET  /courses/:courseId} : get the progress for course "courseId" for the requesting user.
     *
     * @param courseId the id of the course to retrieve progress for.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
     * and with body the chapter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<CourseProgressTrackerDTO> getCourseProgress(
        @PathVariable final UUID courseId) {
        log.debug("REST request to get Course Progress for Course : {}",
            courseId);

        // check that a course with the given id exists
        // might want to compare against the list of the user's courses
        // instead of asking the courseservice like this
        if (courseService.findOne(courseId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UUID userId = RexAuthz.getUserId();
        CourseProgressTrackerDTO progress =
            progressTrackingService.findCourseProgress(courseId, userId);
        return ResponseUtil.wrapOrNotFound(Optional.of(progress));
    }
}
