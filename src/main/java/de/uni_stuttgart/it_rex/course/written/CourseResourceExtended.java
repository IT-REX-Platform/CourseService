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
import java.util.Optional;

/**
 * Class contains course endpoints in addition to those found in
 * CourseResource.java.
 */
@RestController
@RequestMapping("/api")
public class CourseResourceExtended {
    /**
     * Class logger.
     */
    private final Logger log =
            LoggerFactory.getLogger(CourseResourceExtended.class);

    /**
     * Course service extended.
     */
    private final CourseServiceExtended courseServiceExtended;

    /**
     * @param courseServiceExt Instance of course service extended.
     */
    @Autowired
    public CourseResourceExtended(
            final CourseServiceExtended courseServiceExt) {
        this.courseServiceExtended = courseServiceExt;
    }

    /**
     * @param publishState Publish state of course.
     * @return A list of courses that fit the given parameters.
     */
    @GetMapping("/courses/extended")
    public List<CourseDTO> getFilteredCourses(
            @RequestParam("publishState")
            final Optional<PUBLISHSTATE> publishState) {
        log.debug("REST request to get filtered Courses");
        return courseServiceExtended.findAllFiltered(publishState);
    }
}
