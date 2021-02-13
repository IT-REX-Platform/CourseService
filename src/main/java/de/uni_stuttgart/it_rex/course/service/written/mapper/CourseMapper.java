package de.uni_stuttgart.it_rex.course.service.written.mapper;


import de.uni_stuttgart.it_rex.course.domain.written.Course;
import de.uni_stuttgart.it_rex.course.service.mapper.EntityMapper;
import de.uni_stuttgart.it_rex.course.service.written.dto.CourseDTO;

import org.mapstruct.*;

import java.util.UUID;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {


    @Mapping(target = "participations", ignore = true)
    @Mapping(target = "removeParticipation", ignore = true)
    Course toEntity(CourseDTO courseDTO);

    default Course fromId(final UUID id) {
        if (id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }
}
