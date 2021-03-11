package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.repository.written.TimePeriodRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.ChapterMapper;
import de.uni_stuttgart.it_rex.course.utils.written.ChapterUtil;
import de.uni_stuttgart.it_rex.course.utils.written.CourseUtil;
import de.uni_stuttgart.it_rex.course.web.rest.TestUtil;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ChapterResource} REST controller.
 */
@SpringBootTest(classes = {CourseServiceApp.class, TestSecurityConfiguration.class})
@AutoConfigureMockMvc
@WithMockUser
public class ChapterResourceIT {

  private static final UUID NON_EXISTING_ID = UUID.randomUUID();

  private static final String NEW_NAME = "BBBBBBBBBB";

  private static final String EXPECTED_EXCEPTION_MESSAGE = "Invalid id";

  @Autowired
  private ChapterRepository chapterRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private TimePeriodRepository timePeriodRepository;

  @Autowired
  private ContentReferenceRepository contentReferenceRepository;

  @Autowired
  private ChapterResource chapterResource;

  @Autowired
  private ChapterMapper chapterMapper;

  @Autowired
  private MockMvc restChapterMockMvc;

  private static Course THE_COURSE;

  @BeforeEach
  void init() {
    THE_COURSE = CourseUtil.createCourse();
    courseRepository.save(THE_COURSE);
  }

  @AfterEach
  public void cleanup() {
    contentReferenceRepository.deleteAll();
    chapterRepository.deleteAll();
    timePeriodRepository.deleteAll();
    courseRepository.deleteAll();
  }

  @Test
  @Transactional
  public void createChapter() throws Exception {
    final int databaseSizeBeforeCreate = chapterRepository.findAll().size();
    final ChapterDTO chapterDTO = ChapterUtil.createChapterDTO();
    chapterDTO.setCourseId(THE_COURSE.getId());

    // Create the Chapter
    restChapterMockMvc.perform(post("/api/chapters").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(TestUtil.convertObjectToJsonBytes(chapterDTO)))
      .andExpect(status().isCreated());

    // Validate the Chapter in the database
    assertThat(chapterRepository.findAll()).hasSize(databaseSizeBeforeCreate + 1);
  }

  @Test
  @Transactional
  public void createChapterWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = chapterRepository.findAll().size();

    // Create the Chapter with an existing ID
    final ChapterDTO chapterDTO2 = ChapterUtil.createChapterDTO();
    chapterDTO2.setCourseId(THE_COURSE.getId());
    chapterDTO2.setId(UUID.randomUUID());

    // An entity with an existing ID cannot be created, so this API call must fail
    restChapterMockMvc.perform(post("/api/chapters").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(TestUtil.convertObjectToJsonBytes(chapterDTO2)))
      .andExpect(status().isBadRequest());

    // Validate the Chapter in the database
    List<Chapter> chapterList = chapterRepository.findAll();
    assertThat(chapterList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllChapters() throws Exception {
    // Initialize the database
    final Chapter chapter = ChapterUtil.createChapter();
    chapter.setCourse(THE_COURSE);
    chapter.setName(NEW_NAME);

    // Get all the Chapter
    restChapterMockMvc.perform(get("/api/chapters?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(chapter.getId().toString())))
      .andExpect(jsonPath("$.[*].name").value(hasItem(chapter.getName())))
      .andExpect(jsonPath("$.[*].chapterNumber").value(chapter.getChapterNumber()))
      .andExpect(jsonPath("$.[*].courseId").value(hasItem(chapter.getCourse().getId().toString())));
  }

  @Test
  @Transactional
  public void getChapter() throws Exception {
    // Initialize the database
    Chapter chapter = ChapterUtil.createChapter();
    chapter.setCourse(THE_COURSE);
    chapterRepository.saveAndFlush(chapter);

    // Get the Chapter
    restChapterMockMvc.perform(get("/api/chapters/{id}", chapter.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(chapter.getId().toString()))
      .andExpect(jsonPath("$.name").value(chapter.getName()))
      .andExpect(jsonPath("$.chapterNumber").value(chapter.getChapterNumber()))
      .andExpect(jsonPath("$.courseId").value(chapter.getCourse().getId().toString()));
  }

  @Test
  @Transactional
  public void getNonExistingChapter() throws Exception {
    // Get the chapter
    restChapterMockMvc.perform(get("/api/chapters/{id}", NON_EXISTING_ID))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateNonExistingChapter() throws Exception {
    int databaseSizeBeforeUpdate = chapterRepository.findAll().size();
    final ChapterDTO chapterDTO = ChapterUtil.createChapterDTO();
    chapterDTO.setCourseId(THE_COURSE.getId());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restChapterMockMvc.perform(put("/api/chapters").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(TestUtil.convertObjectToJsonBytes(chapterDTO)))
      .andExpect(status().isBadRequest());

    // Validate the Chapter in the database
    List<Chapter> chapterList = chapterRepository.findAll();
    assertThat(chapterList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  public void deleteChapter() throws Exception {
    // Initialize the database
    final Chapter chapter = ChapterUtil.createChapter();
    chapter.setCourse(THE_COURSE);
    chapterRepository.saveAndFlush(chapter);

    final int databaseSizeBeforeDelete = courseRepository.findAll().size();

    // Delete the course
    restChapterMockMvc.perform(delete("/api/chapters/{id}", chapter.getId()).with(csrf())
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    final List<Chapter> chapterList = chapterRepository.findAll();
    assertThat(chapterList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  void patchChapterWithoutId() {
    Chapter toUpdate = new Chapter();
    toUpdate.setName(NEW_NAME);

    Exception e = assertThrows(BadRequestAlertException.class, () -> chapterResource.patchChapter(chapterMapper.toDTO(toUpdate)));
    assertThat(e.getMessage()).isEqualTo(EXPECTED_EXCEPTION_MESSAGE);
  }
}
