package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.service.mapper.written.ChapterMapper;
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
  public ChapterService(final ChapterRepository newChapterRepository,
                        final ChapterMapper newChapterMapper) {
    this.chapterRepository = newChapterRepository;
    this.chapterMapper = newChapterMapper;
  }

  /**
   * Save a chapter.
   *
   * @param chapter the entity to save.
   * @return the persisted entity.
   */
  public Chapter save(final Chapter chapter) {
    LOGGER.debug("Request to save Chapter : {}", chapter);
    return chapterRepository.save(chapter);
  }

  /**
   * Get all the chapters.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<Chapter> findAll() {
    LOGGER.debug("Request to get all Chapters");
    return chapterRepository.findAll();
  }

  /**
   * Get one chapter by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<Chapter> findOne(final UUID id) {
    LOGGER.debug("Request to get Chapter : {}", id);
    return chapterRepository.findById(id);
  }

  /**
   * Delete the chapter by id.
   *
   * @param id the id of the entity.
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
   * Update a chapter without overwriting it.
   *
   * @param chapter the entity to use to update a created entity.
   * @return the persisted entity.
   */
  @Transactional
  public Chapter patch(final Chapter chapter) {
    LOGGER.debug("Request to update Chapter : {}", chapter);
    Optional<Chapter> oldChapter =
        chapterRepository.findById(chapter.getId());

    if (oldChapter.isPresent()) {
      Chapter oldChapterEntity = oldChapter.get();
      chapterMapper.updateChapterFromChapter(chapter, oldChapterEntity);
      return chapterRepository.save(oldChapterEntity);
    }
    return null;
  }
}
