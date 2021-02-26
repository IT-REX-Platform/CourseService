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
 */

public class RexAuthz {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RexAuthz.class);

    // ----- PUBLIC API ENUMS -----
    
    /**
     * The roles avaiable in ItRex.
     */
    public enum RexRole {
        ADMIN, LECTURER, STUDENT
    }
    
    /**
     * The roles avaiable in a course.
     */
    public enum CourseRole {
        OWNER, MANAGER, PARTICIPANT
    }

    // ----- PUBLIC STRING API -----
    
    /**
     * Returns the role string given a {@link RexRole}.
     *
     * @param role the role to make the role string for.
     * @return the role string.
     */
    public static String getRexRoleString(RexRole role) {
        return makeStringFromTemplate(RexAuthzConstants.TEMPLATE_REX_ROLE, role);
    }

    /**
     * Returns the role string given a course {@link UUID} and a {@link CourseRole}.
     *
     * @param courseId the {@link UUID} of the course.
     * @param role     the {@link CourseRole} to make the role string for.
     * @return the role string.
     */
    public static String getCourseRoleString(UUID courseId, CourseRole role) {
        return makeStringFromTemplate(RexAuthzConstants.TEMPLATE_COURSE_ROLE, courseId, role);
    }

    /**
     * Returns the group string given a course {@link UUID} and a {@link CourseRole}.
     *
     * @param courseId the {@link UUID} of the course.
     * @param role     the {@link CourseRole} to make the group string for.
     * @return the group string.
     */
    public static String getCourseGroupString(UUID courseId, CourseRole role) {
        return makeStringFromTemplate(RexAuthzConstants.TEMPLATE_COURSE_GROUP, courseId, role);
    }

    // ----- PUBLIC USER API -----

    /**
     * Returns the {@link UUID} of the current user.
     * 
     * @throws RexAuthzException if JWT does not contain a UUID in field "sub"
     * @return the user {@link UUID}.
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

    /**
     * Returns the {@link RexRole} of the current user.
     * 
     * @throws RexAuthzException if User has no RexRole.
     * @throws RexAuthzException if User has more than one RexRole.
     * @return the {@link RexRole}.
     */
    public static RexRole getRexRole() {
        Set<String> rexRoles = getUserAuthorities().stream()
                .filter(o -> o.startsWith(RexAuthzConstants.MATCHER_ROLE_REX)).collect(Collectors.toSet());
        Set<RexRole> rexRolesStr = rexRoles.stream().map(RexAuthz::getRexRoleFromRoleString).filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toSet());
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

    /**
     * Checks whether the current user has a specific {@link RexRole}.
     * 
     * @param role the {@link RexRole} to be checked.
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userHasRexRole(RexRole role) {
        return userHasAuthority(getRexRoleString(role));
    }

    /**
     * Checks whether the current user has the {@link RexRole} <code>RexRole.ADMIN</code>.
     * 
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userIsRexAdmin() {
        return userHasRexRole(RexRole.ADMIN);
    }

    /**
     * Checks whether the current user has the {@link RexRole} <code>RexRole.LECTURER</code>.
     * 
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userIsRexLecturer() {
        return userHasRexRole(RexRole.LECTURER);
    }

    /**
     * Checks whether the current user has the {@link RexRole} <code>RexRole.STUDENT</code>.
     * 
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userIsRexStudent() {
        return userHasRexRole(RexRole.STUDENT);
    }

    /**
     * Returns the Set of course {@link UUID}s in which the current user participates.
     * 
     * @return Set of course {@link UUID}.
     */
    public static Set<UUID> getCoursesOfUser() {
        Set<String> courseRoles = getUserAuthorities().stream()
                .filter(o -> o.startsWith(RexAuthzConstants.MATCHER_ROLE_COURSE)).collect(Collectors.toSet());
        return courseRoles.stream().map(RexAuthz::getCourseIdFromRoleString).filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toSet());
    }

    /**
     * Returns the Map of course {@link UUID}s and the related {@link CourseRole}s in which the current user participates.
     * 
     * @return Map of course {@link UUID} and {@link CourseRole}.
     */
    public static Map<UUID, CourseRole> getCoursesAndRolesOfUser() {
        Set<String> courseRoles = getUserAuthorities().stream()
                .filter(o -> o.startsWith(RexAuthzConstants.MATCHER_ROLE_COURSE)).collect(Collectors.toSet());
        return courseRoles.stream().map(RexAuthz::getCourseIdAndRoleFromRoleString).filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
    }

    /**
     * Checks whether the current user has a specific {@link CourseRole} for a given course {@link UUID}.
     * 
     * @param courseId the {@link UUID} of the course.
     * @param role the {@link CourseRole} to be checked.
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userHasCourseRole(UUID courseId, CourseRole role) {
        return userHasAuthority(getCourseRoleString(courseId, role));
    }

    /**
     * Checks whether the current user has the {@link CourseRole} <code>RexRole.PARTICIPANT</code> for a given course {@link UUID}.
     * 
     * @param courseId the {@link UUID} of the course.
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userIsCourseParticipant(UUID courseId) {
        return userHasCourseRole(courseId, CourseRole.PARTICIPANT);
    }

    /**
     * Checks whether the current user has one of the following {@link CourseRole}s for a given course {@link UUID}: <p>
     * - <code>RexRole.PARTICIPANT</code> <p>
     * - <code>RexRole.MANAGER</code> <p>
     * - <code>RexRole.OWNER</code> <p>
     * 
     * @param courseId the {@link UUID} of the course.
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userIsCourseParticipantOrManagerOrOwner(UUID courseId) {
        return (userHasCourseRole(courseId, CourseRole.PARTICIPANT)
                || userHasCourseRole(courseId, CourseRole.MANAGER)
                || userHasCourseRole(courseId, CourseRole.OWNER));
    }

    /**
     * Checks whether the current user has one of the following {@link CourseRole}s for a given course {@link UUID}: <p>
     * - <code>RexRole.MANAGER</code> <p>
     * - <code>RexRole.OWNER</code> <p>
     * 
     * @param courseId the {@link UUID} of the course.
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userIsCourseManagerOrOwner(UUID courseId) {
        return (userHasCourseRole(courseId, CourseRole.MANAGER) 
                || userHasCourseRole(courseId, CourseRole.OWNER));
    }

    /**
     * Checks whether the current user has one of the following {@link CourseRole}s for a given course {@link UUID}:<p>
     * - <code>RexRole.OWNER</code> <p>
     * 
     * @param courseId the {@link UUID} of the course.
     * @return <code>true</code>/<code>false</code>.
     */
    public static boolean userIsCourseOwner(UUID courseId) {
        return userHasCourseRole(courseId, CourseRole.OWNER);
    }

    // ----- PRIVATE API -----

    /**
     * Contants for internal use
     */
    private class RexAuthzConstants {
        /**
         * The string template for a {@link RexRole} role string
         */
        public static final String TEMPLATE_REX_ROLE = "ROLE_ITREX_%s";

        /**
         * The match string for a {@link RexRole} string
         */
        public static final String MATCHER_ROLE_REX = "ROLE_ITREX_";

        /**
         * The string template for a {@link CourseRole} role string <p>
         * - 1st param: {@link UUID} as string <p>
         * - 2nd param: {@link CourseRole} as string <p>
         */
        public static final String TEMPLATE_COURSE_ROLE = "ROLE_COURSE_%1$s_%2$s";

        /**
         * The string template for a {@link CourseRole} group string <p>
         * - 1st param: {@link UUID} as string <p>
         * - 2nd param: {@link CourseRole} as string <p>
         */
        public static final String TEMPLATE_COURSE_GROUP = "COURSE_%1$s_%2$s";

        /**
         * The match string for a {@link CourseRole} string
         */
        public static final String MATCHER_ROLE_COURSE = "ROLE_COURSE_";

        /**
         * Do not create an instance of this class
         */
        private RexAuthzConstants() {
        }
    }

    /**
     * Returns a string given a template, a course {@link UUID} and a {@link CourseRole}.
     *
     * @param template the format template which will be filled.
     * @param courseId the course {@link UUID}.
     * @param role     the {@link CourseRole}.
     * @return a string containing the filled-in parameters.
     */
    private static String makeStringFromTemplate(String template, UUID courseId, CourseRole role) {
        return String.format(template, courseId, role.toString());
    }

    /**
     * Returns a string given a template and a {@link RexRole}.
     *
     * @param template the format template which will be filled.
     * @param role     the {@link RexRole}.
     * @return a string containing the filled-in parameters.
     */
    private static String makeStringFromTemplate(String template, RexRole role) {
        return String.format(template, role.toString());
    }

    /**
     * Returns the {@link Authentication} of the current user.
     *
     * @return {@link Authentication}.
     */
    private static Authentication getUserAuthn() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Extracts the {@link RexRole} given a {@link RexRole} role string
     *
     * @param roleString a {@link RexRole} role string according to {@link RexAuthzConstants#TEMPLATE_REX_ROLE}
     * @return {@link RexRole} if extraction was succesful, empty {@link Optional} otherwise.
     */
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

    /**
     * Extracts the course {@link UUID} given a {@link CourseRole} role string
     *
     * @param roleString a {@link RexRole} role string according to {@link RexAuthzConstants#TEMPLATE_COURSE_ROLE}
     * @return course {@link UUID} if extraction was succesful, empty {@link Optional} otherwise.
     */
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

    /**
     * Extracts the course {@link UUID} with the related {@link CourseRole} given a {@link CourseRole} role string
     *
     * @param roleString a {@link RexRole} role string according to {@link RexAuthzConstants#TEMPLATE_COURSE_ROLE}
     * @return a {@link SimpleEntry} of {@link UUID} and {@link CourseRole} if extraction was succesful, empty {@link Optional} otherwise.
     */
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

    /**
     * Returns a Set of Strings representing the {@link GrantedAuthority}s of the current user
     *
     * @return the set of granted authorities
     */
    private static Set<String> getUserAuthorities() {
        return getUserAuthn().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    /**
     * Checks whether the current user as a specific authority
     *
     * @param authority a role string
     * @return <code>true</code>/<code>false</code>.
     */
    private static boolean userHasAuthority(String authority) {
        return getUserAuthorities().contains(authority);
    }

}
