package de.uni_stuttgart.it_rex.course.written.service.mapper;

import de.uni_stuttgart.it_rex.course.domain.Course;
import de.uni_stuttgart.it_rex.course.service.dto.CourseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CourseMapperExtended {
    /**
     * Method for updating course parameters without overwriting the whole
     * course DB entry.
     *
     * @param courseDTO Course DTO with parameters to update.
     * @param course    Course object to be used to update DB course entry.
     */
    @BeanMapping(nullValuePropertyMappingStrategy
            = NullValuePropertyMappingStrategy.IGNORE)
    void updateCourseFromDto(CourseDTO courseDTO, @MappingTarget Course course);
}
