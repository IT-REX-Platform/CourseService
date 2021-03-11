package de.uni_stuttgart.it_rex.course.utils.written;

import de.uni_stuttgart.it_rex.course.domain.written.Course;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CourseUtil {

    /**
     * Creates a random entity.
     *
     * @return the entity
     */
    public static Course createCourse() {
        Course course = new Course();
        course.setName(StringUtil.generateRandomString(10, 50));
        course.setStartDate(LocalDate.now().minusDays(NumbersUtil.generateRandomInteger(20, 200)));
        course.setEndDate(LocalDate.now().plusDays(NumbersUtil.generateRandomInteger(20, 200)));
        course.setRemainActiveOffset(NumbersUtil.generateRandomInteger(33, 34563));
        course.setMaxFoodSum(NumbersUtil.generateRandomInteger(1000, 200000));
        course.setCourseDescription(StringUtil.generateRandomString(300, 600));
        course.setPublishState(EnumUtil.generateRandomPublishState());
        return course;
    }

    /**
     * Creates a random DTO.
     *
     * @return the entity
     */
    public static CourseDTO createCourseDTO() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName(StringUtil.generateRandomString(10, 50));
        courseDTO.setStartDate(LocalDate.now().minusDays(NumbersUtil.generateRandomInteger(20, 200)));
        courseDTO.setEndDate(LocalDate.now().plusDays(NumbersUtil.generateRandomInteger(20, 200)));
        courseDTO.setMaxFoodSum(NumbersUtil.generateRandomInteger(1000, 200000));
        courseDTO.setRemainActiveOffset(NumbersUtil.generateRandomInteger(33, 34563));
        courseDTO.setCourseDescription(StringUtil.generateRandomString(300, 600));
        courseDTO.setPublishState(EnumUtil.generateRandomPublishState());
        return courseDTO;
    }

    /**
     * Creates a List of random DTOs.
     *
     * @param number the length of the list
     * @return the DTOs
     */
    public static List<CourseDTO> createCourseDTOs(final int number) {
        return IntStream.range(0, number).mapToObj(i -> createCourseDTO()).collect(Collectors.toList());
    }

    /**
     * Tests if two entities are equal but ignores their id.
     *
     * @param first
     * @param second
     */
    public static void equals(final Course first, final Course second) {
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getStartDate(), second.getStartDate());
        assertEquals(first.getEndDate(), second.getEndDate());
        assertEquals(first.getMaxFoodSum(), second.getMaxFoodSum());
        assertEquals(first.getPublishState(), second.getPublishState());
        assertEquals(first.getCourseDescription(),
            second.getCourseDescription());
        assertEquals(first.getRemainActiveOffset(),
            second.getRemainActiveOffset());

        /* Todo: replace with equals methods
    assertEquals(first.getTimePeriods(), second.getTimePeriods());
    assertEquals(first.getChapters(), second.getChapters());
    assertEquals(first.getContentReferences(), second.getContentReferences());

         */
    }

    /**
     * Tests if two DTOs are equal but ignores their id.
     *
     * @param first
     * @param second
     */
    public static void equals(final CourseDTO first, final CourseDTO second) {
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getStartDate(), second.getStartDate());
        assertEquals(first.getEndDate(), second.getEndDate());
        assertEquals(first.getMaxFoodSum(), second.getMaxFoodSum());
        assertEquals(first.getPublishState(), second.getPublishState());
        assertEquals(first.getCourseDescription(),
            second.getCourseDescription());
        assertEquals(first.getRemainActiveOffset(),
            second.getRemainActiveOffset());

            /* Todo: replace with equals methods
    assertEquals(first.getTimePeriods(), second.getTimePeriods());
    assertEquals(first.getChapters(), second.getChapters());
    assertEquals(first.getContentReferences(), second.getContentReferences());

         */
    }
}
