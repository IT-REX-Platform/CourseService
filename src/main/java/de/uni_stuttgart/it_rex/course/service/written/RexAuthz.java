package de.uni_stuttgart.it_rex.course.service.written;

import java.util.UUID;

public class RexAuthz {

    /**
     * The roles avaiable in a course
     */
    public enum CourseRole {
        OWNER,
        MANAGER,
        PARTICIPANT
    }

    /**
     * The role a course participant can have.
     */
    public class RexAuthzConstants {
        /**
         * The string template for a role created by the course manager.
         * The first parameter is the role, the second one is the course ID.
         */
        public static final String TEMPLATE_COURSE_ROLE = "ROLE_COURSE_%2$s_%1$s";

        /**
         * The string template for a group created by the course manager.
         * The first parameter is the role, the second one is the course ID.
         */
        public static final String TEMPLATE_COURSE_GROUP = "COURSE_%2$s_%1$s";

        private RexAuthzConstants() {
        }
    }

    /**
     * Returns a name given a template and course id and role.
     *
     * @param template the format template which will be filled in.
     * @param courseID the id of the course to fill in.
     * @param role the role to make the name for.
     * @return a string containing the filled-in name.
     */
    public static String makeNameForCourse(String template, UUID courseID, CourseRole role) {
        return String.format(template, role.toString(), courseID);
    }

    /**
     * Returns the id from a given, previously generated role or group name.
     *
     * @param roleOrGroupName the name to extract the course id from.
     * @return the course id in the given role or group name string.
     */
    public static Long getCourseIdFromName(String roleOrGroupName) {
        String[] splitStr = roleOrGroupName.split("_");
        if (splitStr.length != 4) {
            return -1L;
        }

        if (splitStr[0].equals("ROLE")) {
            return Long.parseLong(splitStr[4]);
        } else {
            return Long.parseLong(splitStr[2]);
        }
    }

}
