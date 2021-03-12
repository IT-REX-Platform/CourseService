package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.domain.written.ContentProgressTracker;
import de.uni_stuttgart.it_rex.course.domain.written.CourseProgressTracker;
import de.uni_stuttgart.it_rex.course.security.written.RexAuthz;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentProgressTrackerDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseProgressTrackerDTO;
import de.uni_stuttgart.it_rex.course.service.written.CourseService;
import de.uni_stuttgart.it_rex.course.service.written.ProgressTrackingService;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
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
    private final Logger log = LoggerFactory.getLogger(ProgressResource.class);

    /**
     * Entity name for the @{@link CourseProgressTracker}.
     */
    private static final String ENTITY_NAME_COURSEPT = "CourseProgressTracker";

    /**
     * Entity name for the @{@link ContentProgressTracker}.
     */
    private static final String ENTITY_NAME_CONTENTPT = "ContentProgressTracker";

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
            progressTrackingService.findOrCreateCourseProgressTracker(courseId, userId);
        return ResponseUtil.wrapOrNotFound(Optional.of(progress));
    }

    /**
     * {@code PUT  /courses/{courseTrackerId}/contentReference : set the last accessed content reference for a specific user}
     *
     * @param courseTrackerId the user's tracker to update
     * @param contentReferenceDTO the contentReference to store as last accessed
     * @return {@link ResponseEntity} with status {@code 200 (OK)} and the updated
     * {@link CourseProgressTrackerDTO} in its body, or with {@code 404 (Not Found)}
     * if there is not tracker with the given Id or the contentReferenceDTO was invalid.
     */
    @PutMapping("/courses/{courseTrackerId}/contentReference")
    public ResponseEntity<CourseProgressTrackerDTO> updateLastAccessedContentReference(
        @PathVariable final UUID courseTrackerId,
        @RequestBody final ContentReferenceDTO contentReferenceDTO){
        CourseProgressTrackerDTO result = progressTrackingService.updateLastAccessedContentReference(courseTrackerId,contentReferenceDTO);

        return ResponseEntity.ok().
            headers(HeaderUtil.createEntityUpdateAlert(
                applicationName,true, ENTITY_NAME_COURSEPT, result.getId().toString())).
            body(result);
    }

    /**
     * {@code GET  /content/:trackerId} : Get Content Progress Tracker for Content Item for User
     *
     * @param trackerId id of Content Progress Tracker
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
     * and with the {@link ContentProgressTrackerDTO} in the body,
     * or with status {@code 404 (Not Found)} in case of an invalid id.
     */
    @GetMapping("/content/{trackerId}")
    public ResponseEntity<ContentProgressTrackerDTO> getContentProgress(
        @PathVariable final UUID trackerId){
        log.debug("REST request to get Content Progress for Content Item : {}", trackerId);
        Optional <ContentProgressTrackerDTO> contentProgressTrackerDTO
            = progressTrackingService.findContentProgressTracker(trackerId);
        return ResponseUtil.wrapOrNotFound(contentProgressTrackerDTO);
    }

    /**
     * {@code POST  /content/ : post a new content tracker for the content Item of request user}
     *
     * @param contentReferenceDTO DTO of Content Item Reference
     * @return the {@link ResponseEntity} with status {@code 201 (Created)}
     *    * and with body of the new content progress tracker, or with status {@code 400 (Bad Request)}
     *    * if the content progress tracker has already an ID.
     */
    @PostMapping("/content")
    public ResponseEntity<ContentProgressTrackerDTO> createContentProgress(
        @RequestBody ContentReferenceDTO contentReferenceDTO,
        @RequestParam UUID courseTrackerId
    ) throws URISyntaxException {
        log.debug("REST request to post Content Progress for Content Item : {}", contentReferenceDTO.getContentId());

        //if (contentReferenceDTO.getId() == null) {
        //    throw new BadRequestAlertException(
        //        "Invalid id", ENTITY_NAME_COURSEPT, "idnull");

        UUID userId = RexAuthz.getUserId();
        ContentProgressTrackerDTO result =
            progressTrackingService.startContentProgressTracking(contentReferenceDTO, userId,
                courseTrackerId);

        return ResponseEntity.created(new URI("/api/progress/content/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName,
                true, ENTITY_NAME_CONTENTPT, result.getId().toString()))
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
        @PathVariable final UUID trackerId,
        @RequestParam final float progress) {
        log.debug("REST request to put Content Progress for Content Item : {}",
            trackerId);

        ContentProgressTrackerDTO contentProgressTrackerDTO =
            progressTrackingService.updateContentProgress(trackerId, progress);


        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName,
                true, ENTITY_NAME_CONTENTPT, contentProgressTrackerDTO.getId().toString()))
            .body(contentProgressTrackerDTO);
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

        ContentProgressTrackerDTO contentProgressTrackerDTO = progressTrackingService.completeContentProgressTracker(trackerId);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName,
                true, ENTITY_NAME_CONTENTPT, contentProgressTrackerDTO.getId().toString()))
            .body(contentProgressTrackerDTO);
    }
}
