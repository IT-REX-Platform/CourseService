package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentIndex;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.utils.written.ChapterUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ChapterMapperImpl.class})
class ChapterMapperIT {
  private static final UUID UPDATE_ID = UUID.randomUUID();

  @Autowired
  private ChapterMapper chapterMapper;

  @Test
  void updateChapterFromChapter() {
    Chapter toUpdate = ChapterUtil.createChapter();
    ChapterDTO update = new ChapterDTO();

    update.setId(UPDATE_ID);
    update.setTitle(null);

    Chapter expected = new Chapter();
    expected.setId(update.getId());
    expected.setTitle(toUpdate.getTitle());
    expected.setCourseId(toUpdate.getCourseId());
    expected.setStartDate(toUpdate.getStartDate());
    expected.setEndDate(toUpdate.getEndDate());
    expected.setContents(toUpdate.getContents());
    chapterMapper.updateChapterFromChapterDTO(update, toUpdate);

    assertEquals(expected, toUpdate);
  }

  @Test
  void toDTO() {
    Chapter chapter = ChapterUtil.createChapter();
    ChapterDTO expected = new ChapterDTO();
    expected.setId(chapter.getId());
    expected.setTitle(chapter.getTitle());
    expected.setCourseId(chapter.getCourseId());
    expected.setStartDate(chapter.getStartDate());
    expected.setEndDate(chapter.getEndDate());

    List<UUID> chapters = chapter.getContents().stream()
        .map(ContentIndex::getContentId).collect(Collectors.toList());
    expected.setContents(chapters);

    ChapterDTO result = chapterMapper.toDTO(chapter);
    assertEquals(expected, result);
  }

  @Test
  void toEntity() {
    ChapterDTO chapterDTO = ChapterUtil.createChapterDTO();
    Chapter expected = new Chapter();
    expected.setId(chapterDTO.getId());
    expected.setTitle(chapterDTO.getTitle());
    expected.setCourseId(chapterDTO.getCourseId());
    expected.setStartDate(chapterDTO.getStartDate());
    expected.setEndDate(chapterDTO.getEndDate());

    final List<UUID> contentIds = chapterDTO.getContents();
    final List<ContentIndex> contents =
        IntStream.range(0, contentIds.size()).mapToObj(i -> {
          final UUID id = contentIds.get(i);
          ContentIndex contentIndex = new ContentIndex();
          contentIndex.setIndex(i);
          contentIndex.setContentId(id);
          contentIndex.setChapterId(expected.getId());
          return contentIndex;
        }).collect(Collectors.toList());

    expected.setContents(contents);

    Chapter result = chapterMapper.toEntity(chapterDTO);
    ChapterUtil.equals(expected, result);
  }
}