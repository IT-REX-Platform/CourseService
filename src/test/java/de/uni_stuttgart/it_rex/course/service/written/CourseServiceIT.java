package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseDTO;
import de.uni_stuttgart.it_rex.course.utils.written.CourseUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(classes = {CourseServiceApp.class, TestSecurityConfiguration.class})
class CourseServiceIT {
  private static final int NUMBER_OF_COURSES = 20;
  private static final int THE_CHOSEN_INDEX = 13;
  private static final LocalDate NEW_DATE = LocalDate.now().plusDays(200);

  @Autowired
  private CourseService courseService;

  @Autowired
  private CourseRepository courseRepository;

  @MockBean
  private KeycloakAdminService keycloakAdminService;

  @AfterEach
  void cleanUp() {
    courseRepository.deleteAll();
  }

  @Test
  @Disabled
  void save() {
    final CourseDTO courseDTO = CourseUtil.createCourseDTO();
    courseService.save(courseDTO);
    final CourseDTO result = courseService.findAll().get(0);
    courseDTO.setId(result.getId());
    assertEquals(courseDTO, result);
  }

  @Test
  @Transactional
  void findAll() {
    final int numberOfEntitiesBefore = courseRepository.findAll().size();
    final List<CourseDTO> courseDTOs = CourseUtil.createCourseDTOs(NUMBER_OF_COURSES);

    courseDTOs.forEach(courseDTO -> {
      courseService.save(courseDTO);
    });

    final int numberOfEntitiesAfter = courseRepository.findAll().size();
    assertEquals(numberOfEntitiesBefore + courseDTOs.size(), numberOfEntitiesAfter);
  }

  @Test
  @Disabled
  void findOne() {
    final List<CourseDTO> courseDTOs = CourseUtil.createCourseDTOs(NUMBER_OF_COURSES);

    courseDTOs.forEach(courseDTO -> {
      courseService.save(courseDTO);
    });

    final CourseDTO theChosenCourse = courseService.findAll().get(THE_CHOSEN_INDEX);
    final CourseDTO result = courseService.findOne(theChosenCourse.getId()).get();

    assertEquals(theChosenCourse, result);
  }

  @Test
  @Disabled
  void delete() {
    doNothing().when(keycloakAdminService).removeRole(any(String.class));
    doNothing().when(keycloakAdminService).removeGroup(any(String.class));

    final int numberOfEntitiesBefore = courseRepository.findAll().size();
    final List<CourseDTO> courseDTOs = CourseUtil.createCourseDTOs(NUMBER_OF_COURSES);

    courseDTOs.forEach(courseDTO -> {
      courseService.save(courseDTO);
    });

    final CourseDTO theChosenCourse = courseService.findAll().get(THE_CHOSEN_INDEX);
    courseService.delete(theChosenCourse.getId());

    final int numberOfEntitiesAfter = courseRepository.findAll().size();
    assertThat(courseService.findAll(), not(hasItem(theChosenCourse)));
    assertEquals(numberOfEntitiesBefore + courseDTOs.size() - 1, numberOfEntitiesAfter);
  }

  @Test
  @Disabled
  void patch() {
    final CourseDTO courseDTO = CourseUtil.createCourseDTO();
    final UUID theId = courseService.save(courseDTO).getId();

    final CourseDTO patch = new CourseDTO();
    patch.setId(theId);
    patch.setEndDate(NEW_DATE);

    courseService.patch(patch);

    final CourseDTO expected = new CourseDTO();

    expected.setId(theId);
    expected.setName(courseDTO.getName());
    expected.setStartDate(courseDTO.getStartDate());
    expected.setEndDate(NEW_DATE);
    expected.setMaxFoodSum(courseDTO.getMaxFoodSum());
    expected.setRemainActiveOffset(courseDTO.getRemainActiveOffset());
    expected.setCourseDescription(courseDTO.getCourseDescription());
    expected.setPublishState(courseDTO.getPublishState());
//    expected.setContentReferences(Collections.emptyList());
    expected.setChapters(Collections.emptyList());
    expected.setTimePeriods(Collections.emptyList());

    final CourseDTO result = courseService.findAll().get(0);

    assertEquals(expected, result);
  }

}