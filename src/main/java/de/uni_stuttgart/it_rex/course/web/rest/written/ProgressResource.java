package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseProgressTrackerDTO;
import de.uni_stuttgart.it_rex.course.service.written.ProgressService;
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
@RequestMapping("/api")
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
    private final ProgressService progressService;

    /**
     * Constructor.
     *
     * @param newProgressService the progress service.
     */
    public ProgressResource(final ProgressService newProgressService) {
        this.progressService = newProgressService;
    }

    /**
     * {@code GET  /progress/course/:id} : get the progress for course "id".
     *
     * @param id the id of the course to retrieve progress for.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
     * and with body the chapter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/progress/course/{id}")
    public ResponseEntity<CourseProgressTrackerDTO> getCourseProgress(
        @PathVariable final UUID id) {
        log.debug("REST request to get Course Progress for Course : {}", id);
        CourseProgressTrackerDTO progress =
            progressService.findCourseProgress(id);
        return ResponseUtil.wrapOrNotFound(Optional.of(progress));
    }
}
