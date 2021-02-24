package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.domain.written.Course;
import de.uni_stuttgart.it_rex.course.service.written.CourseService;
import de.uni_stuttgart.it_rex.course.service.written.KeycloakAdminService;
import de.uni_stuttgart.it_rex.course.service.written.RexAuthz;
import de.uni_stuttgart.it_rex.course.service.written.RexAuthz.CourseRole;
import de.uni_stuttgart.it_rex.course.service.written.RexAuthz.RexAuthzConstants;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
 * REST controller for managing {@link Course}.
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
     * The KeycloakAdminService used for Service-to-Service communication.
     */
    private final KeycloakAdminService keycloakAdminService;

    /**
     * Constructor.
     *
     * @param newCourseService the course service.
     */
    public CourseResource(final CourseService newCourseService, final KeycloakAdminService newKeycloakAdminService) {
        this.courseService = newCourseService;
        this.keycloakAdminService = newKeycloakAdminService;
    }

    /**
     * {@code POST  /courses} : Create a new course.
     *
     * @param course the course to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)}
     * and with body the new course, or with status {@code 400 (Bad Request)}
     * if the course has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody final Course course)
        throws URISyntaxException {
        log.debug("REST request to save Course : {}", course);
        if (course.getId() != null) {
            throw new BadRequestAlertException("A new course cannot already "
                + "have an ID", ENTITY_NAME, "idexists");
        }

        Course result = courseService.save(course);

        // Add keycloak roles and groups for the course.
        for (CourseRole role : CourseRole.values()) {
            String roleName = RexAuthz.makeNameForCourse(RexAuthzConstants.TEMPLATE_COURSE_ROLE, result.getId(), role);
            String groupName = RexAuthz.makeNameForCourse(RexAuthzConstants.TEMPLATE_COURSE_GROUP, result.getId(),
                    role);

            // Create the role rep for the new role.
            keycloakAdminService.addRole(roleName,
                String.format("Role created automatically for course %s and role %s.", result.getName(), role.toString()));

            // Create the group rep for the new group.
            keycloakAdminService.addGroup(groupName);

            // Connect the roles to the group.
            keycloakAdminService.addRolesToGroup(groupName, roleName);
        }

        // TODO: Make the current user join the course.

        return ResponseEntity.created(new URI("/api/courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName,
                true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /courses} : Updates an existing course.
     *
     * @param course the course to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     * body the updated course,
     * or with status {@code 400 (Bad Request)} if the course is not valid,
     * or with status {@code 500 (Internal Server Error)} if the course couldn't
     * be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/courses")
    public ResponseEntity<Course> updateCourse(@RequestBody final Course course)
        throws URISyntaxException {
        log.debug("REST request to update Course : {}", course);
        if (course.getId() == null) {
            throw new BadRequestAlertException(
                "Invalid id", ENTITY_NAME, "idnull");
        }
        Course result = courseService.save(course);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName,
                true, ENTITY_NAME, course.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /courses/:id} : get the "id" course.
     *
     * @param id the id of the courseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
     * and with body the courseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable final UUID id) {
        log.debug("REST request to get Course : {}", id);
        Optional<Course> course = courseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(course);
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

        // Remove the keycloak roles and groups.
        for (CourseRole role : CourseRole.values()) {
            String roleName = RexAuthz.makeNameForCourse(RexAuthzConstants.TEMPLATE_COURSE_ROLE, id, role);
            String groupName = RexAuthz.makeNameForCourse(RexAuthzConstants.TEMPLATE_COURSE_GROUP, id, role);

            keycloakAdminService.removeRole(roleName);
            keycloakAdminService.removeGroup(groupName);
        }

        return ResponseEntity.noContent().headers(HeaderUtil
            .createEntityDeletionAlert(applicationName, true, ENTITY_NAME,
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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // -- If needed, a Optional<CourseRole> role can be added.
        // CourseRole roleToJoin = role.orElse(CourseRole.PARTICIPANT);
        String groupName = KeycloakCommunicator.makeNameForCourse(
            KeycloakCommunicator.GROUP_COURSE_TEMPLATE, id, CourseRole.PARTICIPANT);

        // TODO: Check if joining actually worked out.
        keycloakCommunicator.addUserToGroup(auth.getName(), groupName);

        return ResponseEntity.ok()
            .build();
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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // TODO: Currently just tries removing the user from every group.
        for (CourseRole curRole : CourseRole.values()) {
            String groupName = KeycloakCommunicator.makeNameForCourse(
                KeycloakCommunicator.GROUP_COURSE_TEMPLATE, id, curRole);
            keycloakCommunicator.removeUserFromGroup(auth.getName(), groupName);
        }

        return ResponseEntity.ok()
            .build();
    }

    /**
     * {@code PATCH  /courses} : Patches an existing course.
     *
     * @param course the course to patch.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     * body the updated course,
     * or with status {@code 400 (Bad Request)} if the course is not valid,
     * or with status {@code 500 (Internal Server Error)} if the course
     * couldn't be patched.
     */
    @PatchMapping("/courses")
    public ResponseEntity<Course> patchCourse(
        @RequestBody final Course course) {
        log.debug("REST request to patch Course : {}", course);
        if (course.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME,
                "idnull");
        }
        Course result = courseService.patch(course);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName,
                true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /courses} : get all the courses.
     * Filters them by the publish state if it exists.
     *
     * @param publishState Publish state of course.
     * @return A list of courses that fit the given parameters.
     */
    @GetMapping("/courses")
    public List<Course> getAllCourses(
        @RequestParam("publishState") final Optional<PUBLISHSTATE>
            publishState) {
        log.debug("REST request to get filtered Courses");
        return courseService.findAll(publishState);
    }
}
