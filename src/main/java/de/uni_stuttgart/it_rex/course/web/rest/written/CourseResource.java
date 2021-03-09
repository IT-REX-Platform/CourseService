package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseDTO;
import de.uni_stuttgart.it_rex.course.service.written.CourseService;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing {@link CourseDTO}.
 */
@RestController
@RequestMapping("/api")
public class CourseResource {
    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(CourseResource.class);

    /**
     * Entity name.
     */
    private static final String ENTITY_NAME = "Course";

    /**
     * Application name.
     */
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * Used course service.
     */
    private final CourseService courseService;

    /**
     * Constructor.
     *
     * @param newCourseService the course service.
     */
    public CourseResource(final CourseService newCourseService) {
        this.courseService = newCourseService;
    }

    /**
     * {@code GET  /courses/:id} : get the "id" course.
     *
     * @param id the id of the courseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
     * and with body the courseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable final UUID id) {
        log.debug("REST request to get Course : {}", id);
        Optional<CourseDTO> courseDTO = courseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseDTO);
    }

    /**
     * {@code GET  /courses} : get all the courses.
     * Filters them by the publish state if it exists.
     * Optionally only select active courses.
     *
     * @param publishState Publish state of course.
     * @param activeOnly Set true to only include active courses (current time
     *                   between course start and end date + offset).
     * @return A list of courses that fit the given parameters.
     */
    @GetMapping("/courses")
    public List<CourseDTO> getAllCourses(
        @RequestParam("publishState") final Optional<PUBLISHSTATE>
            publishState,
        @RequestParam("activeOnly") final Optional<Boolean> activeOnly) {
        log.debug("REST request to get filtered Courses");
        return courseService.findAll(publishState, activeOnly);
    }

    /**
     * {@code POST  /courses} : Create a new course.
     *
     * @param courseDTO the course to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)}
     * and with body the new course, or with status {@code 400 (Bad Request)}
     * if the course has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/courses")
    public ResponseEntity<CourseDTO> createCourse(
        @RequestBody final CourseDTO courseDTO)
        throws URISyntaxException {
        log.debug("REST request to save Course : {}", courseDTO);
        if (courseDTO.getId() != null) {
            throw new BadRequestAlertException("A new courseDTO cannot already "
                + "have an ID", ENTITY_NAME, "idexists");
        }

        CourseDTO result = courseService.create(courseDTO);
        return ResponseEntity
            .created(new URI("/api/courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName,
                true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course} : Updates an existing course.
     *
     * @param courseDTO the course to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     * body the updated course,
     * or with status {@code 400 (Bad Request)} if the course is not valid,
     * or with status {@code 500 (Internal Server Error)} if the course couldn't
     * be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/courses")
    public ResponseEntity<CourseDTO> updateCourse(
        @RequestBody final CourseDTO courseDTO)
        throws URISyntaxException {
        log.debug("REST request to update Course : {}", courseDTO);
        if (courseDTO.getId() == null) {
            throw new BadRequestAlertException(
                "Invalid id", ENTITY_NAME, "idnull");
        }
        CourseDTO result = courseService.save(courseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName,
                true, ENTITY_NAME,
                courseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /courses} : Patches an existing course.
     *
     * @param courseDTO the course to patch.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     * body the updated course,
     * or with status {@code 400 (Bad Request)} if the course is not valid,
     * or with status {@code 500 (Internal Server Error)} if the course
     * couldn't be patched.
     */
    @PatchMapping("/courses")
    public ResponseEntity<CourseDTO> patchCourse(
        @RequestBody final CourseDTO courseDTO) {
        log.debug("REST request to patch Course : {}", courseDTO);
        if (courseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME,
                "idnull");
        }
        final CourseDTO result = courseService.patch(courseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName,
                true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code DELETE  /courses/:id} : delete the "id" course.
     *
     * @param id the id of the courseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable final UUID id) {
        log.debug("REST request to delete Course : {}", id);
        courseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil
            .createEntityDeletionAlert(applicationName, true,
                ENTITY_NAME,
                id.toString())).build();
    }

    /**
     * Makes the currently logged in user join a course by id.
     *
     * @param id the id of the course to join.
     * @return an OK request for now.
     */
    @PostMapping("/courses/{id}/join")
    public ResponseEntity<Void> joinCourse(@PathVariable final UUID id) {
        log.debug("REST request to join a course: {}", id);

        courseService.join(id);

        return ResponseEntity.ok().build();
    }

    /**
     * Makes the currently logged in user leave a course by id.
     *
     * @param id the id of the course to leave.
     * @return an OK request for now.
     */
    @PostMapping("/courses/{id}/leave")
    public ResponseEntity<Void> leaveCourse(@PathVariable final UUID id) {
        log.debug("REST request to leave a course: {}", id);

        courseService.leave(id);

        return ResponseEntity.ok().build();
    }
}
