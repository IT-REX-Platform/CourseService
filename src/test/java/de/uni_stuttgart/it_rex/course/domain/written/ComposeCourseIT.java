package de.uni_stuttgart.it_rex.course.domain.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.repository.written.TimePeriodRepository;
import de.uni_stuttgart.it_rex.course.utils.written.ChapterUtil;
import de.uni_stuttgart.it_rex.course.utils.written.ContentReferenceUtil;
import de.uni_stuttgart.it_rex.course.utils.written.CourseUtil;
import de.uni_stuttgart.it_rex.course.utils.written.TimePeriodUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {CourseServiceApp.class, TestSecurityConfiguration.class})
public class ComposeCourseIT {

  private static final int THE_CHOSEN_INDEX = 12;
  private static final int THE_NUMBER_OF_CHAPTERS = 20;
  private static final int THE_NUMBER_OF_TIME_PERIODS = 26;
  private static final int THE_NUMBER_OF_CONTENT_REFERENCES = 19;

  @Autowired
  private ChapterRepository chapterRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private TimePeriodRepository timePeriodRepository;

  @Autowired
  private ContentReferenceRepository contentReferenceRepository;

  @AfterEach
  public void cleanup() {
    contentReferenceRepository.deleteAll();
    chapterRepository.deleteAll();
    timePeriodRepository.deleteAll();
    courseRepository.deleteAll();
  }

  @Test
  @Transactional
  void createCourseWithAll() {
    final Course course = CourseUtil.createCourse();
    courseRepository.saveAndFlush(course);

    final List<Chapter> chapters = ChapterUtil.createChapters(THE_NUMBER_OF_CHAPTERS);
    course.setChapters(chapters);
    chapterRepository.saveAll(chapters);

    final List<TimePeriod> timePeriods = TimePeriodUtil.createTimePeriods(THE_NUMBER_OF_TIME_PERIODS);
    course.setTimePeriods(timePeriods);
    timePeriodRepository.saveAll(timePeriods);

    final List<ContentReference> contentReferences = ContentReferenceUtil.createContentReferenceList(THE_NUMBER_OF_CONTENT_REFERENCES);
    chapters.get(THE_CHOSEN_INDEX).setContentReferences(contentReferences);
    timePeriods.get(THE_CHOSEN_INDEX).setContentReferences(contentReferences);
    contentReferenceRepository.saveAll(contentReferences);

    courseRepository.saveAndFlush(course);

    final Course resultCourse = courseRepository.getOne(course.getId());

    assertEquals(course, resultCourse);

    assertThat(resultCourse.getChapters(), containsInAnyOrder(course.getChapters().toArray()));
    assertThat(resultCourse.getChapters().get(THE_CHOSEN_INDEX).getContentReferences(),
      containsInAnyOrder(course.getChapters().get(THE_CHOSEN_INDEX).getContentReferences().toArray()));

    assertThat(resultCourse.getTimePeriods(), containsInAnyOrder(course.getTimePeriods().toArray()));
    assertThat(resultCourse.getTimePeriods().get(THE_CHOSEN_INDEX).getContentReferences(),
      containsInAnyOrder(course.getTimePeriods().get(THE_CHOSEN_INDEX).getContentReferences().toArray()));
  }
}
