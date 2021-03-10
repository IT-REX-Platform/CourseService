package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.TimePeriodDTO;
import de.uni_stuttgart.it_rex.course.service.written.TimePeriodService;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing {@link TimePeriodDTO}.
 */
@RestController
@RequestMapping("/api")
public class TimePeriodResource {

  /**
   * Logger.
   */
  private final Logger log = LoggerFactory.getLogger(TimePeriodResource.class);

  /**
   * Entity name.
   */
  private static final String ENTITY_NAME = "TimePeriod";

  /**
   * Application name.
   */
  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  /**
   * Used TimePeriod service.
   */
  private final TimePeriodService timePeriodService;

  /**
   * Constructor.
   *
   * @param newTimePeriodService the TimePeriod service.
   */
  public TimePeriodResource(final TimePeriodService newTimePeriodService) {
    this.timePeriodService = newTimePeriodService;
  }

  /**
   * {@code POST  /timeperiods} : Create a new TimePeriod.
   *
   * @param timePeriodDTO the TimePeriod to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
   * with body the new TimePeriod, or with status {@code 400 (Bad Request)} if
   * the TimePeriod has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/timeperiods")
  public ResponseEntity<TimePeriodDTO> createTimePeriod(
      @RequestBody final TimePeriodDTO timePeriodDTO)
      throws URISyntaxException {
    log.debug("REST request to save TimePeriod : {}", timePeriodDTO);
    if (timePeriodDTO.getId() != null) {
      throw new BadRequestAlertException("A new TimePeriod cannot already "
          + "have an ID", ENTITY_NAME, "idexists");
    }
    TimePeriodDTO result = timePeriodService.save(timePeriodDTO);
    return ResponseEntity.created(new URI("/api/timeperiods/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(applicationName,
            true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET  /timeperiods/:id} : get the "id" TimePeriod.
   *
   * @param id the id of the TimePeriod to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
   * body with the TimePeriod, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/timeperiods/{id}")
  public ResponseEntity<TimePeriodDTO> getTimePeriod(
      @PathVariable final UUID id) {
    log.debug("REST request to get TimePeriod : {}", id);
    Optional<TimePeriodDTO> timePeriod = timePeriodService.findOne(id);
    return ResponseUtil.wrapOrNotFound(timePeriod);
  }

  /**
   * {@code GET  /timeperiods} : get all the TimePeriod.
   *
   * @return A list of all TimePeriods.
   */
  @GetMapping("/timeperiods")
  public List<TimePeriodDTO> getAllTimePeriods() {
    log.debug("REST request to get filtered Courses");
    return timePeriodService.findAll();
  }

  /**
   * {@code DELETE  /timeperiods/:id} : delete the "id" TimePeriod.
   *
   * @param id the id of the TimePeriod to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/timeperiods/{id}")
  public ResponseEntity<Void> deleteTimePeriod(@PathVariable final UUID id) {
    log.debug("REST request to delete Course : {}", id);
    timePeriodService.delete(id);
    return ResponseEntity.noContent().headers(HeaderUtil
        .createEntityDeletionAlert(applicationName, true,
            ENTITY_NAME,
            id.toString())).build();
  }

  /**
   * {@code PATCH  /timeperiods} : Patches an existing TimePeriod.
   *
   * @param timePeriodDTO the TimePeriod to patch.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
   * body the updated TimePeriod, or with status {@code 400 (Bad Request)} if
   * the TimePeriod is not valid, or with status {@code 500 (Internal Server
   * Error)} if the TimePeriod couldn't be patched.
   */
  @PatchMapping("/timeperiods")
  public ResponseEntity<TimePeriodDTO> patchTimePeriod(
      @RequestBody final TimePeriodDTO timePeriodDTO) {
    log.debug("REST request to patch TimePeriod : {}", timePeriodDTO);
    if (timePeriodDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME,
          "idnull");
    }
    TimePeriodDTO result = timePeriodService.patch(timePeriodDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName,
            true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }
}
