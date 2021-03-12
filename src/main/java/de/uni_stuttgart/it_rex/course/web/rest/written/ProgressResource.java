package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.security.written.RexAuthz;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentProgressTrackerDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseProgressTrackerDTO;
import de.uni_stuttgart.it_rex.course.service.written.CourseService;
import de.uni_stuttgart.it_rex.course.service.written.ProgressTrackingService;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    /**
     * {@code POST  /content/ : post a new content tracker for the content Item of request user}
     *
     * @param contentReferenceDTO DTO of Content Item Reference
     * @return the {@link ResponseEntity} with status {@code 201 (Created)}
     *    * and with body of the new content progress tracker, or with status {@code 400 (Bad Request)}
     *    * if the content progress tracker has already an ID.
     */
    @PostMapping("/content/")
    public ResponseEntity<ContentProgressTrackerDTO> createContentProgress(
        @RequestBody ContentReferenceDTO contentReferenceDTO
    ) {
        log.debug("REST request to post Content Progress for Content Item : {}", contentReferenceDTO.getContentId());

        //if (contentReferenceDTO.getId() == null) {
        //    throw new BadRequestAlertException(
        //        "Invalid id", ENTITY_NAME_COURSEPT, "idnull");

        UUID userId = RexAuthz.getUserId();
        ContentProgressTrackerDTO result =
            ProgressTrackingService.startContentProgressTracking(contentReferenceDTO.getId(), userId);

        return ResponseEntity.created(new URI("/api/progress/content/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName,
                true, ENTITY_NAME_COURSEPT, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /content/{trackerId}/progress : update the progress of a content item for a specific user}
     *
     * @param trackerId of the content item
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *    * body of the update content tracker,
     *    * or with status {@code 400 (Bad Request)} if the content tracker is not valid,
     *    * or with status {@code 500 (Internal Server Error)} if the content tracker couldn't
     *    * be updated.
     */
    @PutMapping("/content/{trackerId}/progress")
    public ResponseEntity<ContentProgressTrackerDTO> updateContentProgress(
        @PathVariable final UUID trackerId) {
        log.debug("REST request to put Content Progress for Content Item : {}",
            trackerId);

        UUID userId = RexAuthz.getUserId();
        ContentProgressTrackerDTO progress =
            progressTrackingService.findContentProgress(trackerId, userId);

        progressTrackingService.updateContentProgress(progress);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName,
                true, ENTITY_NAME_COURSEPT, progress.getId().toString()))
            .body(progress);
    }

    /**
     * {@code PUT  /content/{trackerId}/complete : set state of a content item of a specific user on complete}
     *
     * @param trackerId id of the content tracker
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     *    * body of the updated content tracker,
     *    * or with status {@code 400 (Bad Request)} if the content tracker is not valid,
     *    * or with status {@code 500 (Internal Server Error)} if the content tracker couldn't
     *    * be updated.
     */
    @PutMapping("/content/{trackerId}/complete")
    public ResponseEntity<ContentProgressTrackerDTO> setContentStateComplete(
        @PathVariable final UUID trackerId) {
        log.debug("REST request to put Content State on Complete for Content Item : {}",
            trackerId);

        UUID userId = RexAuthz.getUserId();
        ContentProgressTrackerDTO state =
            progressTrackingService.findContentProgress(trackerId, userId);

        progressTrackingService.setContentStateAsComplete(state);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName,
                true, ENTITY_NAME_COURSEPT, state.getId().toString()))
            .body(state);
    }
}
