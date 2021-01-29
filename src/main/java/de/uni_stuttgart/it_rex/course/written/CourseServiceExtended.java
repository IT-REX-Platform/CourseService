package de.uni_stuttgart.it_rex.course.written;

import de.uni_stuttgart.it_rex.course.repository.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.dto.CourseDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.CourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * todo.
 */
@Service
@Transactional
public class CourseServiceExtended {
    private final Logger log = LoggerFactory.getLogger(CourseServiceExtended.class);

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseServiceExtended(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    /**
     *
     * @return todo.
     */
    public List<CourseDTO> findAllFiltered() {
        log.debug("Request to get filtered Courses");
        return courseRepository.findAll().stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
