package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mapper(componentModel = "spring")
public abstract class ChapterMapper {

    @Autowired
    ContentReferenceRepository contentReferenceRepository;

    /**
     * Updates an entity from another entity.
     *
     * @param update   the update
     * @param toUpdate the updated entity.
     */
    @BeanMapping(nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateChapterFromChapter
    (Chapter update, @MappingTarget Chapter toUpdate);

    /**
     * Updates an entity from a DTO.
     *
     * @param update   the update
     * @param toUpdate the updated entity.
     */
    public void updateChapterFromChapterDTO(final ChapterDTO update,
                                            final Chapter toUpdate) {
        if (update.getId() != null) {
            toUpdate.setId(update.getId());
        }
        if (update.getTitle() != null) {
            toUpdate.setTitle(update.getTitle());
        }
        if (update.getCourseId() != null) {
            toUpdate.setCourseId(update.getCourseId());
        }
        if (update.getStartDate() != null) {
            toUpdate.setStartDate(update.getStartDate());
        }
        if (update.getEndDate() != null) {
            toUpdate.setEndDate(update.getEndDate());
        }
        if (update.getContentReferenceIds() != null) {
            toUpdate.setContentReferences(
                contentReferenceRepository.findAllById(
                    update.getContentReferenceIds()));
        }
    }

    /**
     * Converts an entity to a DTO.
     *
     * @param chapter the entity
     * @return the dto
     */
    public ChapterDTO toDTO(final Chapter chapter) {
        final ChapterDTO chapterDTO = setBasicProperties(chapter);
        if (chapter.getContentReferences() != null) {
            final List<UUID> chapters = chapter.getContentReferences().stream()
                .map(ContentReference::getContentId)
                .collect(Collectors.toList());
            chapterDTO.setContentReferenceIds(chapters);
        }
        return chapterDTO;
    }

    /**
     * Converts an optional entity to a optional DTO.
     *
     * @param chapter the entity
     * @return the dto
     */
    public Optional<ChapterDTO> toDTO(final Optional<Chapter> chapter) {
        if (chapter.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toDTO(chapter.get()));
    }

    /**
     * Converts a list of entities to a list of DTOs.
     *
     * @param chapters the entities
     * @return the DTOs
     */
    public List<ChapterDTO> toDTO(final List<Chapter> chapters) {
        return chapters.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<Chapter> toEntity(final List<ChapterDTO> chapterDTOS) {
        return chapterDTOS.stream().map(this::toEntity)
            .collect(Collectors.toList());
    }

    /**
     * Converts a DTO to an entity.
     *
     * @param chapterDTO the dto
     * @return the entity
     */
    public Chapter toEntity(final ChapterDTO chapterDTO) {
        final Chapter chapter = setBasicProperties(chapterDTO);
        if (chapterDTO.getContentReferenceIds() != null) {
            final List<ContentReference> contentReferences =
                contentReferenceRepository.findAllById(
                    chapterDTO.getContentReferenceIds());
            chapter.setContentReferences(contentReferences);
        }
        return chapter;
    }

    /**
     * Creates a ChapterDTO from a Chapter and sets all properties
     * except the contents.
     *
     * @param chapter the entity
     * @return the dto
     */
    private ChapterDTO setBasicProperties(final Chapter chapter) {
        final ChapterDTO chapterDTO = new ChapterDTO();
        chapterDTO.setId(chapter.getId());
        chapterDTO.setTitle(chapter.getTitle());
        chapterDTO.setCourseId(chapter.getCourseId());
        chapterDTO.setStartDate(chapter.getStartDate());
        chapterDTO.setEndDate(chapter.getEndDate());
        return chapterDTO;
    }

    /**
     * Creates a Chapter from a ChapterDTO and sets all properties
     * except the contents.
     *
     * @param chapterDTO the dto
     * @return the entity
     */
    private Chapter setBasicProperties(final ChapterDTO chapterDTO) {
        final Chapter chapter = new Chapter();
        chapter.setId(chapterDTO.getId());
        chapter.setTitle(chapterDTO.getTitle());
        chapter.setCourseId(chapterDTO.getCourseId());
        chapter.setStartDate(chapterDTO.getStartDate());
        chapter.setEndDate(chapterDTO.getEndDate());
        return chapter;
    }

}
