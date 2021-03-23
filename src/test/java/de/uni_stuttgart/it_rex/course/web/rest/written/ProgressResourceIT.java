package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.enumeration.ContentProgressState;
import de.uni_stuttgart.it_rex.course.domain.written.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written.ContentProgressTracker;
import de.uni_stuttgart.it_rex.course.domain.written.ContentReference;
import de.uni_stuttgart.it_rex.course.domain.written.Course;
import de.uni_stuttgart.it_rex.course.domain.written.CourseProgressTracker;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.ContentProgressTrackerRepository;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseProgressTrackerRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.mapper.written.ContentProgressTrackerMapper;
import de.uni_stuttgart.it_rex.course.utils.written.ChapterUtil;
import de.uni_stuttgart.it_rex.course.utils.written.ContentReferenceUtil;
import de.uni_stuttgart.it_rex.course.utils.written.CourseUtil;
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
    private ChapterRepository chapterRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContentTrackerMockMvc;

    @Autowired
    private MockMvc restCourseTrackerMockMvc;

    private Course course;

    private Chapter chapter1;
    private Chapter chapter2;
    private Chapter chapter3;

    private ContentProgressTracker contentProgressTracker1;
    private ContentProgressTracker contentProgressTracker2;
    private ContentProgressTracker contentProgressTracker3;

    private ContentReference contentReference1;
    private ContentReference contentReference2;
    private ContentReference contentReference3;

    private CourseProgressTracker courseProgressTracker;


    public static CourseProgressTracker createCourseProgressTracker (
        EntityManager em, ContentReference lastContentReference, UUID courseId){

        CourseProgressTracker courseProgressTracker = new CourseProgressTracker();
        courseProgressTracker.setCourseId(courseId);
        courseProgressTracker.setUserId(FIRST_USER_ID);
        //courseProgressTracker.setLastContentReference(lastContentReference);
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
    public static ContentReference createFirstContentReference (EntityManager em, Chapter chapter){
        ContentReference contentReference = ContentReferenceUtil.createContentReference();
        contentReference.setChapter(chapter);
        return contentReference;
    }
    public static ContentReference createSecondContentReference (EntityManager em, Chapter chapter){
        ContentReference contentReference = ContentReferenceUtil.createContentReference();
        contentReference.setChapter(chapter);
        return contentReference;
    }
    public static ContentReference createThirdContentReference (EntityManager em, Chapter chapter){
        ContentReference contentReference = ContentReferenceUtil.createContentReference();
        contentReference.setChapter(chapter);
        return contentReference;
    }

    @BeforeEach
    public void initTest(){
        course = CourseUtil.createCourse();
        courseRepository.saveAndFlush(course);
        chapter1 = ChapterUtil.createChapter();
        chapter1.setCourse(course);
        chapter2 = ChapterUtil.createChapter();
        chapter2.setCourse(course);
        chapter3 = ChapterUtil.createChapter();
        chapter3.setCourse(course);
        chapterRepository.saveAndFlush(chapter1);
        chapterRepository.saveAndFlush(chapter2);
        chapterRepository.saveAndFlush(chapter3);
        contentReference1 = createFirstContentReference(em, chapter1);
        contentReference2 = createSecondContentReference(em, chapter2);
        contentReference3 = createThirdContentReference(em, chapter3);
        contentReferenceRepository.saveAndFlush(contentReference1);
        contentReferenceRepository.saveAndFlush(contentReference2);
        courseProgressTracker = createCourseProgressTracker(em, contentReference1, course.getId());
        courseProgressTrackerRepository.saveAndFlush(courseProgressTracker);
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
    public void getContentProgress() throws Exception{
        //Initialize database
        contentProgressTrackerRepository.saveAndFlush(contentProgressTracker2);

        //Get the Content Progress
        restContentTrackerMockMvc.perform(get("/api/progress/content/{trackerId}", contentProgressTracker2.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(contentProgressTracker2.getId().toString()))
            //.andExpect(jsonPath("$.contentReference").value(contentReference2.toString()))
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

        restContentTrackerMockMvc.perform(put("/api/progress/content/{trackerId}/progress", contentProgressTracker1.getId()).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contentProgressTrackerMapper.toDTO(updatedTracker)))
            .param("progress", String.valueOf(updatedTracker.getProgress())))
            .andExpect(status().isOk());

        //Validate Tracker in DB
        List<ContentProgressTracker> trackerList = contentProgressTrackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeUpdate);
        ContentProgressTracker testTracker = trackerList.get(trackerList.size() - 1);
        assertThat(testTracker.getProgress()).isEqualTo(progress2);

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

        restContentTrackerMockMvc.perform(put("/api/progress/content/{trackerId}/complete", updatedTracker.getId()).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contentProgressTrackerMapper.toDTO(updatedTracker))))
            .andExpect(status().isOk());

        //Validate Tracker in DB
        List<ContentProgressTracker> trackerList = contentProgressTrackerRepository.findAll();
        assertThat(trackerList).hasSize(databaseSizeBeforeCreate);
        ContentProgressTracker testTracker = trackerList.get(trackerList.size() - 1);
        assertThat(testTracker.getState()).isEqualTo(COMPLETE);
    }
}
