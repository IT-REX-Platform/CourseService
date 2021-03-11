package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.domain.written_entities.TimePeriod;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.repository.written.TimePeriodRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.TimePeriodDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.TimePeriodMapper;
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
 * Integration tests for the {@link TimePeriodResource} REST controller.
 */
@SpringBootTest(classes = {CourseServiceApp.class, TestSecurityConfiguration.class})
@AutoConfigureMockMvc
@WithMockUser
public class TimePeriodResourceIT {

  private static final UUID NON_EXISTING_ID = UUID.randomUUID();

  private static final String EXPECTED_EXCEPTION_MESSAGE = "Invalid id";

  @Autowired
  private TimePeriodRepository timePeriodRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private TimePeriodResource timePeriodResource;

  @Autowired
  private TimePeriodMapper timePeriodMapper;

  @Autowired
  private ContentReferenceRepository contentReferenceRepository;

  @Autowired
  private MockMvc restTimePeriodMockMvc;

  private static Course THE_COURSE;

  @BeforeEach
  void init() {
    THE_COURSE = CourseUtil.createCourse();
    courseRepository.save(THE_COURSE);
  }

  @AfterEach
  public void cleanup() {
    timePeriodRepository.deleteAll();
    contentReferenceRepository.deleteAll();
    courseRepository.deleteAll();
  }

  @Test
  @Transactional
  public void createTimePeriod() throws Exception {
    final int databaseSizeBeforeCreate = timePeriodRepository.findAll().size();
    final TimePeriodDTO timePeriodDTO = TimePeriodUtil.createTimePeriodDTO();
    timePeriodDTO.setCourseId(THE_COURSE.getId());

    // Create the TimePeriod
    restTimePeriodMockMvc.perform(post("/api/timeperiods").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(TestUtil.convertObjectToJsonBytes(timePeriodDTO)))
      .andExpect(status().isCreated());

    // Validate the TimePeriod in the database
    assertThat(timePeriodRepository.findAll()).hasSize(databaseSizeBeforeCreate + 1);
  }

  @Test
  @Transactional
  public void createTimePeriodWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = timePeriodRepository.findAll().size();

    // Create the TimePeriod with an existing ID
    final TimePeriodDTO timePeriodDTO2 = TimePeriodUtil.createTimePeriodDTO();
    timePeriodDTO2.setCourseId(THE_COURSE.getId());
    timePeriodDTO2.setId(UUID.randomUUID());

    // An entity with an existing ID cannot be created, so this API call must fail
    restTimePeriodMockMvc.perform(post("/api/timeperiods").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(TestUtil.convertObjectToJsonBytes(timePeriodDTO2)))
      .andExpect(status().isBadRequest());

    // Validate the TimePeriod in the database
    List<TimePeriod> timePeriodList = timePeriodRepository.findAll();
    assertThat(timePeriodList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllTimePeriods() throws Exception {
    // Initialize the database
    final TimePeriod timePeriod = TimePeriodUtil.createTimePeriod();
    timePeriod.setCourse(THE_COURSE);

    // Get all the TimePeriod
    restTimePeriodMockMvc.perform(get("/api/timeperiods?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(timePeriod.getId().toString())))
      .andExpect(jsonPath("$.[*].startDate").value(hasItem(timePeriod.getStartDate().toString())))
      .andExpect(jsonPath("$.[*].endDate").value(hasItem(timePeriod.getEndDate().toString())))
      .andExpect(jsonPath("$.[*].courseId").value(hasItem(timePeriod.getCourse().getId().toString())));
  }

  @Test
  @Transactional
  public void getTimePeriod() throws Exception {
    // Initialize the database
    TimePeriod timePeriod = TimePeriodUtil.createTimePeriod();
    timePeriod.setCourse(THE_COURSE);
    timePeriodRepository.saveAndFlush(timePeriod);

    // Get the TimePeriod
    restTimePeriodMockMvc.perform(get("/api/timeperiods/{id}", timePeriod.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(timePeriod.getId().toString()))
      .andExpect(jsonPath("$.startDate").value(timePeriod.getStartDate().toString()))
      .andExpect(jsonPath("$.endDate").value(timePeriod.getEndDate().toString()))
      .andExpect(jsonPath("$.courseId").value(timePeriod.getCourse().getId().toString()));
  }

  @Test
  @Transactional
  public void getNonExistingTimePeriod() throws Exception {
    // Get the timePeriod
    restTimePeriodMockMvc.perform(get("/api/timeperiods/{id}", NON_EXISTING_ID))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void patchNonExistingTimePeriod() throws Exception {
    int databaseSizeBeforeUpdate = timePeriodRepository.findAll().size();
    final TimePeriodDTO timePeriodDTO = TimePeriodUtil.createTimePeriodDTO();
    timePeriodDTO.setCourseId(THE_COURSE.getId());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restTimePeriodMockMvc.perform(patch("/api/timeperiods").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(TestUtil.convertObjectToJsonBytes(timePeriodDTO)))
      .andExpect(status().isBadRequest());

    // Validate the TimePeriod in the database
    List<TimePeriod> timePeriodList = timePeriodRepository.findAll();
    assertThat(timePeriodList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  public void deleteTimePeriod() throws Exception {
    // Initialize the database
    final TimePeriod timePeriod = TimePeriodUtil.createTimePeriod();
    timePeriod.setCourse(THE_COURSE);
    timePeriodRepository.saveAndFlush(timePeriod);

    final int databaseSizeBeforeDelete = courseRepository.findAll().size();

    // Delete the course
    restTimePeriodMockMvc.perform(delete("/api/timeperiods/{id}", timePeriod.getId()).with(csrf())
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    final List<TimePeriod> timePeriodList = timePeriodRepository.findAll();
    assertThat(timePeriodList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  void patchTimePeriodWithoutId() {
    TimePeriod toUpdate = new TimePeriod();

    Exception e = assertThrows(BadRequestAlertException.class, () -> timePeriodResource.patchTimePeriod(timePeriodMapper.toDTO(toUpdate)));
    assertThat(e.getMessage()).isEqualTo(EXPECTED_EXCEPTION_MESSAGE);
  }
}
