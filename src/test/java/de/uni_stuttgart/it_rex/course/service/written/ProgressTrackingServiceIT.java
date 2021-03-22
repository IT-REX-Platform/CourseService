package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.enumeration.ContentProgressState;
import de.uni_stuttgart.it_rex.course.domain.written.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written.ContentReference;
import de.uni_stuttgart.it_rex.course.domain.written.Course;
import de.uni_stuttgart.it_rex.course.domain.written.CourseProgressTracker;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.ContentProgressTrackerRepository;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseProgressTrackerRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentProgressTrackerDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseProgressTrackerDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.ContentReferenceMapper;
import de.uni_stuttgart.it_rex.course.utils.written.ChapterUtil;
import de.uni_stuttgart.it_rex.course.utils.written.ContentReferenceUtil;
import de.uni_stuttgart.it_rex.course.utils.written.CourseProgressTrackerUtil;
import de.uni_stuttgart.it_rex.course.utils.written.CourseUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CourseServiceApp.class,
    TestSecurityConfiguration.class})
class ProgressTrackingServiceIT {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID COURSE_TRACKER_ID = UUID.randomUUID();
    private static final UUID CONTENT_REFERENCE_ID = UUID.randomUUID();

    private static final float progress = 0.2f;

    @Autowired
    private ProgressTrackingService progressTrackingService;

    @Autowired
    private ContentReferenceMapper contentReferenceMapper;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private ContentReferenceRepository contentReferenceRepository;

    @Autowired
    private CourseProgressTrackerRepository courseProgressTrackerRepository;

    @Autowired
    private ContentProgressTrackerRepository contentProgressTrackerRepository;

    private static Course COURSE;

    private static Chapter CHAPTER;

    private static ContentReference CONTENT_REFERENCE;

    private static CourseProgressTracker COURSE_TRACKER;

    @BeforeEach
    void init(){
        COURSE = CourseUtil.createCourse();
        courseRepository.save(COURSE);
        CHAPTER = ChapterUtil.createChapter();
        CHAPTER.setCourse(COURSE);
        chapterRepository.save(CHAPTER);
        CONTENT_REFERENCE = ContentReferenceUtil.createContentReference();
        CONTENT_REFERENCE.setChapter(CHAPTER);
        contentReferenceRepository.save(CONTENT_REFERENCE);
        COURSE_TRACKER = CourseProgressTrackerUtil.createCourseProgressTracker();
        COURSE_TRACKER.setCourseId(COURSE.getId());
        COURSE_TRACKER.setUserId(USER_ID);
        courseProgressTrackerRepository.save(COURSE_TRACKER);
    }
    @AfterEach
    void cleanUp(){
        courseRepository.deleteAll();
        chapterRepository.deleteAll();
        contentReferenceRepository.deleteAll();
        courseProgressTrackerRepository.deleteAll();
        contentReferenceRepository.deleteAll();
    }

    @Test
    @Transactional
    void startContentProgressTracking() {
        ContentProgressTrackerDTO result
            = progressTrackingService.startContentProgressTracking(
                contentReferenceMapper.toDTO(CONTENT_REFERENCE), USER_ID, COURSE_TRACKER.getId());
        assertEquals(result.getState(), ContentProgressState.STARTED);
        assertEquals(result.getProgress(), 0.0f);
        assertEquals(result.getContentReference().getId(), CONTENT_REFERENCE.getId());
    }

    @Test
    void completeContentProgressTracker() {
        ContentProgressTrackerDTO contentProgressTracker
            = progressTrackingService.startContentProgressTracking(
                contentReferenceMapper.toDTO(CONTENT_REFERENCE), USER_ID, COURSE_TRACKER.getId());
        ContentProgressTrackerDTO result
            = progressTrackingService.completeContentProgressTracker(contentProgressTracker.getId());
        assertEquals(result.getState(), ContentProgressState.COMPLETED);
    }

    @Test
    void updateContentProgress() {
        ContentProgressTrackerDTO contentProgressTracker
            = progressTrackingService.startContentProgressTracking(
            contentReferenceMapper.toDTO(CONTENT_REFERENCE), USER_ID, COURSE_TRACKER.getId());
        ContentProgressTrackerDTO result
            = progressTrackingService.updateContentProgress(contentProgressTracker.getId(), progress);
        assertEquals(result.getProgress(), progress);
    }

    @Test
    void findContentProgressTracker() {
        ContentProgressTrackerDTO contentProgressTracker
            = progressTrackingService.startContentProgressTracking(
            contentReferenceMapper.toDTO(CONTENT_REFERENCE), USER_ID, COURSE_TRACKER.getId());
        Optional<ContentProgressTrackerDTO> result
            = progressTrackingService.findContentProgressTracker(contentProgressTracker.getId());
    }

    @Test
    void createCourseProgressTracker(){
        CourseProgressTrackerDTO result
            = progressTrackingService.findOrCreateCourseProgressTracker(COURSE.getId(), USER_ID);
        assertEquals(result.getCourseId(), COURSE.getId());
    }

    @Test
    void findCourseProgressTracker() {
        //Create Tracker
        CourseProgressTrackerDTO courseProgressTracker
            = progressTrackingService.findOrCreateCourseProgressTracker(COURSE.getId(), USER_ID);

        //Find Tracker
        CourseProgressTrackerDTO result
            = progressTrackingService.findOrCreateCourseProgressTracker(COURSE.getId(), USER_ID);
        assertEquals(result.getCourseId(), courseProgressTracker.getCourseId());
    }

    @Test
    void updateLastAccessedContentReference() {
        CourseProgressTrackerDTO courseProgressTracker
            = progressTrackingService.findOrCreateCourseProgressTracker(COURSE.getId(), USER_ID);
        CourseProgressTrackerDTO result
            = progressTrackingService.updateLastAccessedContentReference(
                courseProgressTracker.getId(), contentReferenceMapper.toDTO(CONTENT_REFERENCE));
        assertEquals(result.getLastContentReference(), CONTENT_REFERENCE);
    }
}
