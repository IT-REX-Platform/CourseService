package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.TimePeriodRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class ContentReferenceMapper {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private TimePeriodRepository timePeriodRepository;

    /**
     * Updates an entity from a DTO.
     *
     * @param update   the update
     * @param toUpdate the updated entity.
     */
    @Mapping(target = "chapter", ignore = true)
    @Mapping(target = "timePeriod", ignore = true)
    @Mapping(target = "index", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateContentReferenceFromContentReferenceDTO(
        final ContentReferenceDTO update,
        @MappingTarget final ContentReference toUpdate);

    @Mapping(target = "chapter", ignore = true)
    @Mapping(target = "timePeriod", ignore = true)
    @Mapping(target = "index", ignore = true)
    public abstract ContentReference toEntity(
        final ContentReferenceDTO contentReferenceDTO);

    public abstract List<ContentReference> toEntity(
        final Collection<ContentReferenceDTO> contentReferenceDTOs);

    @AfterMapping
    protected void setMappings(
        ContentReferenceDTO contentReferenceDTO,
        @MappingTarget ContentReference contentReference) {
        if (contentReferenceDTO.getChapterId() != null) {
            chapterRepository.findById(contentReferenceDTO.getChapterId())
                .ifPresent(contentReference::setChapter);
        }
        if (contentReferenceDTO.getTimePeriodId() != null) {
            timePeriodRepository.findById(contentReferenceDTO.getTimePeriodId())
                .ifPresent(contentReference::setTimePeriod);
        }
    }

    @AfterMapping
    protected void setIndex(@MappingTarget ContentReference contentReference) {
        contentReference.setIndex(  contentReference
                .getChapter()
                .getContentReferences()
                .indexOf(contentReference));
    }

    @Mapping(target = "chapterId", source = "chapter.id")
    @Mapping(target = "timePeriodId", source = "timePeriod.id")
    public abstract ContentReferenceDTO toDTO(
        final ContentReference contentReference);

    public abstract List<ContentReferenceDTO> toDTO(
        final Collection<ContentReference> contentReferences);

    /**
     * Converts an optional entity to a optional DTO.
     *
     * @param contentReference the optional
     * @return the dto
     */
    public Optional<ContentReferenceDTO> toDTO(
        final Optional<ContentReference> contentReference) {
        if (contentReference.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toDTO(contentReference.get()));
    }
}
