package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.ChapterMapper;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link Chapter}.
 */
@Service
@Transactional
public class ChapterService {

  /**
   * Logger.
   */
  private static final Logger LOGGER
      = LoggerFactory.getLogger(ChapterService.class);

  /**
   * ContentReferenceService.
   */
  private final ContentReferenceService contentReferenceService;

  /**
   * Chapter Repository.
   */
  private final ChapterRepository chapterRepository;

  /**
   * Chapter mapper.
   */
  private final ChapterMapper chapterMapper;

  /**
   * Constructor.
   *
   * @param newChapterRepository
   * @param newChapterMapper
   */
  @Autowired
  public ChapterService(
      final ContentReferenceService newContentReferenceService,
      final ChapterRepository newChapterRepository,
      final ChapterMapper newChapterMapper) {
    this.contentReferenceService = newContentReferenceService;
    this.chapterRepository = newChapterRepository;
    this.chapterMapper = newChapterMapper;
  }

  /**
   * Save a Chapter.
   *
   * @param chapterDTO the Chapter to save.
   * @return the persisted Chapter.
   */
  @Transactional
  public ChapterDTO save(final ChapterDTO chapterDTO) {
    LOGGER.debug("Request to save Chapter : {}", chapterDTO);
    final Chapter chapter = chapterRepository
        .save(chapterMapper.toEntity(chapterDTO));
    return chapterMapper.toDTO(chapter);
  }

  /**
   * Get all the Chapters.
   *
   * @return the list of Chapters.
   */
  @Transactional(readOnly = true)
  public List<ChapterDTO> findAll() {
    LOGGER.debug("Request to get all Chapters");
    return chapterMapper.toDTO(chapterRepository.findAll());
  }

  /**
   * Get one Chapter by id.
   *
   * @param id the id of the Chapter.
   * @return the chapter.
   */
  @Transactional(readOnly = true)
  public Optional<ChapterDTO> findOne(final UUID id) {
    LOGGER.debug("Request to get Chapter : {}", id);
    return chapterMapper.toDTO(chapterRepository.findById(id));
  }

  /**
   * Delete the Chapter by id.
   *
   * @param id the id of the Chapter.
   */
  @Transactional
  public void delete(final UUID id) {
    LOGGER.debug("Request to delete Chapter : {}", id);

    final Optional<Chapter> toDeleteOptional = chapterRepository.findById(id);

    if (!toDeleteOptional.isPresent()) {
      return;
    }
    chapterRepository.deleteById(id);
  }

  /**
   * Update a Chapter without overwriting it.
   *
   * @param chapterDTO the Chapter that is used as an update.
   * @return the persisted Chapter.
   */
  @Transactional
  public ChapterDTO patch(final ChapterDTO chapterDTO) {
    LOGGER.debug("Request to update Chapter : {}", chapterDTO);
    Optional<Chapter> oldChapter =
        chapterRepository.findById(chapterDTO.getId());

    if (oldChapter.isPresent()) {
      Chapter oldChapterEntity = oldChapter.get();
      chapterMapper.updateChapterFromChapterDTO(chapterDTO, oldChapterEntity);
      return chapterMapper.toDTO(chapterRepository.save(oldChapterEntity));
    }
    return null;
  }

  /**
   * Adds a ContentReference to a Chapter by id.
   *
   * @param chapterId          the id of the Chapter
   * @param contentReferenceId the id of the ContentReference
   * @return the added ContentReference
   */
  @Transactional
  public ContentReferenceDTO addToChapter(final UUID chapterId,
                                          final UUID contentReferenceId) {
    final Chapter chapter = chapterRepository.findById(chapterId)
        .orElseThrow(() -> new BadRequestAlertException("Invalid id",
            Chapter.class.getName(), "idnotfound"));

    final ContentReferenceDTO contentReferenceDTO = new ContentReferenceDTO();
    contentReferenceDTO.setContentId(contentReferenceId);
    contentReferenceDTO.setChapterId(chapter.getId());

    return contentReferenceService.save(contentReferenceDTO);
  }
}
