package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.COURSEROLE;
import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.domain.written.Course;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.mapper.written.CourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link Course}.
 */
@Service
@Transactional
public class CourseService {

    /**
     * Logger.
     */
    private static final Logger LOGGER
        = LoggerFactory.getLogger(CourseService.class);

    /**
     * CourseRepository.
     */
    private final CourseRepository courseRepository;

    /**
     * Course mapper.
     */
    private final CourseMapper courseMapper;

    /**
     * KeycloakAdminService.
     */
    private final KeycloakAdminService keycloakAdminService;

    /**
     * Constructor.
     *
     * @param newCourseRepository     the course repository.
     * @param newCourseMapper         the course mapper.
     * @param newKeycloakAdminService the keycloakAdminService.
     */
    @Autowired
    public CourseService(final CourseRepository newCourseRepository,
                         final CourseMapper newCourseMapper,
                         final KeycloakAdminService newKeycloakAdminService) {
        this.courseRepository = newCourseRepository;
        this.courseMapper = newCourseMapper;
        this.keycloakAdminService = newKeycloakAdminService;
    }


    /**
     * Save a course.
     *
     * @param course the entity to save.
     * @return the persisted entity.
     */
    public Course save(final Course course) {
        LOGGER.debug("Request to save Course : {}", course);
        return courseRepository.save(course);
    }

    // TODO: transaction handling

    /**
     * Create a new course.
     *
     * @param course the entity representing the course that should be created
     * @return the entity.
     */
    public Course create(final Course course) {
        LOGGER.debug("Request to create Course : {}", course);

        Course newCourse = courseRepository.save(course);

        // Add keycloak roles and groups for the course.
        for (COURSEROLE role : COURSEROLE.values()) {
            String roleName =
                RexAuthz.getCourseRoleString(newCourse.getId(), role);
            String groupName =
                RexAuthz.getCourseGroupString(newCourse.getId(), role);

            // Create the role rep for the new role.
            keycloakAdminService.addRole(roleName,
                String.format(
                    "Role created automatically for course %s and role %s.",
                    newCourse.getName(), role.toString()));

            // Create the group rep for the new group.
            keycloakAdminService.addGroup(groupName);

            // Connect the roles to the group.
            keycloakAdminService.addRolesToGroup(groupName, roleName);
        }

        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        String groupName =
            RexAuthz.getCourseGroupString(newCourse.getId(), COURSEROLE.OWNER);
        keycloakAdminService.addUserToGroup(auth.getName(), groupName);

        return course;
    }

    /**
     * Get all the courses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Course> findAll() {
        LOGGER.debug("Request to get all Courses");
        return courseRepository.findAll();
    }


    /**
     * Get one course by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Course> findOne(final UUID id) {
        LOGGER.debug("Request to get Course : {}", id);
        return courseRepository.findById(id);
    }

    /**
     * Delete the course by id.
     *
     * @param id the id of the entity.
     */
    public void delete(final UUID id) {
        LOGGER.debug("Request to delete Course : {}", id);

        // Remove the keycloak roles and groups.
        for (COURSEROLE role : COURSEROLE.values()) {
            String roleName = RexAuthz.getCourseRoleString(id, role);
            String groupName = RexAuthz.getCourseGroupString(id, role);

            keycloakAdminService.removeRole(roleName);
            keycloakAdminService.removeGroup(groupName);
        }

        courseRepository.deleteById(id);
    }

    /**
     * Update a course without overwriting it.
     *
     * @param course the entity to use to update a created entity.
     * @return the persisted entity.
     */
    public Course patch(final Course course) {
        LOGGER.debug("Request to update Course : {}", course);
        Optional<Course> oldCourse =
            courseRepository.findById(course.getId());

        if (oldCourse.isPresent()) {
            Course oldCourseEntity = oldCourse.get();
            courseMapper.updateCourseFromCourse(course, oldCourseEntity);
            return courseRepository.save(oldCourseEntity);
        }
        return null;
    }

    /**
     * Method finds all courses and filters them by given parameters.
     *
     * @param publishState Publish state of course.
     * @return A list of courses that fit the given parameters.
     */
    public List<Course> findAll(
        final Optional<PUBLISHSTATE> publishState) {
        LOGGER.debug("Request to get filtered Courses");

        LOGGER.trace("Applying filters.");
        Course courseExample = applyFiltersToExample(publishState);

        return courseRepository.findAll(Example.of(courseExample));
    }

    /**
     * Method applies filters to an example instance of course,
     * which is used for running a search over all courses.
     * <p>
     * More filters should be added here. @s.pastuchov 30.01.21
     *
     * @param publishState Filter publish state.
     * @return Example course with applied filters for search.
     */
    private Course applyFiltersToExample(
        final Optional<PUBLISHSTATE> publishState) {
        Course course = new Course();
        publishState.ifPresent(course::setPublishState);
        return course;
    }

    /**
     * Join a course.
     *
     * @param id of the course to join.
     */
    public void join(final UUID id) {
        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        String groupName =
            RexAuthz.getCourseGroupString(id, COURSEROLE.PARTICIPANT);
        keycloakAdminService.addUserToGroup(auth.getName(), groupName);
    }

    /**
     * Leave a course.
     *
     * @param id of the course to leave.
     */
    public void leave(final UUID id) {
        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        String groupName =
            RexAuthz.getCourseGroupString(id, COURSEROLE.PARTICIPANT);
        keycloakAdminService.removeUserFromGroup(auth.getName(), groupName);
    }

}
