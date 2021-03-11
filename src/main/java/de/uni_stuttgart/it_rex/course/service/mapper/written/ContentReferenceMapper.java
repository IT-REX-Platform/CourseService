package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written.ContentReference;
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

    /**
     * The ChapterRepository.
     */
    @Autowired
    private ChapterRepository chapterRepository;

    /**
     * The TimePeriodRepository.
     */
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
        ContentReferenceDTO update,
        @MappingTarget ContentReference toUpdate);

    /**
     * Converts a DTO to an entity.
     *
     * @param contentReferenceDTO the DTO
     * @return the entity
     */
    @Mapping(target = "chapter", ignore = true)
    @Mapping(target = "timePeriod", ignore = true)
    @Mapping(target = "index", ignore = true)
    public abstract ContentReference toEntity(
        ContentReferenceDTO contentReferenceDTO);

    /**
     * Converts a list of DTOs to a list of entities.
     *
     * @param contentReferenceDTOs the DTOs
     * @return the entities
     */
    public abstract List<ContentReference> toEntity(
        Collection<ContentReferenceDTO> contentReferenceDTOs);

    /**
     * Sets the Relationships when a DTO is converted to an entity.
     *
     * @param contentReferenceDTO the DTO
     * @param contentReference    the entity
     */
    @AfterMapping
    protected void setRelationships(
        final ContentReferenceDTO contentReferenceDTO,
        @MappingTarget final ContentReference contentReference) {
        if (contentReferenceDTO.getChapterId() != null) {
            chapterRepository.findById(contentReferenceDTO.getChapterId())
                .ifPresent(contentReference::setChapter);
        }
        if (contentReferenceDTO.getTimePeriodId() != null) {
            timePeriodRepository.findById(contentReferenceDTO.getTimePeriodId())
                .ifPresent(contentReference::setTimePeriod);
        }
    }

    /**
     * Sets the index when a DTO is converted to an entity.
     *
     * @param contentReference the entity
     */
    @AfterMapping
    protected void setIndex(
        @MappingTarget final ContentReference contentReference) {
        contentReference.setIndex(contentReference
            .getChapter()
            .getContentReferences()
            .indexOf(contentReference));
    }

    /**
     * Converts an entity to a DTO.
     *
     * @param contentReference the entity
     * @return the DTO
     */
    @Mapping(target = "chapterId", source = "chapter.id")
    @Mapping(target = "timePeriodId", source = "timePeriod.id")
    public abstract ContentReferenceDTO toDTO(
        ContentReference contentReference);

    /**
     * Converts a list of entities to a list of DTOs.
     *
     * @param contentReferences the entities
     * @return the DTOs
     */
    public abstract List<ContentReferenceDTO> toDTO(
        Collection<ContentReference> contentReferences);

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
