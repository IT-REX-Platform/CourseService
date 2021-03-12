package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.enumeration.CONTENTREFERENCETYPE;
import de.uni_stuttgart.it_rex.course.domain.written.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written.ContentReference;
import de.uni_stuttgart.it_rex.course.domain.written.Course;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import de.uni_stuttgart.it_rex.course.utils.written.ChapterUtil;
import de.uni_stuttgart.it_rex.course.utils.written.ContentReferenceUtil;
import de.uni_stuttgart.it_rex.course.utils.written.CourseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest(classes = {CourseServiceApp.class,
    TestSecurityConfiguration.class})
class ContentReferenceMapperIT {
    private static final UUID UPDATE_ID = UUID.randomUUID();

    @Autowired
    private ContentReferenceMapper contentReferenceMapper;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    private static Course THE_COURSE;

    private static Chapter THE_CHAPTER;

    @BeforeEach
    void init() {
        THE_COURSE = CourseUtil.createCourse();
        THE_CHAPTER = ChapterUtil.createChapter();
        courseRepository.save(THE_COURSE);
        THE_CHAPTER.setCourse(THE_COURSE);
        chapterRepository.save(THE_CHAPTER);
    }

    @Test
    void updateContentReferenceFromContentReferenceDTO() {
        ContentReference toUpdate =
            ContentReferenceUtil.createContentReference();
        toUpdate.setChapter(THE_CHAPTER);
        ContentReferenceDTO update = new ContentReferenceDTO();

        update.setId(UPDATE_ID);
        update.setContentId(null);

        ContentReference expected = new ContentReference();
        expected.setId(update.getId());
        expected.setContentId(toUpdate.getContentId());
        expected.setChapter(THE_CHAPTER);
        contentReferenceMapper
            .updateContentReferenceFromContentReferenceDTO(update, toUpdate);

        ContentReferenceUtil.equalsContentReference(expected, toUpdate);
    }

    @Test
    void toDTO() {
        ContentReference contentReference =
            ContentReferenceUtil.createContentReference();
        contentReference.setChapter(THE_CHAPTER);
        ContentReferenceDTO expected = new ContentReferenceDTO();

        expected.setId(contentReference.getId());
        expected.setContentId(contentReference.getContentId());
        expected.setChapterId(THE_CHAPTER.getId());

        ContentReferenceDTO result =
            contentReferenceMapper.toDTO(contentReference);
        ContentReferenceUtil.equalsContentReferenceDTO(expected, result);
    }

    @Test
    @Transactional
    void toEntity() {
        ContentReferenceDTO contentReferenceDTO
            = ContentReferenceUtil.createContentReferenceDTO();
        contentReferenceDTO.setChapterId(THE_CHAPTER.getId());

        ContentReference result =
            contentReferenceMapper.toEntity(contentReferenceDTO);

        ContentReference expected = new ContentReference();
        expected.setId(contentReferenceDTO.getId());
        expected.setContentId(contentReferenceDTO.getContentId());
        expected.setContentReferenceType(
            contentReferenceDTO.getContentReferenceType());
        expected.setChapter(THE_CHAPTER);

        ContentReferenceUtil.equalsContentReference(expected, result);
    }
}
