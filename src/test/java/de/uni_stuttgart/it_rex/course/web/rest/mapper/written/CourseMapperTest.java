package de.uni_stuttgart.it_rex.course.web.rest.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.service.mapper.written.CourseMapper;
import de.uni_stuttgart.it_rex.course.service.mapper.written.CourseMapperImpl;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CourseMapperTest {
  private static CourseMapper COURSEMAPPER = new CourseMapperImpl();

  private static final String OLD_NAME = "Herr der Ringe schauen";
  private static final String OLD_DESCRIPTION = "Cool Course";
  private static final Integer OLD_MAX_FOOD_SUM = Integer.MAX_VALUE;
  private static final UUID OLD_ID = UUID.randomUUID();

  private static final String NEW_DESCRIPTION = "Really cool Course";
  private static final PUBLISHSTATE NEW_PUBLISHED_STATE = PUBLISHSTATE.PUBLISHED;
  private static final UUID NEW_ID = UUID.randomUUID();

  @Test
  void mapping() {
    Course toUpdate = new Course();
    toUpdate.setId(OLD_ID);
    toUpdate.setName(OLD_NAME);
    toUpdate.setCourseDescription(OLD_DESCRIPTION);
    toUpdate.setMaxFoodSum(OLD_MAX_FOOD_SUM);

    Course update = new Course();
    update.setId(NEW_ID);
    update.setCourseDescription(NEW_DESCRIPTION);
    update.setPublishState(NEW_PUBLISHED_STATE);

    COURSEMAPPER.updateCourseFromCourse(update, toUpdate);

    Course expected = new Course();
    expected.setId(NEW_ID);
    expected.setName(OLD_NAME);
    expected.setCourseDescription(NEW_DESCRIPTION);
    expected.setMaxFoodSum(OLD_MAX_FOOD_SUM);
    expected.setPublishState(NEW_PUBLISHED_STATE);

    assertThat(toUpdate).isEqualTo(expected);
  }
}
