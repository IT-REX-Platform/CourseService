package de.uni_stuttgart.it_rex.course.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.service.dto.CourseDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * todo.
 */
@RestController
@RequestMapping("/api")
public class CourseResourceExtended {

    private final Logger log =
            LoggerFactory.getLogger(CourseResourceExtended.class);

    /**
     * todo.
     */
    private final CourseServiceExtended courseServiceExtended;

    /**
     * @param courseServiceExtended todo.
     */
    @Autowired
    public CourseResourceExtended(CourseServiceExtended courseServiceExtended) {
        this.courseServiceExtended = courseServiceExtended;
    }

    /**
     * @param publishState todo.
     * @return todo.
     */
    @GetMapping("/courses/extended")
    public List<CourseDTO> getFilteredCourses(
            @RequestParam("publishState") final PUBLISHSTATE publishState) {
        log.debug("REST request to get filtered Courses");
        return courseServiceExtended.findAllFiltered(publishState);
    }
}
