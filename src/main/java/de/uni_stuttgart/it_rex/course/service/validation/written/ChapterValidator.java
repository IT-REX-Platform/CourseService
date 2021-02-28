package de.uni_stuttgart.it_rex.course.service.validation.written;

import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChapterValidator {
  /**
   * Chapter service.
   */
 private ChapterRepository chapterRepository;

 /**
  * Constructor.
  *
  * @param newChapterRepository the chapter service
  */
 @Autowired
 public ChapterValidator(final ChapterRepository newChapterRepository) {
  this.chapterRepository = newChapterRepository;
 }

}
