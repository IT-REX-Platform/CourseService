package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.enumeration.ContentProgressState;
import de.uni_stuttgart.it_rex.course.domain.written_entities.*;
import de.uni_stuttgart.it_rex.course.repository.written.ContentProgressTrackerRepository;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseProgressTrackerRepository;
import de.uni_stuttgart.it_rex.course.service.mapper.written.ContentProgressTrackerMapper;
import de.uni_stuttgart.it_rex.course.web.rest.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProgressResource} REST controller.
 */
@SpringBootTest(classes = {CourseServiceApp.class, TestSecurityConfiguration.class})
@AutoConfigureMockMvc
@WithMockUser
class ProgressResourceIT {

    private static final UUID NON_EXISTING_ID = UUID.randomUUID();

    private static final UUID COURSE_ID = UUID.randomUUID();

    private static final UUID FIRST_CONTENT_ID = UUID.randomUUID();
    private static final UUID SECOND_CONTENT_ID = UUID.randomUUID();
    private static final UUID THIRD_CONTENT_ID = UUID.randomUUID();

    private static final UUID FIRST_USER_ID = UUID.randomUUID();
    private static final UUID SECOND_USER_ID = UUID.randomUUID();
    private static final UUID THIRD_USER_ID = UUID.randomUUID();

    private static final LocalDate FIRST_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate SECOND_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate THIRD_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate FIRST_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate SECOND_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate THIRD_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static float progress1 = 123;
    private static float progress2 = 456;
    private static float progress3 = 789;

    private static ContentProgressState COMPLETE = ContentProgressState.COMPLETED;

    private static final String EXPECTED_EXCEPTION_MESSAGE = "Invalid id";

    @Autowired
    private ContentReferenceRepository contentReferenceRepository;

    @Autowired
    private ContentProgressTrackerRepository contentProgressTrackerRepository;

    @Autowired
    private CourseProgressTrackerRepository courseProgressTrackerRepository;

    @Autowired
    private ContentProgressTrackerMapper contentProgressTrackerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContentTrackerMockMvc;

    @Autowired
    private MockMvc restCourseTrackerMockMvc;

    private ContentProgressTracker contentProgressTracker1;
    private ContentProgressTracker contentProgressTracker2;
    private ContentProgressTracker contentProgressTracker3;

    private ContentReference contentReference1;
    private ContentReference contentReference2;
    private ContentReference contentReference3;

    private CourseProgressTracker courseProgressTracker;


    public static CourseProgressTracker createCourseProgressTracker (
        EntityManager em, ContentReference lastContentReference){

        CourseProgressTracker courseProgressTracker = new CourseProgressTracker();
        courseProgressTracker.setCourseId(COURSE_ID);
        courseProgressTracker.setUserId(FIRST_USER_ID);
        courseProgressTracker.setLastContentReference(lastContentReference);
        return courseProgressTracker;
    }
    public static ContentProgressTracker createFirstContentProgressTracker (
        EntityManager em, ContentReference contentReference1, CourseProgressTracker courseProgressTracker){

        ContentProgressTracker contentProgressTracker
            = new ContentProgressTracker(FIRST_USER_ID, contentReference1, courseProgressTracker);
        contentProgressTracker.setProgress(progress1);
        return contentProgressTracker;
    }
    public static ContentProgressTracker createSecondContentProgressTracker (
        EntityManager em, ContentReference contentReference2, CourseProgressTracker courseProgressTracker){

        ContentProgressTracker contentProgressTracker
            = new ContentProgressTracker(SECOND_USER_ID, contentReference2, courseProgressTracker);
        contentProgressTracker.setProgress(progress2);
        return contentProgressTracker;
    }
    public static ContentProgressTracker createThirdContentProgressTracker (
        EntityManager em, ContentReference contentReference3, CourseProgressTracker courseProgressTracker){

        ContentProgressTracker contentProgressTracker
            = new ContentProgressTracker(THIRD_USER_ID, contentReference3, courseProgressTracker);
        contentProgressTracker.setProgress(progress3);
        return contentProgressTracker;
    }
    public static ContentReference createFirstContentReference (EntityManager em){
        ContentReference contentReference = new ContentReference();
        contentReference.setContentId(FIRST_CONTENT_ID);
        contentReference.setStartDate(FIRST_START_DATE);
        contentReference.setEndDate(FIRST_END_DATE);
        return contentReference;
    }
    public static ContentReference createSecondContentReference (EntityManager em){
        ContentReference contentReference = new ContentReference();
        contentReference.setContentId(SECOND_CONTENT_ID);
        contentReference.setStartDate(SECOND_START_DATE);
        contentReference.setEndDate(SECOND_END_DATE);
        return contentReference;
    }
    public static ContentReference createThirdContentReference (EntityManager em){
        ContentReference contentReference = new ContentReference();
        contentReference.setContentId(THIRD_CONTENT_ID);
        contentReference.setStartDate(THIRD_START_DATE);
        contentReference.setEndDate(THIRD_END_DATE);
        return contentReference;
    }

    @BeforeEach
    public void initTest(){
        contentReference1 = createFirstContentReference(em);
        contentReference2 = createSecondContentReference(em);
        contentReference3 = createThirdContentReference(em);
        courseProgressTracker = createCourseProgressTracker(em, contentReference1);
        contentProgressTracker1 = createFirstContentProgressTracker(em, contentReference1, courseProgressTracker);
        contentProgressTracker2 = createSecondContentProgressTracker(em, contentReference2, courseProgressTracker);
        contentProgressTracker3 = createThirdContentProgressTracker(em, contentReference3, courseProgressTracker);
    }

    @AfterEach
    public void cleanup(){
        courseProgressTrackerRepository.deleteAll();
        contentProgressTrackerRepository.deleteAll();
        contentReferenceRepository.deleteAll();
    }

    @Test
    @Transactional
    public void createContentProgress() throws Exception{
        int databaseSizeBeforeCreate = contentProgressTrackerRepository.findAll().size();
        //Create Content Tracker
        restContentTrackerMockMvc.perform(post("api/progress/content/").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contentProgressTrackerMapper
                .toDTO(contentProgressTracker1))))
            .andExpect(status().isCreated());

        //Validate Content Tracker in Database
        List<ContentProgressTracker> trackerList = contentProgressTrackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeCreate + 1);
        ContentProgressTracker testTracker = trackerList.get(trackerList.size() - 1);
        assertEquals(contentReference1, testTracker.getContentReference());
        assertEquals(FIRST_USER_ID, testTracker.getUserId());
        assertEquals(ContentProgressState.STARTED, testTracker.getState());
        assertEquals(progress1, testTracker.getProgress());
        assertEquals(courseProgressTracker, testTracker.getCourseProgressTracker());
    }

    @Test
    @Transactional
    public void createContentProgressWithExistingId() throws Exception{
        int databaseSizeBeforeCreate = contentProgressTrackerRepository.findAll().size();

        //Create ContentTracker with existing Id
        contentProgressTracker2.setId(UUID.randomUUID());

        restContentTrackerMockMvc.perform(post("api/progress/content/").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(contentProgressTracker2)))
        .andExpect(status().isBadRequest());

        // Validate the Tracker in the database
        List<ContentProgressTracker> trackerList = contentProgressTrackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getContentProgress() throws Exception{
        //Initialize database
        contentProgressTrackerRepository.saveAndFlush(contentProgressTracker2);

        //Get the Content Progress
        restContentTrackerMockMvc.perform(get("api/progress/content/{trackerId}", contentProgressTracker2.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(contentProgressTracker2.getId().toString()))
            .andExpect(jsonPath("$.contentReference").value(contentReference2.toString()))
            .andExpect(jsonPath("$.progress").value(String.valueOf(progress2)));
    }

    @Test
    @Transactional
    public void getContentProgressWithoutExistingId() throws Exception{
        // Get the tracker
        restContentTrackerMockMvc.perform(get("/api/progress/content/{trackerId}", NON_EXISTING_ID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContentProgress() throws Exception{
        contentProgressTrackerRepository.saveAndFlush(contentProgressTracker1);

        int databaseSizeBeforeUpdate = contentProgressTrackerRepository.findAll().size();

        //ContentTracker to update
        ContentProgressTracker updatedTracker
            = contentProgressTrackerRepository.findById(contentProgressTracker1.getId()).get();
        // Disconnect from session so that the updates on updatedTracker are not directly saved in db
        em.detach(updatedTracker);

        updatedTracker.setProgress(progress2);

        restContentTrackerMockMvc.perform(put("api/progress/content/{trackerId}").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contentProgressTrackerMapper.toDTO(updatedTracker))))
            .andExpect(status().isOk());

        //Validate Tracker in DB
        List<ContentProgressTracker> trackerList = contentProgressTrackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeUpdate);
        ContentProgressTracker testTracker = trackerList.get(trackerList.size() - 1);
        assertThat(testTracker.getProgress()).isEqualTo(progress2);

    }

    @Test
    @Transactional
    public void updateNonExistingContentProgress() throws Exception{
        int databaseSizeBeforeUpdate = contentProgressTrackerRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentTrackerMockMvc.perform(put("/api/progress/content").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contentProgressTracker3)))
            .andExpect(status().isBadRequest());

        // Validate the Tracker in the database
        List<ContentProgressTracker> trackerList = contentProgressTrackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void setContentStateComplete() throws Exception{
        contentProgressTrackerRepository.saveAndFlush(contentProgressTracker1);

        int databaseSizeBeforeCreate = contentProgressTrackerRepository.findAll().size();

        //ContentTracker to update
        ContentProgressTracker updatedTracker
            = contentProgressTrackerRepository.findById(contentProgressTracker1.getId()).get();
        // Disconnect from session so that the updates on updatedTracker are not directly saved in db
        em.detach(updatedTracker);

        updatedTracker.complete();

        restContentTrackerMockMvc.perform(put("api/progress/content/{trackerId}").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contentProgressTrackerMapper.toDTO(updatedTracker))))
            .andExpect(status().isOk());

        //Validate Tracker in DB
        List<ContentProgressTracker> trackerList = contentProgressTrackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeCreate);
        ContentProgressTracker testTracker = trackerList.get(trackerList.size() - 1);
        assertThat(testTracker.getState()).isEqualTo(COMPLETE);
    }

    @Test
    @Transactional
    public void getCourseProgress() throws Exception{
        //Initialize database
        courseProgressTrackerRepository.saveAndFlush(courseProgressTracker);

        //Get the Course Progress
        //Missing Set of Content Trackers
        restCourseTrackerMockMvc.perform(get("api/progress/courses/{courseId}", courseProgressTracker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(courseProgressTracker.getId().toString()))
            .andExpect(jsonPath("$.courseId").value(COURSE_ID.toString()))
            .andExpect(jsonPath("$.lastContentReference").value(contentReference1.toString()));
    }
}
