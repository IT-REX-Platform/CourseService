package de.uni_stuttgart.it_rex.course.written.service;

import de.uni_stuttgart.it_rex.course.domain.Course;
import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.repository.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.dto.CourseDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.CourseMapper;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import de.uni_stuttgart.it_rex.course.written.service.mapper.CourseMapperExtended;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class contains services to be called by endpoints in
 * CourseResourceExtended.java.
 */
@Service
@Transactional
public class CourseServiceExtended {
    /**
     * Class logger.
     */
    private final Logger log =
            LoggerFactory.getLogger(CourseServiceExtended.class);

    /**
     * Course repository with all courses.
     */
    private final CourseRepository courseRepository;

    /**
     * Course mapper to map courses to DTO.
     */
    private final CourseMapper courseMapper;

    /**
     * Extended course mapper to map courses to DTO.
     */
    private final CourseMapperExtended courseMapperExtended;

    /**
     * Entity name.
     */
    private static final String ENTITY_NAME = "courseServiceExtended";

    /**
     * Constructor of CourseServiceExtended class.
     *
     * @param courseRepo      Course repository.
     * @param courseMap       Course mapper.
     * @param courseMapperExt Course mapper extended.
     */
    @Autowired
    public CourseServiceExtended(final CourseRepository courseRepo,
                                 final CourseMapper courseMap,
                                 final CourseMapperExtended courseMapperExt) {
        this.courseRepository = courseRepo;
        this.courseMapper = courseMap;
        this.courseMapperExtended = courseMapperExt;
    }

    /**
     * Update a course without overwriting it.
     *
     * @param courseDTO the entity to use to update a created entity.
     * @return the persisted entity.
     */
    public CourseDTO patch(final CourseDTO courseDTO) {
        log.debug("Request to update Course : {}", courseDTO);
        Optional<Course> oldCourse =
                courseRepository.findById(courseDTO.getId());

        if (oldCourse.isPresent()) {
            Course oldCourseEntity = oldCourse.get();
            courseMapperExtended
                    .updateCourseFromDto(courseDTO, oldCourseEntity);
            courseRepository.save(oldCourseEntity);
            return courseMapper.toDto(oldCourseEntity);
        }

        throw new BadRequestAlertException("Invalid id", ENTITY_NAME,
                "idnotfound");
    }


    /**
     * Method finds all courses and filters them by given parameters.
     *
     * @param publishState Publish state of course.
     * @return A list of courses that fit the given parameters.
     */
    public List<CourseDTO> findAllFiltered(
            final Optional<PUBLISHSTATE> publishState) {
        log.debug("Request to get filtered Courses");

        log.trace("Applying filters.");
        Course courseExample = applyFiltersToExample(publishState);

        return courseRepository
                .findAll(Example.of(courseExample))
                .stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
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
}
