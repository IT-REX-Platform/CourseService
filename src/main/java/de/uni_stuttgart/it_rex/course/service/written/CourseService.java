package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.COURSEROLE;
import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.security.written.RexAuthz;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.CourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
     * @param courseDTO the DTO to save.
     * @return the persisted entity.
     */
    public CourseDTO save(final CourseDTO courseDTO) {
        LOGGER.debug("Request to save Course : {}", courseDTO);

        Course course = courseMapper.toEntity(courseDTO);
        course = courseRepository.saveAndFlush(course);
        return courseMapper.toDTO(course);
    }

    /**
     * Creates a Course.
     * TODO: transaction handling
     *
     * @param courseDTO the Chapter
     * @return the created Course
     */
    @Transactional
    public CourseDTO create(final CourseDTO courseDTO) {
        LOGGER.debug("Request to create Course : {}", courseDTO);
        final CourseDTO storedCourse = this.save(courseDTO);

        // Add keycloak roles and groups for the course.
        for (COURSEROLE role : COURSEROLE.values()) {
            String roleName =
                RexAuthz.getCourseRoleString(storedCourse.getId(), role);
            String groupName =
                RexAuthz.getCourseGroupString(storedCourse.getId(), role);


            // Create the role rep for the new role.
            keycloakAdminService.addRole(roleName,
                String.format(
                    "Role created automatically for course %s and role %s.",
                    storedCourse.getName(), role.toString()));

            // Create the group rep for the new group.
            keycloakAdminService.addGroup(groupName);

            // Connect the roles to the group.
            keycloakAdminService.addRolesToGroup(groupName, roleName);
        }

        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        String groupName =
            RexAuthz.getCourseGroupString(storedCourse.getId(), COURSEROLE.OWNER);
        keycloakAdminService.addUserToGroup(auth.getName(), groupName);

       // CourseDTO courseDto = courseMapper.toDTO(storedCourse);
        addRoleofUserToDto(storedCourse);
        return storedCourse;
    }

    /**
     * Get all the courses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CourseDTO> findAll() {
        LOGGER.debug("Request to get all Courses");
        List<CourseDTO> courseDtos = courseMapper.toDTO(courseRepository.findAll());
        addRoleofUserToDto(courseDtos);
        return courseDtos;
    }


    /**
     * Get one course by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CourseDTO> findOne(final UUID id) {
        LOGGER.debug("Request to get Course : {}", id);

        Optional<CourseDTO> courseDto = courseMapper.toDTO(courseRepository.findById(id));
        addRoleofUserToDto(courseDto);
        return courseDto;
    }

    /**
     * Delete the course by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
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
     * @param courseDTO the DTO to use to update a created entity.
     * @return the persisted entity.
     */
    @Transactional
    public CourseDTO patch(final CourseDTO courseDTO) {
        LOGGER.debug("Request to update Course : {}", courseDTO);
        Optional<Course> courseOptional =
            courseRepository.findById(courseDTO.getId());

        if (!courseOptional.isPresent()) {
            return null;
        }

        Course course = courseOptional.get();
        courseMapper.updateCourseFromCourseDTO(courseDTO, course);
        course = courseRepository.save(course);
        return courseMapper.toDTO(course);
    }

    /**
     * Method finds all courses and filters them by given parameters.
     *
     * @param publishState Publish state of course.
     * @param activeOnly   Set true to only include active courses (current time
     *                     between course start and end date + offset).
     * @return A list of courses that fit the given parameters.
     */
    @Transactional(readOnly = true)
    public List<CourseDTO> findAll(
        final Optional<PUBLISHSTATE> publishState,
        final Optional<Boolean> activeOnly) {
        LOGGER.debug("Request to get filtered Courses");
        LOGGER.trace("Applying filters.");

        Example<Course> courseExample =
            Example.of(applyFiltersToExample(publishState));
        Specification<Course> spec =
            getSpecFromActiveAndExample(activeOnly, courseExample);
        List<Course> courses = courseRepository.findAll(spec);

        List<CourseDTO> courseDtos = courseMapper.toDTO(courses);
        addRoleofUserToDto(courseDtos);
        return courseDtos;
    }

    /**
     * Method finds all user courses and filters them by given parameters.
     * additional filters: <p>
     * - remove if {@link COURSEROLE#PARTICIPANT} && unpublished
     *
     * @param publishState Publish state of course.
     * @param activeOnly   Set true to only include active courses (current time
     *                     between course start and end date + offset).
     * @return A list of courses that fit the given parameters.
     */
    public List<CourseDTO> findUserCourses(
        final Optional<PUBLISHSTATE> publishState,
        final Optional<Boolean> activeOnly) {
        LOGGER.debug("Request to get user Courses");

        List<Course> courses;
        Example<Course> courseExample = Example.of(applyFiltersToExample(publishState));
        Specification<Course> spec = getSpecFromActiveAndExample(activeOnly, courseExample);
        courses = courseRepository.findAll(spec);

        List<CourseDTO> courseDtos = courseMapper.toDTO(courses);
        addRoleofUserToDto(courseDtos);
        courseDtos = courseDtos.stream().filter(o -> o.getCourseRole() != null).collect(Collectors.toList());
        courseDtos = courseDtos.stream().filter(
                o -> !(o.getCourseRole() == COURSEROLE.PARTICIPANT && o.getPublishState() == PUBLISHSTATE.UNPUBLISHED))
                .collect(Collectors.toList());
        return courseDtos;
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
     * Method generates a specification that describes Course objects according
     * to an example that also optionally have an endDate greater than or equal
     * to the current date.
     *
     * @param activeOnly (Optional) set endDate >= LocalDate.now()
     * @param example    Example course object
     * @return A specification describing said Course object(s).
     */
    private Specification<Course> getSpecFromActiveAndExample(
        final Optional<Boolean> activeOnly, final Example<Course> example) {
        return (root, query, builder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (activeOnly.isPresent() && activeOnly.get()) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("endDate"),
                    LocalDate.now()));
            }

            predicates.add(QueryByExamplePredicateBuilder
                .getPredicate(root, builder, example));

            return builder.and(predicates.toArray(
                new Predicate[predicates.size()]));
        };
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

    // private helper functions
    /**
     * adds the {@link COURSEROLE} of the user to a {@link CourseDTO}.
     *
     * @param courseDto the courseDto.
     */
    private void addRoleofUserToDto(CourseDTO courseDto) {
        Map<UUID, COURSEROLE> coursesAndRoles = RexAuthz.getCoursesAndRolesOfUser();
        courseDto.setCourseRole(coursesAndRoles.get(courseDto.getId()));
    }

    /**
     * adds the {@link COURSEROLE} of the user to a {@link List} of
     * {@link CourseDTO}.
     *
     * @param courseDtos the courseDtos.
     */
    private void addRoleofUserToDto(List<CourseDTO> courseDtos) {
        courseDtos.forEach(o -> addRoleofUserToDto(o));
    }

    /**
     * adds the {@link COURSEROLE} of the user to a {@link Optional} of
     * {@link CourseDTO} if presend.
     *
     * @param courseDto the courseDto.
     */
    private void addRoleofUserToDto(Optional<CourseDTO> courseDto) {
        courseDto.ifPresent(o -> addRoleofUserToDto(o));
    }
}
