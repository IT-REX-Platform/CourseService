package de.uni_stuttgart.it_rex.course.written;

import de.uni_stuttgart.it_rex.course.service.dto.CourseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * todo.
 */
@RestController
@RequestMapping("/api")
public class CourseResourceExtended {

    private final Logger log = LoggerFactory.getLogger(CourseResourceExtended.class);

    /**
     * todo.
     */
    private final CourseServiceExtended courseServiceExtended;

    /**
     *
     * @param courseServiceExtended todo.
     */
    @Autowired
    public CourseResourceExtended(CourseServiceExtended courseServiceExtended) {
        this.courseServiceExtended = courseServiceExtended;
    }

    /**
     *
     * @return todo.
     */
    @GetMapping("/courses/extended")
    public List<CourseDTO> getFilteredCourses() {
        log.debug("REST request to get filtered Courses");
        return courseServiceExtended.findAllFiltered();
    }
}
