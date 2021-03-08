package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.ChapterMapper;
import de.uni_stuttgart.it_rex.course.web.rest.TestUtil;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import static de.uni_stuttgart.it_rex.course.utils.written.ContentReferenceUtil.createContentReferenceList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ChapterResource} REST controller.
 */
@Disabled
@SpringBootTest(classes = {CourseServiceApp.class, TestSecurityConfiguration.class})
@AutoConfigureMockMvc
@WithMockUser
public class ChapterResourceIT {

  private static final UUID NON_EXISTING_ID = UUID.randomUUID();

  private static final UUID COURSE_ID = UUID.randomUUID();

  private static final UUID FIRST_CHAPTER_ID = UUID.randomUUID();
  private static final UUID SECOND_CHAPTER_ID = UUID.randomUUID();
  private static final UUID THIRD_CHAPTER_ID = UUID.randomUUID();

  private static final String FIRST_TITLE = "AAAAAAAAAA";
  private static final String SECOND_TITLE = "BBBBBBBBBB";
  private static final String THIRD_TITLE = "CCCCCCCCC";

  private static final LocalDate FIRST_START_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate SECOND_START_DATE = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate THIRD_START_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final LocalDate FIRST_END_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate SECOND_END_DATE = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate THIRD_END_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final List<ContentReference> EXPECTED_CONTENTS = createContentReferenceList(34);

  private static final String EXPECTED_EXCEPTION_MESSAGE = "Invalid id";

  @Autowired
  private ChapterRepository chapterRepository;

  @Autowired
  private ChapterResource chapterResource;

  @Autowired
  private ChapterMapper chapterMapper;

  @Autowired
  private ContentReferenceRepository contentReferenceRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restChapterMockMvc;

  private Chapter chapter1;
  private Chapter chapter2;
  private Chapter chapter3;

  private static List<ContentReference> firstContents;
  private static List<ContentReference> secondContents;
  private static List<ContentReference> thirdContents;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Chapter createFirstChapter(EntityManager em) {
    Chapter chapter = new Chapter();
    chapter.setName(FIRST_TITLE);
  //  chapter.setStartDate(FIRST_START_DATE);
  //  chapter.setEndDate(FIRST_END_DATE);
    return chapter;
  }

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Chapter createSecondChapter(EntityManager em) {
    Chapter chapter = new Chapter();
    chapter.setName(SECOND_TITLE);
  //  chapter.setStartDate(SECOND_START_DATE);
  //  chapter.setEndDate(SECOND_END_DATE);
    return chapter;
  }

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Chapter createThirdChapter(EntityManager em) {
    Chapter chapter = new Chapter();
    chapter.setName(THIRD_TITLE);
 //  chapter.setStartDate(THIRD_START_DATE);
 //  chapter.setEndDate(THIRD_END_DATE);
    return chapter;
  }

  /**
   * Create an updated entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Chapter createUpdatedEntity(EntityManager em) {
    Chapter chapter = new Chapter();
    chapter.setName(SECOND_TITLE);
  // chapter.setStartDate(SECOND_START_DATE);
  // chapter.setEndDate(SECOND_END_DATE);
    return chapter;
  }

  @BeforeEach
  public void initTest() {
    firstContents = createContentReferenceList(34);
    secondContents = createContentReferenceList(69);
    thirdContents = createContentReferenceList(42);
    chapter1 = createFirstChapter(em);
    chapter2 = createSecondChapter(em);
    chapter3 = createThirdChapter(em);
  }

  @AfterEach
  public void cleanup() {
    chapterRepository.deleteAll();
    contentReferenceRepository.deleteAll();
  }

  @Test
  @Transactional
  public void createChapter() throws Exception {
    int databaseSizeBeforeCreate = chapterRepository.findAll().size();
    // Create the Chapter
    restChapterMockMvc.perform(post("/api/chapters").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(chapterMapper
            .toDTO(chapter1))))
        .andExpect(status().isCreated());

    // Validate the Chapter in the database
    List<Chapter> chapterList = chapterRepository.findAll();
    assertThat(chapterList).hasSize(databaseSizeBeforeCreate + 1);
    Chapter testChapter = chapterList.get(chapterList.size() - 1);
    assertEquals(FIRST_TITLE, testChapter.getName());
  // assertEquals(FIRST_START_DATE, testChapter.getStartDate());
  // assertEquals(FIRST_END_DATE, testChapter.getEndDate());
  }

  @Test
  @Transactional
  public void createChapterWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = chapterRepository.findAll().size();

    // Create the Chapter with an existing ID
    chapter2.setId(UUID.randomUUID());

    // An entity with an existing ID cannot be created, so this API call must fail
    restChapterMockMvc.perform(post("/api/chapters").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(chapter2)))
        .andExpect(status().isBadRequest());

    // Validate the Chapter in the database
    List<Chapter> chapterList = chapterRepository.findAll();
    assertThat(chapterList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  public void getAllChapters() throws Exception {
    // Initialize the database
    chapterRepository.saveAndFlush(chapter2);

    // Get all the Chapter
    restChapterMockMvc.perform(get("/api/chapters?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(chapter2.getId().toString())))
        .andExpect(jsonPath("$.[*].title").value(hasItem(SECOND_TITLE)))
        .andExpect(jsonPath("$.[*].courseId").value(hasItem(COURSE_ID.toString())))
        .andExpect(jsonPath("$.[*].startDate").value(hasItem(SECOND_START_DATE.toString())))
        .andExpect(jsonPath("$.[*].endDate").value(hasItem(SECOND_END_DATE.toString())));
  }

  @Test
  @Transactional
  public void getChapter() throws Exception {
    // Initialize the database
    chapterRepository.saveAndFlush(chapter2);

    // Get the Chapter
    restChapterMockMvc.perform(get("/api/chapters/{id}", chapter2.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(chapter2.getId().toString()))
        .andExpect(jsonPath("$.title").value(SECOND_TITLE))
        .andExpect(jsonPath("$.courseId").value(COURSE_ID.toString()))
        .andExpect(jsonPath("$.startDate").value(SECOND_START_DATE.toString()))
        .andExpect(jsonPath("$.endDate").value(SECOND_END_DATE.toString()));
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
  public void updateChapter() throws Exception {
    // Initialize the database
    chapterRepository.saveAndFlush(chapter1);

    int databaseSizeBeforeUpdate = chapterRepository.findAll().size();

    // Update the chapter
    Chapter updatedChapter = chapterRepository.findById(chapter1.getId()).get();
    // Disconnect from session so that the updates on updatedChapter are not directly saved in db
    em.detach(updatedChapter);

    updatedChapter.setName(SECOND_TITLE);
   // updatedChapter.setStartDate(SECOND_START_DATE);
   // updatedChapter.setEndDate(SECOND_END_DATE);

    restChapterMockMvc.perform(put("/api/chapters").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(chapterMapper
            .toDTO(updatedChapter))))
        .andExpect(status().isOk());

    // Validate the chapter in the database
    List<Chapter> chapterList = chapterRepository.findAll();
    assertThat(chapterList).hasSize(databaseSizeBeforeUpdate);
    Chapter testChapter = chapterList.get(chapterList.size() - 1);
    assertThat(testChapter.getName()).isEqualTo(SECOND_TITLE);
 //   assertThat(testChapter.getStartDate()).isEqualTo(SECOND_START_DATE);
 //   assertThat(testChapter.getEndDate()).isEqualTo(SECOND_END_DATE);
  }

  @Test
  @Transactional
  public void updateNonExistingChapter() throws Exception {
    int databaseSizeBeforeUpdate = chapterRepository.findAll().size();

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restChapterMockMvc.perform(put("/api/chapters").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(chapter3)))
        .andExpect(status().isBadRequest());

    // Validate the Chapter in the database
    List<Chapter> chapterList = chapterRepository.findAll();
    assertThat(chapterList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteChapter() throws Exception {
    // Initialize the database
    final Chapter toDelete = chapterRepository.saveAndFlush(chapter2);

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
  void patchChapter() throws URISyntaxException {
    Chapter toUpdate = new Chapter();
    toUpdate.setName(FIRST_TITLE);
  //  toUpdate.setStartDate(FIRST_START_DATE);
  //  toUpdate.setEndDate(FIRST_END_DATE);
   // toUpdate.setContents(EXPECTED_CONTENTS);

    UUID id = chapterResource.createChapter(chapterMapper.toDTO(toUpdate)).getBody().getId();
    Chapter update = new Chapter();
    update.setId(id);
    update.setName(SECOND_TITLE);
   // update.setEndDate(SECOND_END_DATE);

    chapterResource.patchChapter(chapterMapper.toDTO(update));

    Chapter expected = new Chapter();
    expected.setId(id);
    expected.setName(SECOND_TITLE);
  //  expected.setStartDate(FIRST_START_DATE);
  //  expected.setEndDate(SECOND_END_DATE);
   // expected.setContents(EXPECTED_CONTENTS);

    ChapterDTO updated = chapterResource.getChapter(id).getBody();

    expected.setId(id);

    assertEquals(updated.getId(), expected.getId());
    assertEquals(updated.getName(), expected.getName());
 //  assertEquals(updated.getStartDate(), expected.getStartDate());
 //  assertEquals(updated.getEndDate(), expected.getEndDate());
  }

  @Test
  @Transactional
  void patchChapterWithoutId() {
    Chapter toUpdate = new Chapter();
    toUpdate.setName(SECOND_TITLE);

    Exception e = assertThrows(BadRequestAlertException.class, () -> chapterResource.patchChapter(chapterMapper.toDTO(toUpdate)));
    assertThat(e.getMessage()).isEqualTo(EXPECTED_EXCEPTION_MESSAGE);
  }
}
