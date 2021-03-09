package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mapper(componentModel = "spring",
    uses = ContentReferenceMapper.class,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ChapterMapper {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private  ContentReferenceMapper contentReferenceMapper;

    /**
     * Updates an entity from a DTO.
     *
     * @param update   the update
     * @param toUpdate the updated entity.
     */
    @Mapping(target = "course", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateChapterFromChapterDTO
    (ChapterDTO update, @MappingTarget Chapter toUpdate);

    /**
     * Converts an entity to a DTO.
     *
     * @param chapter the entity
     * @return the dto
     */
    @Mapping(target = "courseId", source = "course.id")
    public abstract ChapterDTO toDTO(final Chapter chapter);

    /**
     * Converts an optional entity to a optional DTO.
     *
     * @param chapter the optional
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
    public abstract List<ChapterDTO> toDTO(final Collection<Chapter> chapters);

    public abstract List<Chapter> toEntity(final List<ChapterDTO> chapterDTOS);

    @AfterMapping
    protected void setCourse(
        ChapterDTO chapterDTO, @MappingTarget Chapter chapter) {
        if (chapterDTO.getCourseId() != null) {
            courseRepository.findById(chapterDTO.getCourseId())
                .ifPresent(chapter::setCourse);
        }
    }

    @AfterMapping
    protected void setContentReferences(
        ChapterDTO chapterDTO, @MappingTarget Chapter chapter) {
        chapter.setContentReferences(IntStream
            .range(0, chapterDTO.getContentReferences().size()).mapToObj(i -> {
                final ContentReferenceDTO contentReferenceDTO
                    = chapterDTO.getContentReferences().get(i);
                final ContentReference contentReference
                    = contentReferenceMapper.toEntity(contentReferenceDTO);
                contentReference.setIndex(i);
                return contentReference;
            }).collect(Collectors.toList()));
    }

    /**
     * Converts a DTO to an entity.
     *
     * @param chapterDTO the dto
     * @return the entity
     */
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "contentReferences", ignore = true)
    public abstract Chapter toEntity(final ChapterDTO chapterDTO);
}
