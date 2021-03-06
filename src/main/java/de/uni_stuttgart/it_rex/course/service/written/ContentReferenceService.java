package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.ContentReferenceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
   * ContentReference mapper.
   */
  private final ContentReferenceMapper contentReferenceMapper;

  /**
   * Constructor.
   *
   * @param newContentReferenceRepository
   * @param newContentReferenceMapper
   */
  @Autowired
  public ContentReferenceService(final ContentReferenceRepository newContentReferenceRepository,
                                 final ContentReferenceMapper newContentReferenceMapper) {
    this.contentReferenceRepository = newContentReferenceRepository;
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
}
