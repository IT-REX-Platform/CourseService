package de.uni_stuttgart.it_rex.course.written.web.rest;

import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.service.dto.CourseDTO;
import de.uni_stuttgart.it_rex.course.web.rest.CourseResource;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import de.uni_stuttgart.it_rex.course.written.KeycloakCommunicator;
import de.uni_stuttgart.it_rex.course.written.service.CourseServiceExtended;
import io.github.jhipster.web.util.HeaderUtil;

import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.core.Response;

/**
 * Class contains course endpoints in addition to those found in
 * CourseResource.java.
 */
@RestController
@RequestMapping("/api/extended")
public class CourseResourceExtended {
    /**
     * The role a course participant can have.
     */
    public enum CourseRole {
        OWNER,
        MANAGER,
        PARTICIPANT
    }

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
     * The Keycloak communicator used for Service-to-Service communication.
     */
    private final KeycloakCommunicator keycloakCommunicator;

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
        this.keycloakCommunicator = new KeycloakCommunicator();
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
        ResponseEntity<CourseDTO> createdCourse = courseResource.createCourse(courseDTO);

        // If the creation was successful, also create roles and groups for the course.
        // TODO: Should cancel creation of course if Keycloak has an error. (see next TODO)
        // TODO: Move this one "layer" lower (inside service?).
        if (createdCourse.getStatusCode() == HttpStatus.CREATED) {
            for (CourseRole role : CourseRole.values()) {
                String roleName = KeycloakCommunicator.makeNameForCourse(
                    KeycloakCommunicator.ROLE_COURSE_TEMPLATE, createdCourse.getBody().getId(), role);
                String groupName = KeycloakCommunicator.makeNameForCourse(
                    KeycloakCommunicator.GROUP_COURSE_TEMPLATE, createdCourse.getBody().getId(), role);

                // Create the role rep for the new role.
                keycloakCommunicator.addRole(roleName,
                    String.format("Role created automatically for course %s and role %s.",
                        courseDTO.getName(), role.toString()));

                // Create the group rep for the new group.
                keycloakCommunicator.addGroup(groupName);

                // Connect the roles to the group.
                keycloakCommunicator.addRolesToGroup(groupName, roleName);
            }
        }

        return createdCourse;
    }

    /**
     * {@code PATCH  /courses} : Patches an existing course.
     *
     * @param courseDTO the courseDTO to patch.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     * body the updated courseDTO,
     * or with status {@code 400 (Bad Request)} if the courseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseDTO
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
        CourseDTO result = courseServiceExtended.patch(courseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil
                .createEntityUpdateAlert(applicationName, true,
                    ENTITY_NAME, courseDTO.getId().toString()))
            .body(result);
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
        @RequestBody final CourseDTO courseDTO)
        throws URISyntaxException {
        return courseResource.updateCourse(courseDTO);
    }

    /**
     * @param publishState Publish state of course.
     * @return A list of courses that fit the given parameters.
     */
    @GetMapping("/courses")
    public List<CourseDTO> getFilteredCourses(
        @RequestParam("publishState") final Optional<PUBLISHSTATE>
            publishState) {
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
        ResponseEntity<Void> deletedCourseResp = courseResource.deleteCourse(id);

        // TODO: Check if deletion was successful.
        // TODO: Should cancel deletion of course if Keycloak has an error.
        if (deletedCourseResp.getStatusCode() == HttpStatus.NO_CONTENT) {
            for (CourseRole role : CourseRole.values()) {
                String roleName = KeycloakCommunicator.makeNameForCourse(
                    KeycloakCommunicator.ROLE_COURSE_TEMPLATE, id, role);
                String groupName = KeycloakCommunicator.makeNameForCourse(
                    KeycloakCommunicator.GROUP_COURSE_TEMPLATE, id, role);

                keycloakCommunicator.removeRole(roleName);
                keycloakCommunicator.removeGroup(groupName);
            }
        }

        return deletedCourseResp;
    }
}
