package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.service.written.dto.CourseDTO;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import de.uni_stuttgart.it_rex.course.web.rest.written.CourseResourceExtended;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {TestSecurityConfiguration.class})
public class CourseServiceExtendedTestIT {

  private static final String COURSE_NAME = "Herr der Ringe schauen";

  private static final String COURSE_DESCRIPTION = "Cool Course";

  private static final Integer COURSE_MAX_FOOD_SUM = Integer.MAX_VALUE;

  private static final UUID COURSE_ID = UUID.randomUUID();

  private static final String EXPECTED_EXCEPTION_MESSAGE = "Invalid id";

  @Autowired
  private CourseResourceExtended courseResourceExtended;

  @Test
  void patchNotExisting() {
    CourseDTO courseDTO = new CourseDTO();
    courseDTO.setId(COURSE_ID);
    courseDTO.setName(COURSE_NAME);
    courseDTO.setCourseDescription(COURSE_DESCRIPTION);
    courseDTO.setMaxFoodSum(COURSE_MAX_FOOD_SUM);

    BadRequestAlertException e = assertThrows(BadRequestAlertException.class,
        () -> courseResourceExtended.patchCourse(courseDTO));
    assertThat(e.getMessage()).isEqualTo(EXPECTED_EXCEPTION_MESSAGE);
  }
}
