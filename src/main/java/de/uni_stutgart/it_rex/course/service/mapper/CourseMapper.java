package de.uni_stutgart.it_rex.course.service.mapper;


import de.uni_stutgart.it_rex.course.domain.*;
import de.uni_stutgart.it_rex.course.service.dto.CourseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {


    @Mapping(target = "participations", ignore = true)
    @Mapping(target = "removeParticipation", ignore = true)
    Course toEntity(CourseDTO courseDTO);

    default Course fromId(Long id) {
        if (id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }
}
