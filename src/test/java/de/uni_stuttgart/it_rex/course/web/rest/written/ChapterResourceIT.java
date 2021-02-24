package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.written.Chapter;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.web.rest.TestUtil;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
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

  private static final UUID COURSE_ID = UUID.randomUUID();

  private static final String PREVIOUS_TITLE = "AAAAAAAAAA";
  private static final String MIDDLE_TITLE = "BBBBBBBBBB";
  private static final String NEXT_TITLE = "CCCCCCCCC";

  private static final LocalDate PREVIOUS_START_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate MIDDLE_START_DATE = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate NEXT_START_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final LocalDate PREVIOUS_END_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate MIDDLE_END_DATE = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate NEXT_END_DATE = LocalDate.now(ZoneId.systemDefault());


  private static final String EXPECTED_EXCEPTION_MESSAGE = "Invalid id";

  @Autowired
  private ChapterRepository chapterRepository;

  @Autowired
  private ChapterResource chapterResource;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restChapterMockMvc;

  private Chapter previousChapter;
  private Chapter middleChapter;
  private Chapter nextChapter;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Chapter createPrevious(EntityManager em) {
    Chapter previous = new Chapter();
    previous.setTitle(PREVIOUS_TITLE);
    previous.setCourseId(COURSE_ID);
    previous.setStartDate(PREVIOUS_START_DATE);
    previous.setEndDate(PREVIOUS_END_DATE);
    return previous;
  }

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Chapter createMiddle(EntityManager em) {
    Chapter middle = new Chapter();
    middle.setTitle(MIDDLE_TITLE);
    middle.setCourseId(COURSE_ID);
    middle.setStartDate(MIDDLE_START_DATE);
    middle.setEndDate(MIDDLE_END_DATE);
    return middle;
  }

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Chapter createNext(EntityManager em) {
    Chapter next = new Chapter();
    next.setTitle(NEXT_TITLE);
    next.setCourseId(COURSE_ID);
    next.setStartDate(NEXT_START_DATE);
    next.setEndDate(NEXT_END_DATE);
    return next;
  }

  /**
   * Create an updated entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Chapter createUpdatedEntity(EntityManager em) {
    Chapter chapter = new Chapter();
    chapter.setTitle(MIDDLE_TITLE);
    chapter.setCourseId(COURSE_ID);
    chapter.setStartDate(MIDDLE_START_DATE);
    chapter.setEndDate(MIDDLE_END_DATE);
    return chapter;
  }

  @BeforeEach
  public void initTest() {
    previousChapter = createPrevious(em);
    middleChapter = createMiddle(em);
    nextChapter = createNext(em);
  }

  @Test
  @Transactional
  public void createChapter() throws Exception {
    int databaseSizeBeforeCreate = chapterRepository.findAll().size();
    // Create the Course
    restChapterMockMvc.perform(post("/api/chapters").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(previousChapter)))
        .andExpect(status().isCreated());

    // Validate the Course in the database
    List<Chapter> courseList = chapterRepository.findAll();
    assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
    Chapter testChapter = courseList.get(courseList.size() - 1);
    assertThat(testChapter.getTitle()).isEqualTo(PREVIOUS_TITLE);
    assertThat(testChapter.getCourseId()).isEqualTo(COURSE_ID);
    assertThat(testChapter.getStartDate()).isEqualTo(PREVIOUS_START_DATE);
    assertThat(testChapter.getEndDate()).isEqualTo(PREVIOUS_END_DATE);
  }

  @Test
  @Transactional
  public void createChapterWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = chapterRepository.findAll().size();

    // Create the Course with an existing ID
    middleChapter.setId(UUID.randomUUID());

    // An entity with an existing ID cannot be created, so this API call must fail
    restChapterMockMvc.perform(post("/api/chapters").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(middleChapter)))
        .andExpect(status().isBadRequest());

    // Validate the Course in the database
    List<Chapter> chapterList = chapterRepository.findAll();
    assertThat(chapterList).hasSize(databaseSizeBeforeCreate);
  }


  @Test
  @Transactional
  public void getAllChapters() throws Exception {
    // Initialize the database
    chapterRepository.saveAndFlush(previousChapter);

    // Get all the courseList
    restChapterMockMvc.perform(get("/api/chapters?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(previousChapter.getId().toString())))
        .andExpect(jsonPath("$.[*].title").value(hasItem(PREVIOUS_TITLE)))
        .andExpect(jsonPath("$.[*].courseId").value(hasItem(COURSE_ID.toString())))
        .andExpect(jsonPath("$.[*].startDate").value(hasItem(PREVIOUS_START_DATE.toString())))
        .andExpect(jsonPath("$.[*].endDate").value(hasItem(PREVIOUS_END_DATE.toString())));
  }

  @Test
  @Transactional
  public void getChapter() throws Exception {
    // Initialize the database
    chapterRepository.saveAndFlush(previousChapter);

    // Get the course
    restChapterMockMvc.perform(get("/api/chapters/{id}", previousChapter.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(previousChapter.getId().toString()))
        .andExpect(jsonPath("$.title").value(PREVIOUS_TITLE))
        .andExpect(jsonPath("$.courseId").value(COURSE_ID.toString()))
        .andExpect(jsonPath("$.startDate").value(PREVIOUS_START_DATE.toString()))
        .andExpect(jsonPath("$.endDate").value(PREVIOUS_END_DATE.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingCourse() throws Exception {
    // Get the course
    restChapterMockMvc.perform(get("/api/chapters/{id}", NON_EXISTING_ID))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateCourse() throws Exception {
    // Initialize the database
    chapterRepository.saveAndFlush(previousChapter);

    int databaseSizeBeforeUpdate = chapterRepository.findAll().size();

    // Update the chapter
    Chapter updatedCourse = chapterRepository.findById(previousChapter.getId()).get();
    // Disconnect from session so that the updates on updatedChapter are not directly saved in db
    em.detach(updatedCourse);

    updatedCourse.setTitle(MIDDLE_TITLE);
    updatedCourse.setCourseId(COURSE_ID);
    updatedCourse.setStartDate(MIDDLE_START_DATE);
    updatedCourse.setEndDate(MIDDLE_END_DATE);

    restChapterMockMvc.perform(put("/api/chapters").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(updatedCourse)))
        .andExpect(status().isOk());

    // Validate the Course in the database
    List<Chapter> chapterList = chapterRepository.findAll();
    assertThat(chapterList).hasSize(databaseSizeBeforeUpdate);
    Chapter testCourse = chapterList.get(chapterList.size() - 1);
    assertThat(testCourse.getTitle()).isEqualTo(MIDDLE_TITLE);
    assertThat(testCourse.getCourseId()).isEqualTo(COURSE_ID);
    assertThat(testCourse.getStartDate()).isEqualTo(MIDDLE_START_DATE);
    assertThat(testCourse.getEndDate()).isEqualTo(MIDDLE_END_DATE);
  }

  @Test
  @Transactional
  public void updateNonExistingCourse() throws Exception {
    int databaseSizeBeforeUpdate = chapterRepository.findAll().size();

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restChapterMockMvc.perform(put("/api/chapters").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(nextChapter)))
        .andExpect(status().isBadRequest());

    // Validate the Course in the database
    List<Chapter> chapterList = chapterRepository.findAll();
    assertThat(chapterList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteCourse() throws Exception {
    // Initialize the database
    final Chapter toDelete = chapterRepository.saveAndFlush(middleChapter);

    chapterRepository.saveAndFlush(toDelete);

    int databaseSizeBeforeDelete = chapterRepository.findAll().size();

    // Delete the chapter
    restChapterMockMvc.perform(delete("/api/chapters/{id}", toDelete.getId()).with(csrf())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Chapter> chapterList = chapterRepository.findAll();
    assertThat(chapterList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  void patchCourse() throws URISyntaxException {
    Chapter toUpdate = new Chapter();
    toUpdate.setTitle(PREVIOUS_TITLE);
    toUpdate.setCourseId(COURSE_ID);
    toUpdate.setStartDate(PREVIOUS_START_DATE);
    toUpdate.setEndDate(PREVIOUS_END_DATE);

    UUID id = chapterResource.createChapter(toUpdate).getBody().getId();
    Chapter update = new Chapter();
    update.setId(id);
    update.setTitle(MIDDLE_TITLE);
    update.setCourseId(COURSE_ID);
    update.setEndDate(MIDDLE_END_DATE);

    chapterResource.patchChapter(update).getBody();

    Chapter expected = new Chapter();
    expected.setId(id);
    expected.setTitle(MIDDLE_TITLE);
    expected.setCourseId(COURSE_ID);
    expected.setStartDate(PREVIOUS_START_DATE);
    expected.setEndDate(MIDDLE_END_DATE);

    Chapter updated = chapterResource.getChapter(id).getBody();

    assertThat(updated).isEqualTo(expected);
  }

  @Test
  @Transactional
  void patchCourseWithoutId() {
    Chapter toUpdate = new Chapter();
    toUpdate.setTitle(MIDDLE_TITLE);
    toUpdate.setCourseId(COURSE_ID);

    Exception e = assertThrows(BadRequestAlertException.class, () -> chapterResource.patchChapter(toUpdate));
    assertThat(e.getMessage()).isEqualTo(EXPECTED_EXCEPTION_MESSAGE);
  }
}
