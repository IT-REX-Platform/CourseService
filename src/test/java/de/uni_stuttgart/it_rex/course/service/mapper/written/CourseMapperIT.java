package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseDTO;
import de.uni_stuttgart.it_rex.course.utils.written.CourseUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = { CourseServiceApp.class, TestSecurityConfiguration.class })
class CourseMapperIT {
    private static final UUID UPDATE_ID = UUID.randomUUID();
    private static final int MAX_FOOD_SUM = 42;

    @Autowired
    private CourseMapper courseMapper;

    @Test
    void updateCourseFromCourseDTO() {
        Course toUpdate = CourseUtil.createCourse();
        CourseDTO update = new CourseDTO();

        update.setId(UPDATE_ID);
        update.setName(null);
        update.setMaxFoodSum(MAX_FOOD_SUM);

        Course expected = new Course();
        expected.setId(update.getId());
        expected.setName(toUpdate.getName());
        expected.setMaxFoodSum(update.getMaxFoodSum());
        expected.setCourseDescription(toUpdate.getCourseDescription());
        expected.setRemainActiveOffset(toUpdate.getRemainActiveOffset());
        expected.setPublishState(toUpdate.getPublishState());
        expected.setStartDate(toUpdate.getStartDate());
        expected.setEndDate(toUpdate.getEndDate());

        courseMapper.updateCourseFromCourseDTO(update, toUpdate);

        assertEquals(expected, toUpdate);
    }

    @Test
    void toDTO() {
        Course course = CourseUtil.createCourse();
        CourseDTO expected = new CourseDTO();
        expected.setId(course.getId());
        expected.setName(course.getName());
        expected.setMaxFoodSum(course.getMaxFoodSum());
        expected.setCourseDescription(course.getCourseDescription());
        expected.setRemainActiveOffset(course.getRemainActiveOffset());
        expected.setPublishState(course.getPublishState());
        expected.setStartDate(course.getStartDate());
        expected.setEndDate(course.getEndDate());

        CourseDTO result = courseMapper.toDTO(course);
        assertEquals(expected, result);
    }

    @Test
    void toEntity() {
        CourseDTO courseDTO = CourseUtil.createCourseDTO();
        Course expected = new Course();
        expected.setId(courseDTO.getId());
        expected.setName(courseDTO.getName());
        expected.setMaxFoodSum(courseDTO.getMaxFoodSum());
        expected.setCourseDescription(courseDTO.getCourseDescription());
        expected.setRemainActiveOffset(courseDTO.getRemainActiveOffset());
        expected.setPublishState(courseDTO.getPublishState());
        expected.setStartDate(courseDTO.getStartDate());
        expected.setEndDate(courseDTO.getEndDate());

        Course result = courseMapper.toEntity(courseDTO);
        CourseUtil.equals(expected, result);
    }
}
