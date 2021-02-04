package de.uni_stuttgart.it_rex.course.written.web.rest;

import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.service.dto.CourseDTO;

import de.uni_stuttgart.it_rex.course.web.rest.CourseResource;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import de.uni_stuttgart.it_rex.course.written.service.CourseServiceExtended;
import io.github.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * Class contains course endpoints in addition to those found in
 * CourseResource.java.
 */
@RestController
@RequestMapping("/api/extended")
public class CourseResourceExtended {
    /**
     * Class logger.
     */
    private final Logger log =
            LoggerFactory.getLogger(CourseResourceExtended.class);

    /**
     * Entity name.
     */
    private static final String ENTITY_NAME = "courseServiceCourseExtended";

    /**
     * Application name.
     */
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * Course resource.
     */
    private final CourseResource courseResource;

    /**
     * Course service extended.
     */
    private final CourseServiceExtended courseServiceExtended;

    /**
     * Constructor.
     *
     * @param courseServiceExt Instance of course service extended.
     * @param courseRes        Instance of course resource.
     */
    @Autowired
    public CourseResourceExtended(
            final CourseServiceExtended courseServiceExt,
            final CourseResource courseRes
    ) {
        this.courseServiceExtended = courseServiceExt;
        this.courseResource = courseRes;
    }

    /**
     * {@code POST  /courses} : Create a new course.
     *
     * @param courseDTO the courseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)}
     * and with body the new courseDTO, or with status {@code 400 (Bad Request)}
     * if the course has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/courses")
    public ResponseEntity<CourseDTO> createCourse(
            @RequestBody final CourseDTO courseDTO) throws URISyntaxException {
        return courseResource.createCourse(courseDTO);
    }

    /**
     * {@code PUT  /courses} : Updates an existing course.
     *
     * @param courseDTO the courseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     * body the updated courseDTO,
     * or with status {@code 400 (Bad Request)} if the courseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseDTO
     * couldn't be updated.
     */
    @PutMapping("/courses")
    public ResponseEntity<CourseDTO> updateCourse(
            @RequestBody final CourseDTO courseDTO) {
        log.debug("REST request to update Course : {}", courseDTO);
        if (courseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME,
                    "idnull");
        }
        CourseDTO result = courseServiceExtended.update(courseDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil
                        .createEntityUpdateAlert(applicationName, true,
                                ENTITY_NAME, courseDTO.getId().toString()))
                .body(result);
    }

    /**
     * @param publishState Publish state of course.
     * @return A list of courses that fit the given parameters.
     */
    @GetMapping("/courses")
    public List<CourseDTO> getFilteredCourses(
            @RequestParam("publishState")
            final Optional<PUBLISHSTATE> publishState) {
        log.debug("REST request to get filtered Courses");
        return courseServiceExtended.findAllFiltered(publishState);
    }

    /**
     * {@code GET  /courses/:id} : get the "id" course.
     *
     * @param id the id of the courseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     * body the courseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable final Long id) {
        return courseResource.getCourse(id);
    }

    /**
     * {@code DELETE  /courses/:id} : delete the "id" course.
     *
     * @param id the id of the courseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable final Long id) {
        return courseResource.deleteCourse(id);
    }
}
