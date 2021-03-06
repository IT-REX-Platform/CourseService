package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.utils.written.ChapterUtil;
import de.uni_stuttgart.it_rex.course.utils.written.CourseUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {CourseServiceApp.class, TestSecurityConfiguration.class})
class ChapterServiceIT {
  private static final int NUMBER_OF_CHAPTERS = 20;
  private static final int THE_CHOSEN_INDEX = 13;
  private static final LocalDate NEW_DATE = LocalDate.now().plusDays(200);

  @Autowired
  private ChapterService chapterService;

  @Autowired
  private ChapterRepository chapterRepository;

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
    chapterRepository.deleteAll();
  }

  @Test
  void save() {
    final ChapterDTO chapterDTO = ChapterUtil.createChapterDTO();
    chapterDTO.setCourseId(THE_COURSE.getId());
    chapterService.save(chapterDTO);
    final ChapterDTO result = chapterService.findAll().get(0);

    ChapterUtil.equalsChapterDTO(chapterDTO, result);
  }

  @Test
  @Transactional
  void findAll() {
    final int numberOfEntitiesBefore = chapterRepository.findAll().size();
    final List<ChapterDTO> chapterDTOs = ChapterUtil.createChapterDTOs(NUMBER_OF_CHAPTERS);

    chapterDTOs.forEach(chapterDTO -> {
      chapterDTO.setCourseId(THE_COURSE.getId());
      chapterService.save(chapterDTO);
    });

    final int numberOfEntitiesAfter = chapterRepository.findAll().size();
    assertEquals(numberOfEntitiesBefore + chapterDTOs.size(), numberOfEntitiesAfter);
  }

  @Test
  void findOne() {
    final List<ChapterDTO> chapterDTOs = ChapterUtil.createChapterDTOs(NUMBER_OF_CHAPTERS);

    chapterDTOs.forEach(chapterDTO -> {
      chapterDTO.setCourseId(THE_COURSE.getId());
      chapterService.save(chapterDTO);
    });

    final ChapterDTO theChosenChapter = chapterService.findAll().get(THE_CHOSEN_INDEX);
    final ChapterDTO result = chapterService.findOne(theChosenChapter.getId()).get();

    assertEquals(theChosenChapter, result);
  }

  @Test
  void delete() {
    final int numberOfEntitiesBefore = chapterRepository.findAll().size();
    final List<ChapterDTO> chapterDTOs = ChapterUtil.createChapterDTOs(NUMBER_OF_CHAPTERS);

    chapterDTOs.forEach(chapterDTO -> {
      chapterDTO.setCourseId(THE_COURSE.getId());
      chapterService.save(chapterDTO);
    });

    final ChapterDTO theChosenChapter = chapterService.findAll().get(THE_CHOSEN_INDEX);
    chapterService.delete(theChosenChapter.getId());

    final int numberOfEntitiesAfter = chapterRepository.findAll().size();
    assertThat(chapterService.findAll(), not(hasItem(theChosenChapter)));
    assertEquals(numberOfEntitiesBefore + chapterDTOs.size() - 1, numberOfEntitiesAfter);
  }

  @Test
  void patch() {
    final ChapterDTO chapterDTO = ChapterUtil.createChapterDTO();
    chapterDTO.setCourseId(THE_COURSE.getId());
    final UUID theId = chapterService.save(chapterDTO).getId();

    final ChapterDTO patch = new ChapterDTO();
    patch.setId(theId);
    patch.setEndDate(NEW_DATE);

    chapterService.patch(patch);

    final ChapterDTO expected = new ChapterDTO();

    expected.setId(theId);
    expected.setTitle(chapterDTO.getTitle());
    expected.setCourseId(THE_COURSE.getId());
    expected.setStartDate(chapterDTO.getStartDate());
    expected.setEndDate(NEW_DATE);


    final ChapterDTO result = chapterService.findAll().get(0);

    ChapterUtil.equalsChapterDTO(expected, result);
  }
}