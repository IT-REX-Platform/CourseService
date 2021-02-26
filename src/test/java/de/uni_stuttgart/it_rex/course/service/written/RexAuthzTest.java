package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.COURSEROLE;
import de.uni_stuttgart.it_rex.course.domain.enumeration.REXROLE;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

@SpringBootTest
public class RexAuthzTest {

    @Autowired
    RexAuthz rexAuthz;

    UUID courseID = UUID.fromString("9119539c-3eb1-4392-8341-438a6b711312");

    @Test
    public void getRexRoleStringSuccessful() {
        REXROLE rexRole = REXROLE.ADMIN;
        String result = RexAuthz.getRexRoleString(rexRole);
        assertThat(result).isEqualTo(("ROLE_ITREX_ADMIN"));
    }

    @Test
    public void getCourseRoleStringSuccessful() {
        COURSEROLE courserole = COURSEROLE.OWNER;
        String result = RexAuthz.getCourseRoleString(courseID, courserole);
        assertThat(result).isEqualTo(("ROLE_COURSE_9119539c-3eb1-4392-8341-438a6b711312_OWNER"));
    }

    @Test
    public void getCourseGroupStringSuccessful() {
        COURSEROLE courserole = COURSEROLE.OWNER;
        String result = RexAuthz.getCourseGroupString(courseID, courserole);
        assertThat(result).isEqualTo(("COURSE_9119539c-3eb1-4392-8341-438a6b711312_OWNER"));
    }
}
