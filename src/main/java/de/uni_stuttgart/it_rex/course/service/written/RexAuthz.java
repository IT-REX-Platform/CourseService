package de.uni_stuttgart.it_rex.course.service.written;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.GrantedAuthority;

/**
 * These are the classes currently used: 
 * 
 * Authentication: org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
 * Principal: org.springframework.security.oauth2.jwt.Jwt 
 * Details: org.springframework.security.web.authentication.WebAuthenticationDetails
 * 
 * Roles need to start with "ROLE_"
 * 
 * further details tbd
 * 
 */

public class RexAuthz {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RexAuthz.class);

    /**
     * ----- PUBLIC API -----
     */

    /**
     * The roles avaiable in a course
     */
    public enum RexRole {
        ADMIN, LECTURER, STUDENT
    }

    /**
     * The roles avaiable in a course
     */
    public enum CourseRole {
        OWNER, MANAGER, PARTICIPANT
    }

    public static String getRexRoleString(RexRole role) {
        return makeStringFromTemplate(RexAuthzConstants.TEMPLATE_REX_ROLE, role);
    }

    /**
     * Returns a name given a template and course id and role.
     *
     * @param courseID the id of the course to fill in.
     * @param role     the role to make the name for.
     * @return a string containing the role name.
     */
    public static String getCourseRoleString(UUID courseID, CourseRole role) {
        return makeStringFromTemplate(RexAuthzConstants.TEMPLATE_COURSE_ROLE, courseID, role);
    }

    /**
     * Returns a name given a template and course id and role.
     *
     * @param courseID the id of the course to fill in.
     * @param role     the role to make the name for.
     * @return a string containing the role name.
     */
    public static String getCourseGroupString(UUID courseID, CourseRole role) {
        return makeStringFromTemplate(RexAuthzConstants.TEMPLATE_COURSE_GROUP, courseID, role);
    }

    /**
     * ----- PUBLIC USER API -----
     */

    public static UUID getUserId() {
        UUID uuid;
        try {
            uuid = UUID.fromString(getUserAuthn().getName());
        } catch (Exception e) {
            String msg = "Field sub in JWT does not contain a valid UUID";
            LOGGER.error(msg);
            throw new RexAuthzException(msg);
        }
        return uuid;
    }

    public static RexRole getRexRole() {
        Set<String> rexRoles = getUserAuthorities().stream().filter(o -> o.startsWith(RexAuthzConstants.MATCHER_ROLE_REX)).collect(Collectors.toSet());
        Set<RexRole> rexRolesStr = rexRoles.stream().map(RexAuthz::getRexRoleFromRoleString).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
        if (rexRolesStr.isEmpty()) {
            String msg = "User has no RexRole";
            LOGGER.error(msg);
            throw new RexAuthzException(msg);
        }
        if (rexRolesStr.size() > 1) {
            String msg = "User has more than one RexRole";
            LOGGER.error(msg);
            throw new RexAuthzException(msg);
        }
        return rexRolesStr.iterator().next();
    }

    public static boolean userHasRexRole(RexRole role) {
        return userHasAuthority(getRexRoleString(role));
    }

    public static boolean userIsRexAdmin() {
        return userHasRexRole(RexRole.ADMIN);
    }

    public static boolean userIsRexLecturer() {
        return userHasRexRole(RexRole.LECTURER);
    }

    public static boolean userIsRexStudent() {
        return userHasRexRole(RexRole.STUDENT);
    }

    public static Set<UUID> getCoursesOfUser() {
        Set<String> courseRoles = getUserAuthorities().stream()
                .filter(o -> o.startsWith(RexAuthzConstants.MATCHER_ROLE_COURSE)).collect(Collectors.toSet());
        return courseRoles.stream().map(RexAuthz::getCourseIdFromRoleString).filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toSet());
    }

    public static Map<UUID, CourseRole> getCoursesAndRolesOfUser() {
        Set<String> courseRoles = getUserAuthorities().stream()
                .filter(o -> o.startsWith(RexAuthzConstants.MATCHER_ROLE_COURSE)).collect(Collectors.toSet());
        return courseRoles.stream().map(RexAuthz::getCourseIdAndRoleFromRoleString).filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
    }

    public static boolean userHasCourseRole(UUID courseID, CourseRole role) {
        return userHasAuthority(getCourseRoleString(courseID, role));
    }

    public static boolean userIsCourseParticipant(UUID courseID) {
        return userHasCourseRole(courseID, CourseRole.PARTICIPANT);
    }

    public static boolean userIsCourseParticipantOrManagerOrOwner(UUID courseID) {
        return (userHasCourseRole(courseID, CourseRole.PARTICIPANT)
                || userHasCourseRole(courseID, CourseRole.MANAGER)
                || userHasCourseRole(courseID, CourseRole.OWNER));
    }

    public static boolean userIsCourseManagerOrOwner(UUID courseID) {
        return (userHasCourseRole(courseID, CourseRole.MANAGER) 
                || userHasCourseRole(courseID, CourseRole.OWNER));
    }

    public static boolean userIsCourseOwner(UUID courseID) {
        return userHasCourseRole(courseID, CourseRole.OWNER);
    }

    /**
     * ----- PRIVATE API -----
     */

    /**
     * The role a course participant can have.
     */
    private class RexAuthzConstants {

        public static final String TEMPLATE_REX_ROLE = "ROLE_ITREX_%s";

        public static final String MATCHER_ROLE_REX = "ROLE_ITREX_";

        /**
         * The string template for a role created by the course manager. The first
         * parameter is the role, the second one is the course ID.
         */
        public static final String TEMPLATE_COURSE_ROLE = "ROLE_COURSE_%2$s_%1$s";

        /**
         * The string template for a group created by the course manager. The first
         * parameter is the role, the second one is the course ID.
         */
        public static final String TEMPLATE_COURSE_GROUP = "COURSE_%2$s_%1$s";

        /**
         * The string to match relevant course roles
         */
        public static final String MATCHER_ROLE_COURSE = "ROLE_COURSE_";

        private RexAuthzConstants() {
        }
    }

    /**
     * Returns a name given a template and course id and role.
     *
     * @param template the format template which will be filled in.
     * @param courseID the id of the course to fill in.
     * @param role     the role to make the name for.
     * @return a string containing the filled-in name.
     */
    private static String makeStringFromTemplate(String template, UUID courseID, CourseRole role) {
        return String.format(template, role.toString(), courseID);
    }

    private static String makeStringFromTemplate(String template, RexRole role) {
        return String.format(template, role.toString());
    }

    private static Authentication getUserAuthn() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private static Optional<RexRole> getRexRoleFromRoleString(String roleString) {
        Optional<RexRole> role = Optional.empty();
        String[] roleComponents = roleString.split("_");
        if (roleComponents.length == 3) {
            try {
                role = Optional.of(RexRole.valueOf(roleComponents[2]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return role;
    }

    private static Optional<UUID> getCourseIdFromRoleString(String roleString) {
        Optional<UUID> uuid = Optional.empty();
        String[] roleComponents = roleString.split("_");
        if (roleComponents.length == 4) {
            try {
                uuid = Optional.of(UUID.fromString(roleComponents[2]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return uuid;
    }

    private static Optional<SimpleEntry<UUID, CourseRole>> getCourseIdAndRoleFromRoleString(String roleString) {
        Optional<SimpleEntry<UUID, CourseRole>> entry = Optional.empty();
        String[] roleComponents = roleString.split("_");
        if (roleComponents.length == 4) {
            try {
                UUID uuid = UUID.fromString(roleComponents[2]);
                CourseRole role = CourseRole.valueOf(roleComponents[3]);
                entry = Optional.of(new SimpleEntry<>(uuid, role));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entry;
    }

    private static Set<String> getUserAuthorities() {
        return getUserAuthn().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    private static boolean userHasAuthority(String authority) {
        return getUserAuthorities().contains(authority);
    }

}
