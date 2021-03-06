package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import de.uni_stuttgart.it_rex.course.utils.written.ContentReferenceUtil;
import de.uni_stuttgart.it_rex.course.utils.written.CourseUtil;
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
class ContentReferenceServiceIT {
  private static final int NUMBER_OF_CHAPTERS = 20;
  private static final int THE_CHOSEN_INDEX = 13;
  private static final LocalDate NEW_DATE = LocalDate.now().plusDays(200);

  @Autowired
  private ContentReferenceService contentReferenceService;

  @Autowired
  private ContentReferenceRepository contentReferenceRepository;

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
    contentReferenceRepository.deleteAll();
  }

  @Test
  void save() {
    final ContentReferenceDTO contentReferenceDTO = ContentReferenceUtil.createContentReferenceDTO();
    contentReferenceDTO.setCourseId(THE_COURSE.getId());
    contentReferenceService.save(contentReferenceDTO);
    final ContentReferenceDTO result = contentReferenceService.findAll().get(0);

    ContentReferenceUtil.equalsContentReferenceDTO(contentReferenceDTO, result);
  }

  @Test
  @Transactional
  void findAll() {
    final int numberOfEntitiesBefore = contentReferenceRepository.findAll().size();
    final List<ContentReferenceDTO> contentReferenceDTOs = ContentReferenceUtil.createContentReferenceDTOs(NUMBER_OF_CHAPTERS);

    contentReferenceDTOs.forEach(contentReferenceDTO -> {
      contentReferenceDTO.setCourseId(THE_COURSE.getId());
      contentReferenceService.save(contentReferenceDTO);
    });

    final int numberOfEntitiesAfter = contentReferenceRepository.findAll().size();
    assertEquals(numberOfEntitiesBefore + contentReferenceDTOs.size(), numberOfEntitiesAfter);
  }

  @Test
  void findOne() {
    final List<ContentReferenceDTO> contentReferenceDTOs = ContentReferenceUtil.createContentReferenceDTOs(NUMBER_OF_CHAPTERS);

    contentReferenceDTOs.forEach(contentReferenceDTO -> {
      contentReferenceDTO.setCourseId(THE_COURSE.getId());
      contentReferenceService.save(contentReferenceDTO);
    });

    final ContentReferenceDTO theChosenContentReference = contentReferenceService.findAll().get(THE_CHOSEN_INDEX);
    final ContentReferenceDTO result = contentReferenceService.findOne(theChosenContentReference.getId()).get();

    assertEquals(theChosenContentReference, result);
  }

  @Test
  void delete() {
    final int numberOfEntitiesBefore = contentReferenceRepository.findAll().size();
    final List<ContentReferenceDTO> contentReferenceDTOs = ContentReferenceUtil.createContentReferenceDTOs(NUMBER_OF_CHAPTERS);

    contentReferenceDTOs.forEach(contentReferenceDTO -> {
      contentReferenceDTO.setCourseId(THE_COURSE.getId());
      contentReferenceService.save(contentReferenceDTO);
    });

    final ContentReferenceDTO theChosenContentReference = contentReferenceService.findAll().get(THE_CHOSEN_INDEX);
    contentReferenceService.delete(theChosenContentReference.getId());

    final int numberOfEntitiesAfter = contentReferenceRepository.findAll().size();
    assertThat(contentReferenceService.findAll(), not(hasItem(theChosenContentReference)));
    assertEquals(numberOfEntitiesBefore + contentReferenceDTOs.size() - 1, numberOfEntitiesAfter);
  }

  @Test
  void patch() {
    final ContentReferenceDTO contentReferenceDTO = ContentReferenceUtil.createContentReferenceDTO();
    contentReferenceDTO.setCourseId(THE_COURSE.getId());
    final UUID theId = contentReferenceService.save(contentReferenceDTO).getId();

    final ContentReferenceDTO patch = new ContentReferenceDTO();
    patch.setId(theId);
    patch.setEndDate(NEW_DATE);

    contentReferenceService.patch(patch);

    final ContentReferenceDTO expected = new ContentReferenceDTO();

    expected.setId(theId);
    expected.setCourseId(contentReferenceDTO.getCourseId());
    expected.setStartDate(contentReferenceDTO.getStartDate());
    expected.setContentId(contentReferenceDTO.getContentId());
    expected.setChapterIds(Collections.emptySet());
    expected.setTimePeriodIds(Collections.emptySet());
    expected.setEndDate(NEW_DATE);

    final ContentReferenceDTO result = contentReferenceService.findAll().get(0);

    assertEquals(expected, result);
  }
}