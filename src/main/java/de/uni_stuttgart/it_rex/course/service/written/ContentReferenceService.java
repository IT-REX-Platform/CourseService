package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Organizable;
import de.uni_stuttgart.it_rex.course.domain.written_entities.TimePeriod;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.TimePeriodRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.ContentReferenceMapper;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link ContentReference}.
 */
@Service
@Transactional
public class ContentReferenceService {

  /**
   * Logger.
   */
  private static final Logger LOGGER
      = LoggerFactory.getLogger(ContentReferenceService.class);

  /**
   * ContentReference Repository.
   */
  private final ContentReferenceRepository contentReferenceRepository;

  /**
   * The TimePeriod Repository.
   */
  private final TimePeriodRepository timePeriodRepository;

  /**
   * The Chapter Repository.
   */
  private final ChapterRepository chapterRepository;

  /**
   * ContentReference mapper.
   */
  private final ContentReferenceMapper contentReferenceMapper;

  /**
   * Constructor.
   *
   * @param newContentReferenceRepository
   * @param newTimePeriodRepository
   * @param newContentReferenceMapper
   */
  @Autowired
  public ContentReferenceService(final ContentReferenceRepository newContentReferenceRepository,
                                 final TimePeriodRepository newTimePeriodRepository,
                                 final ChapterRepository newChapterRepository,
                                 final ContentReferenceMapper newContentReferenceMapper) {
    this.contentReferenceRepository = newContentReferenceRepository;
    this.timePeriodRepository = newTimePeriodRepository;
    this.chapterRepository = newChapterRepository;
    this.contentReferenceMapper = newContentReferenceMapper;
  }

  /**
   * Save a ContentReference.
   *
   * @param contentReferenceDTO the ContentReference to save.
   * @return the persisted ContentReference.
   */
  public ContentReferenceDTO save(final ContentReferenceDTO contentReferenceDTO) {
    LOGGER.debug("Request to save ContentReference : {}", contentReferenceDTO);
    final ContentReference contentReference = contentReferenceRepository
        .save(contentReferenceMapper.toEntity(contentReferenceDTO));
    return contentReferenceMapper.toDTO(contentReference);
  }

  /**
   * Get all the ContentReferences.
   *
   * @return the list of ContentReferences.
   */
  @Transactional(readOnly = true)
  public List<ContentReferenceDTO> findAll() {
    LOGGER.debug("Request to get all ContentReferences");
    return contentReferenceMapper.toDTO(contentReferenceRepository.findAll());
  }

  /**
   * Get one ContentReference by id.
   *
   * @param id the id of the ContentReference.
   * @return the contentReference.
   */
  @Transactional(readOnly = true)
  public Optional<ContentReferenceDTO> findOne(final UUID id) {
    LOGGER.debug("Request to get ContentReference : {}", id);
    return contentReferenceMapper.toDTO(contentReferenceRepository.findById(id));
  }

  /**
   * Delete the ContentReference by id.
   *
   * @param id the id of the ContentReference.
   */
  @Transactional
  public void delete(final UUID id) {
    LOGGER.debug("Request to delete ContentReference : {}", id);

    final Optional<ContentReference> toDeleteOptional = contentReferenceRepository.findById(id);

    if (!toDeleteOptional.isPresent()) {
      return;
    }
    contentReferenceRepository.deleteById(id);
  }

  /**
   * Update a ContentReference without overwriting it.
   *
   * @param contentReferenceDTO the ContentReference that is used as an update.
   * @return the persisted ContentReference.
   */
  @Transactional
  public ContentReferenceDTO patch(final ContentReferenceDTO contentReferenceDTO) {
    LOGGER.debug("Request to update ContentReference : {}", contentReferenceDTO);
    Optional<ContentReference> oldContentReference =
        contentReferenceRepository.findById(contentReferenceDTO.getId());

    if (oldContentReference.isPresent()) {
      ContentReference oldContentReferenceEntity = oldContentReference.get();
      contentReferenceMapper.updateContentReferenceFromContentReferenceDTO(contentReferenceDTO, oldContentReferenceEntity);
      return contentReferenceMapper.toDTO(contentReferenceRepository.save(oldContentReferenceEntity));
    }
    return null;
  }

  /**
   * Adds a ContentReference to a TimePeriod by id.
   *
   * @param contentReferenceId the id of the ContentReference
   * @param timePeriodId       the id of the TimePeriod
   * @return the added ContentReference
   */
  @Transactional
  public ContentReferenceDTO addToTimePeriod(final UUID contentReferenceId,
                                             final UUID timePeriodId) {
    return addToOrganizable(contentReferenceId,
        timePeriodId, timePeriodRepository, TimePeriod.class.getName());
  }

  /**
   * Adds a ContentReference to a Chapter by id.
   *
   * @param contentReferenceId the id of the ContentReference
   * @param chapterId          the id of the Chapter
   * @return the added ContentReference
   */
  @Transactional
  public ContentReferenceDTO addToChapter(final UUID contentReferenceId,
                                          final UUID chapterId) {
    return addToOrganizable(contentReferenceId,
        chapterId, chapterRepository, Chapter.class.getName());
  }

  private <ORGANIZABLE extends Organizable> ContentReferenceDTO addToOrganizable(
      final UUID contentReferenceId,
      final UUID organizableId,
      final JpaRepository<ORGANIZABLE, UUID> organizableRepository,
      final String organizableName) {
    final ContentReference contentReference = contentReferenceRepository
        .findById(contentReferenceId).orElseThrow(
            () -> new BadRequestAlertException("Invalid id",
                ContentReference.class.getName(), "idnotfound"));
    final ORGANIZABLE organizable = organizableRepository.findById(organizableId)
        .orElseThrow(() -> new BadRequestAlertException("Invalid id",
            organizableName, "idnotfound"));
    organizable.addContentReference(contentReference);
    return contentReferenceMapper.toDTO(contentReference);
  }

  /**
   * Removes a ContentReference from a TimePeriod by id.
   *
   * @param contentReferenceId the id of the ContentReference
   * @param timePeriodId       the id of the TimePeriod
   */
  @Transactional
  public void removeFromTimePeriod(final UUID contentReferenceId,
                                   final UUID timePeriodId) {
    removeFromOrganizable(contentReferenceId, timePeriodId,
        timePeriodRepository);
  }

  /**
   * Removes a ContentReference from a Chapter by id.
   *
   * @param contentReferenceId the id of the ContentReference
   * @param chapterId          the id of the Chapter
   */
  @Transactional
  public void removeFromChapter(final UUID contentReferenceId,
                                final UUID chapterId) {
    removeFromOrganizable(contentReferenceId, chapterId, chapterRepository);
  }

  private <ORGANIZABLE extends Organizable> void removeFromOrganizable(
      final UUID contentReferenceId,
      final UUID organizableId,
      final JpaRepository<ORGANIZABLE, UUID> organizableRepository) {

    final Optional<ContentReference> contentReferenceOptional
        = contentReferenceRepository.findById(contentReferenceId);
    final Optional<ORGANIZABLE> organizableOptional
        = organizableRepository.findById(organizableId);
    if (bothArePresent(contentReferenceOptional, organizableOptional)) {
      return;
    }
    final ContentReference contentReference = contentReferenceOptional.get();
    final ORGANIZABLE organizable = organizableOptional.get();
    organizable.removeContentReference(contentReference);
  }

  private <ORGANIZABLE extends Organizable> boolean bothArePresent(
      final Optional<ContentReference> contentReferenceOptional,
      final Optional<ORGANIZABLE> organizableOptional) {
    return !(contentReferenceOptional.isPresent()
        && organizableOptional.isPresent());
  }
}
