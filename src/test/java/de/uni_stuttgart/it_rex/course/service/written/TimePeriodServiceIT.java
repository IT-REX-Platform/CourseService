package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.repository.written.TimePeriodRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.TimePeriodDTO;
import de.uni_stuttgart.it_rex.course.utils.written.CourseUtil;
import de.uni_stuttgart.it_rex.course.utils.written.TimePeriodUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {CourseServiceApp.class, TestSecurityConfiguration.class})
class TimePeriodServiceIT {
  private static final int NUMBER_OF_TimePeriodS = 20;
  private static final int THE_CHOSEN_INDEX = 13;
  private static final LocalDate NEW_DATE = LocalDate.now().plusDays(200);

  @Autowired
  private TimePeriodService timePeriodService;

  @Autowired
  private TimePeriodRepository timePeriodRepository;

  @Autowired
  private CourseRepository courseRepository;

  private static Course THE_COURSE;

  @BeforeEach
  void init() {
    THE_COURSE = CourseUtil.createCourse();
    courseRepository.save(THE_COURSE);
  }

  @AfterEach
  void cleanUp() {
    courseRepository.deleteAll();
    timePeriodRepository.deleteAll();
  }

  @Test
  void save() {
    final TimePeriodDTO timePeriodDTO = TimePeriodUtil.createTimePeriodDTO();
    timePeriodDTO.setCourseId(THE_COURSE.getId());
    timePeriodService.save(timePeriodDTO);
    final TimePeriodDTO result = timePeriodService.findAll().get(0);

    TimePeriodUtil.equalsTimePeriodDTO(timePeriodDTO, result);
  }

  @Test
  @Transactional
  void findAll() {
    final int numberOfEntitiesBefore = timePeriodRepository.findAll().size();
    final List<TimePeriodDTO> timePeriodDTOs = TimePeriodUtil.createTimePeriodDTOs(NUMBER_OF_TimePeriodS);

    timePeriodDTOs.forEach(timePeriodDTO -> {
      timePeriodDTO.setCourseId(THE_COURSE.getId());
      timePeriodService.save(timePeriodDTO);
    });

    final int numberOfEntitiesAfter = timePeriodRepository.findAll().size();
    assertEquals(numberOfEntitiesBefore + timePeriodDTOs.size(), numberOfEntitiesAfter);
  }

  @Test
  void findOne() {
    final List<TimePeriodDTO> timePeriodDTOs = TimePeriodUtil.createTimePeriodDTOs(NUMBER_OF_TimePeriodS);

    timePeriodDTOs.forEach(timePeriodDTO -> {
      timePeriodDTO.setCourseId(THE_COURSE.getId());
      timePeriodService.save(timePeriodDTO);
    });

    final TimePeriodDTO theChosenTimePeriod = timePeriodService.findAll().get(THE_CHOSEN_INDEX);
    final TimePeriodDTO result = timePeriodService.findOne(theChosenTimePeriod.getId()).get();

    assertEquals(theChosenTimePeriod, result);
  }

  @Test
  void delete() {
    final int numberOfEntitiesBefore = timePeriodRepository.findAll().size();
    final List<TimePeriodDTO> timePeriodDTOs = TimePeriodUtil.createTimePeriodDTOs(NUMBER_OF_TimePeriodS);

    timePeriodDTOs.forEach(timePeriodDTO -> {
      timePeriodDTO.setCourseId(THE_COURSE.getId());
      timePeriodService.save(timePeriodDTO);
    });

    final TimePeriodDTO theChosenTimePeriod = timePeriodService.findAll().get(THE_CHOSEN_INDEX);
    timePeriodService.delete(theChosenTimePeriod.getId());

    final int numberOfEntitiesAfter = timePeriodRepository.findAll().size();
    assertThat(timePeriodService.findAll(), not(hasItem(theChosenTimePeriod)));
    assertEquals(numberOfEntitiesBefore + timePeriodDTOs.size() - 1, numberOfEntitiesAfter);
  }

  @Test
  void patch() {
    final TimePeriodDTO timePeriodDTO = TimePeriodUtil.createTimePeriodDTO();
    timePeriodDTO.setCourseId(THE_COURSE.getId());
    final UUID theId = timePeriodService.save(timePeriodDTO).getId();

    final TimePeriodDTO patch = new TimePeriodDTO();
    patch.setId(theId);
    patch.setEndDate(NEW_DATE);

    timePeriodService.patch(patch);

    final TimePeriodDTO expected = new TimePeriodDTO();

    expected.setId(theId);
    expected.setCourseId(timePeriodDTO.getCourseId());
    expected.setStartDate(timePeriodDTO.getStartDate());
    expected.setContentReferenceIds(Collections.emptyList());
    expected.setEndDate(NEW_DATE);


    final TimePeriodDTO result = timePeriodService.findAll().get(0);

    assertEquals(expected, result);
  }
}