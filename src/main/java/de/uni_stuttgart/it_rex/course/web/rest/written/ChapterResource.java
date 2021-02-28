package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.service.written.ChapterService;
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
 * REST controller for managing {@link ChapterDTO}.
 */
@RestController
@RequestMapping("/api")
public class ChapterResource {

  /**
   * Logger.
   */
  private final Logger log = LoggerFactory.getLogger(ChapterResource.class);

  /**
   * Entity name.
   */
  private static final String ENTITY_NAME = "Chapter";

  /**
   * Application name.
   */
  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  /**
   * Used chapter service.
   */
  private final ChapterService chapterService;

  /**
   * Constructor.
   *
   * @param newChapterService the chapter service.
   */
  public ChapterResource(final ChapterService newChapterService) {
    this.chapterService = newChapterService;
  }

  /**
   * {@code POST  /chapters} : Create a new chapter.
   *
   * @param chapterDTO the chapter to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)}
   * and with body the new chapter, or with status {@code 400 (Bad Request)}
   * if the chapter has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/chapters")
  public ResponseEntity<ChapterDTO> createChapter(
      @RequestBody final ChapterDTO chapterDTO)
      throws URISyntaxException {
    log.debug("REST request to save Chapter : {}", chapterDTO);
    if (chapterDTO.getId() != null) {
      throw new BadRequestAlertException("A new chapter cannot already "
          + "have an ID", ENTITY_NAME, "idexists");
    }
    ChapterDTO result = chapterService.save(chapterDTO);
    return ResponseEntity.created(new URI("/api/chapters/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(applicationName,
            true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT  /chapters} : Updates an existing chapter.
   *
   * @param chapterDTO the chapter to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
   * body the updated chapter,
   * or with status {@code 400 (Bad Request)} if the chapter is not valid,
   * or with status {@code 500 (Internal Server Error)} if the chapter couldn't
   * be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/chapters")
  public ResponseEntity<ChapterDTO> updateChapter(
      @RequestBody final ChapterDTO chapterDTO)
      throws URISyntaxException {
    log.debug("REST request to update Chapter : {}", chapterDTO);
    if (chapterDTO.getId() == null) {
      throw new BadRequestAlertException(
          "Invalid id", ENTITY_NAME, "idnull");
    }
    ChapterDTO result = chapterService.save(chapterDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName,
            true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET  /chapters/:id} : get the "id" chapter.
   *
   * @param id the id of the chapter to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)}
   * and with body the chapter, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/chapters/{id}")
  public ResponseEntity<ChapterDTO> getChapter(@PathVariable final UUID id) {
    log.debug("REST request to get Chapter : {}", id);
    Optional<ChapterDTO> chapter = chapterService.findOne(id);
    return ResponseUtil.wrapOrNotFound(chapter);
  }

  /**
   * {@code GET  /chapters} : get all the chapters.
   *
   * @return A list of all chapters.
   */
  @GetMapping("/chapters")
  public List<ChapterDTO> getAllChapters() {
    log.debug("REST request to get filtered Courses");
    return chapterService.findAll();
  }

  /**
   * {@code DELETE  /chapters/:id} : delete the "id" chapter.
   *
   * @param id the id of the chapter to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/chapters/{id}")
  public ResponseEntity<Void> deleteChapter(@PathVariable final UUID id) {
    log.debug("REST request to delete Course : {}", id);
    chapterService.delete(id);
    return ResponseEntity.noContent().headers(HeaderUtil
        .createEntityDeletionAlert(applicationName, true, ENTITY_NAME,
            id.toString())).build();
  }

  /**
   * {@code PATCH  /chapters} : Patches an existing chapter.
   *
   * @param chapterDTO the chapter to patch.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
   * body the updated chapter,
   * or with status {@code 400 (Bad Request)} if the chapter is not valid,
   * or with status {@code 500 (Internal Server Error)} if the chapter
   * couldn't be patched.
   */
  @PatchMapping("/chapters")
  public ResponseEntity<ChapterDTO> patchChapter(
      @RequestBody final ChapterDTO chapterDTO) {
    log.debug("REST request to patch Chapter : {}", chapterDTO);
    if (chapterDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME,
          "idnull");
    }
    ChapterDTO result = chapterService.patch(chapterDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName,
            true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }
}
