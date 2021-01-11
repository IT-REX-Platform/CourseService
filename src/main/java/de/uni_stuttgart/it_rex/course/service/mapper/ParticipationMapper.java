package de.uni_stuttgart.it_rex.course.service.mapper;


import de.uni_stuttgart.it_rex.course.domain.*;
import de.uni_stuttgart.it_rex.course.service.dto.ParticipationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Participation} and its DTO {@link ParticipationDTO}.
 */
@Mapper(componentModel = "spring", uses = {PersonMapper.class, CourseMapper.class})
public interface ParticipationMapper extends EntityMapper<ParticipationDTO, Participation> {

    @Mapping(source = "person.id", target = "personId")
    @Mapping(source = "course.id", target = "courseId")
    ParticipationDTO toDto(Participation participation);

    @Mapping(source = "personId", target = "person")
    @Mapping(source = "courseId", target = "course")
    Participation toEntity(ParticipationDTO participationDTO);

    default Participation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Participation participation = new Participation();
        participation.setId(id);
        return participation;
    }
}
