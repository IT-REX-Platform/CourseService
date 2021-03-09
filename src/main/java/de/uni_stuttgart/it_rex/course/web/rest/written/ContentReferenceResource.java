package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import de.uni_stuttgart.it_rex.course.service.written.ContentReferenceService;
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
 * REST controller for managing {@link ContentReferenceDTO}.
 */
@RestController
@RequestMapping("/api")
public class ContentReferenceResource {

  /**
   * Logger.
   */
  private final Logger log
      = LoggerFactory.getLogger(ContentReferenceResource.class);

  /**
   * Entity name.
   */
  private static final String ENTITY_NAME = "ContentReference";

  /**
   * Application name.
   */
  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  /**
   * Used contentReference service.
   */
  private final ContentReferenceService contentReferenceService;

  /**
   * Constructor.
   *
   * @param newContentReferenceService the contentReference service.
   */
  public ContentReferenceResource(
      final ContentReferenceService newContentReferenceService) {
    this.contentReferenceService = newContentReferenceService;
  }

  /**
   * {@code POST  /contentreferences} : Create a new contentReference.
   *
   * @param contentReferenceDTO the contentReference to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
   * with body the new contentReference, or with status {@code 400 (Bad
   * Request)} if the contentReference has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/contentreferences")
  public ResponseEntity<ContentReferenceDTO> createContentReference(
      @RequestBody final ContentReferenceDTO contentReferenceDTO)
      throws URISyntaxException {
    log.debug("REST request to save ContentReference : {}",
        contentReferenceDTO);
    if (contentReferenceDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new ContentReference cannot already "
              + "have an ID", ENTITY_NAME, "idexists");
    }
    ContentReferenceDTO result = contentReferenceService
        .save(contentReferenceDTO);
    return ResponseEntity.created(new URI("/api/contentreferences/"
        + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(applicationName,
            true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT  /contentreferences} : Updates an existing contentReference.
   *
   * @param contentReferenceDTO the contentReference to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
   * body the updated contentReference, or with status {@code 400 (Bad
   * Request)} if the contentReference is not valid, or with status {@code 500
   * (Internal Server Error)} if the contentReference couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/contentreferences")
  public ResponseEntity<ContentReferenceDTO> updateContentReference(
      @RequestBody final ContentReferenceDTO contentReferenceDTO)
      throws URISyntaxException {
    log.debug("REST request to update ContentReference : {}",
        contentReferenceDTO);
    if (contentReferenceDTO.getId() == null) {
      throw new BadRequestAlertException(
          "Invalid id", ENTITY_NAME, "idnull");
    }
    ContentReferenceDTO result = contentReferenceService
        .save(contentReferenceDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName,
            true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET  /contentreferences/:id} : get the "id" ContentReference.
   *
   * @param id the id of the ContentReference to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
   * body the ContentReference, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/contentreferences/{id}")
  public ResponseEntity<ContentReferenceDTO> getContentReference(
      @PathVariable final UUID id) {
    log.debug("REST request to get ContentReference : {}", id);
    Optional<ContentReferenceDTO> contentReference =
        contentReferenceService.findOne(id);
    return ResponseUtil.wrapOrNotFound(contentReference);
  }

  /**
   * {@code GET  /contentreferences} : get all the ContentReferences.
   *
   * @return A list of all ContentReferences.
   */
  @GetMapping("/contentreferences")
  public List<ContentReferenceDTO> getAllContentReferences() {
    log.debug("REST request to get filtered ContentReferences");
    return contentReferenceService.findAll();
  }

  /**
   * {@code DELETE  /contentreferences/:id} : delete the "id"
   * ContentReference.
   *
   * @param id the id of the ContentReference to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/contentreferences/{id}")
  public ResponseEntity<Void> deleteContentReference(
      @PathVariable final UUID id) {
    log.debug("REST request to delete Course : {}", id);
    contentReferenceService.delete(id);
    return ResponseEntity.noContent().headers(HeaderUtil
        .createEntityDeletionAlert(applicationName, true,
            ENTITY_NAME,
            id.toString())).build();
  }

  /**
   * {@code PATCH  /contentreferences} : Patches an existing
   * ContentReference.
   *
   * @param contentReferenceDTO the ContentReference to patch.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
   * body the updated ContentReference, or with status {@code 400 (Bad
   * Request)} if the ContentReference is not valid, or with status {@code 500
   * (Internal Server Error)} if the ContentReference couldn't be patched.
   */
  @PatchMapping("/contentreferences")
  public ResponseEntity<ContentReferenceDTO> patchContentReference(
      @RequestBody final ContentReferenceDTO contentReferenceDTO) {
    log.debug("REST request to patch ContentReference : {}",
        contentReferenceDTO);
    if (contentReferenceDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME,
          "idnull");
    }
    ContentReferenceDTO result = contentReferenceService
        .patch(contentReferenceDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName,
            true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }
}
