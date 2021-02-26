package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.domain.written.Course;
import de.uni_stuttgart.it_rex.course.service.written.CourseService;
import de.uni_stuttgart.it_rex.course.service.written.RexAuthz;
import de.uni_stuttgart.it_rex.course.service.written.RexAuthz.RexRole;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing {@link User}.
 */
@RestController
@RequestMapping("/api")
public class RexAuthzCheckResource {
    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(RexAuthzCheckResource.class);

    /**
     * Entity name.
     */
    private static final String ENTITY_NAME = "User";

    /**
     * Application name.
     */
    @Value("${jhipster.clientApp.name}")
    private String applicationName;


    @GetMapping("/rex-authz-check/")
    public ResponseEntity<Void> getUserInfo() {
        log.debug("REST request to get UserInfo");
        log.debug("getUserId: {}", RexAuthz.getUserId());
        log.debug("getCoursesOfUser: {}", RexAuthz.getCoursesOfUser());
        log.debug("getCoursesAndRolesOfUser: {}", RexAuthz.getCoursesAndRolesOfUser());
        log.debug("getRexRole: {}", RexAuthz.getRexRole());
        log.debug("userHasRexRole: {}", RexAuthz.userHasRexRole(RexRole.STUDENT));
        log.debug("userIsRexAdmin: {}", RexAuthz.userIsRexAdmin());
        log.debug("userIsRexLecturer: {}", RexAuthz.userIsRexLecturer());
        log.debug("userIsRexStudent: {}", RexAuthz.userIsRexStudent());

        return ResponseEntity.ok().build();
    }

}

