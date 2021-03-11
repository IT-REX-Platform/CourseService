package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.domain.written_entities.TimePeriod;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.repository.written.TimePeriodRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.ContentReferenceMapper;
import de.uni_stuttgart.it_rex.course.utils.written.ChapterUtil;
import de.uni_stuttgart.it_rex.course.utils.written.ContentReferenceUtil;
import de.uni_stuttgart.it_rex.course.utils.written.CourseUtil;
import de.uni_stuttgart.it_rex.course.utils.written.TimePeriodUtil;
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
 * Integration tests for the {@link ContentReferenceResource} REST controller.
 */
@SpringBootTest(classes = {CourseServiceApp.class, TestSecurityConfiguration.class})
@AutoConfigureMockMvc
@WithMockUser
public class ContentReferenceResourceIT {

  private static final UUID NON_EXISTING_ID = UUID.randomUUID();

  private static final String EXPECTED_EXCEPTION_MESSAGE = "Invalid id";

  @Autowired
  private ContentReferenceRepository contentReferenceRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private ContentReferenceResource contentReferenceResource;

  @Autowired
  private ChapterRepository chapterRepository;

  @Autowired
  private TimePeriodRepository timePeriodRepository;

  @Autowired
  private ContentReferenceMapper contentReferenceMapper;

  @Autowired
  private MockMvc restContentReferenceMockMvc;

  private static Course THE_COURSE;
  private static Chapter THE_CHAPTER;
  private static TimePeriod THE_TIME_PERIOD;

  @BeforeEach
  void init() {
    THE_COURSE = CourseUtil.createCourse();
    THE_CHAPTER = ChapterUtil.createChapter();
    THE_TIME_PERIOD = TimePeriodUtil.createTimePeriod();
    THE_CHAPTER.setCourse(THE_COURSE);
    THE_TIME_PERIOD.setCourse(THE_COURSE);
    courseRepository.save(THE_COURSE);
    chapterRepository.save(THE_CHAPTER);
    timePeriodRepository.save(THE_TIME_PERIOD);
  }

  @AfterEach
  public void cleanup() {
    contentReferenceRepository.deleteAll();
    contentReferenceRepository.deleteAll();
    courseRepository.deleteAll();
  }

  @Test
  @Transactional
  public void createContentReference() throws Exception {
    final int databaseSizeBeforeCreate = contentReferenceRepository.findAll().size();
    final ContentReferenceDTO contentReferenceDTO = ContentReferenceUtil.createContentReferenceDTO();
    contentReferenceDTO.setChapterId(THE_CHAPTER.getId());
    contentReferenceDTO.setTimePeriodId(THE_TIME_PERIOD.getId());

    // Create the ContentReference
    restContentReferenceMockMvc.perform(post("/api/contentreferences").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(TestUtil.convertObjectToJsonBytes(contentReferenceDTO)))
      .andExpect(status().isCreated());

    // Validate the ContentReference in the database
    assertThat(contentReferenceRepository.findAll()).hasSize(databaseSizeBeforeCreate + 1);
  }

  @Test
  @Transactional
  public void createContentReferenceWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = contentReferenceRepository.findAll().size();

    // Create the ContentReference with an existing ID
    final ContentReferenceDTO contentReferenceDTO2 = ContentReferenceUtil.createContentReferenceDTO();
    contentReferenceDTO2.setChapterId(THE_CHAPTER.getId());
    contentReferenceDTO2.setTimePeriodId(THE_TIME_PERIOD.getId());
    contentReferenceDTO2.setId(UUID.randomUUID());

    // An entity with an existing ID cannot be created, so this API call must fail
    restContentReferenceMockMvc.perform(post("/api/contentreferences").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(TestUtil.convertObjectToJsonBytes(contentReferenceDTO2)))
      .andExpect(status().isBadRequest());

    // Validate the ContentReference in the database
    List<ContentReference> contentReferenceList = contentReferenceRepository.findAll();
    assertThat(contentReferenceList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllContentReferences() throws Exception {
    // Initialize the database
    final ContentReference contentReference = ContentReferenceUtil.createContentReference();
    contentReference.setChapter(THE_CHAPTER);
    contentReference.setTimePeriod(THE_TIME_PERIOD);

    // Get all the ContentReference
    restContentReferenceMockMvc.perform(get("/api/contentreferences?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(contentReference.getId().toString())))
      .andExpect(jsonPath("$.[*].contentId").value(hasItem(contentReference.getContentId().toString())))
      .andExpect(jsonPath("$.[*].contentReferenceType").value(hasItem(contentReference.getContentReferenceType().toString())))
      .andExpect(jsonPath("$.[*].chapterId").value(hasItem(contentReference.getChapter().getId().toString())))
      .andExpect(jsonPath("$.[*].timePeriodId").value(hasItem(contentReference.getTimePeriod().getId().toString())));
  }

  @Test
  @Transactional
  public void getContentReference() throws Exception {
    // Initialize the database
    ContentReference contentReference = ContentReferenceUtil.createContentReference();
    contentReference.setChapter(THE_CHAPTER);
    contentReference.setTimePeriod(THE_TIME_PERIOD);
    contentReferenceRepository.saveAndFlush(contentReference);

    // Get the ContentReference
    restContentReferenceMockMvc.perform(get("/api/contentreferences/{id}", contentReference.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(contentReference.getId().toString()))
      .andExpect(jsonPath("$.contentId").value(contentReference.getContentId().toString()))
      .andExpect(jsonPath("$.contentReferenceType").value(contentReference.getContentReferenceType().toString()))
      .andExpect(jsonPath("$.chapterId").value(contentReference.getChapter().getId().toString()))
      .andExpect(jsonPath("$.timePeriodId").value(contentReference.getTimePeriod().getId().toString()));
  }

  @Test
  @Transactional
  public void getNonExistingContentReference() throws Exception {
    // Get the contentReference
    restContentReferenceMockMvc.perform(get("/api/contentreferences/{id}", NON_EXISTING_ID))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void patchNonExistingContentReference() throws Exception {
    int databaseSizeBeforeUpdate = contentReferenceRepository.findAll().size();
    final ContentReferenceDTO contentReferenceDTO = ContentReferenceUtil.createContentReferenceDTO();
    contentReferenceDTO.setChapterId(THE_CHAPTER.getId());
    contentReferenceDTO.setTimePeriodId(THE_TIME_PERIOD.getId());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restContentReferenceMockMvc.perform(patch("/api/contentreferences").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(TestUtil.convertObjectToJsonBytes(contentReferenceDTO)))
      .andExpect(status().isBadRequest());

    // Validate the ContentReference in the database
    List<ContentReference> contentReferenceList = contentReferenceRepository.findAll();
    assertThat(contentReferenceList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  public void deleteContentReference() throws Exception {
    // Initialize the database
    final ContentReference contentReference = ContentReferenceUtil.createContentReference();
    contentReference.setChapter(THE_CHAPTER);
    contentReferenceRepository.saveAndFlush(contentReference);

    final int databaseSizeBeforeDelete = courseRepository.findAll().size();

    // Delete the course
    restContentReferenceMockMvc.perform(delete("/api/contentreferences/{id}", contentReference.getId()).with(csrf())
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    final List<ContentReference> contentReferenceList = contentReferenceRepository.findAll();
    assertThat(contentReferenceList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  void patchContentReferenceWithoutId() {
    ContentReference toUpdate = new ContentReference();

    Exception e = assertThrows(BadRequestAlertException.class, () -> contentReferenceResource.patchContentReference(contentReferenceMapper.toDTO(toUpdate)));
    assertThat(e.getMessage()).isEqualTo(EXPECTED_EXCEPTION_MESSAGE);
  }
}
