package de.uni_stutgart.it_rex.course.service.mapper;


import de.uni_stutgart.it_rex.course.domain.*;
import de.uni_stutgart.it_rex.course.service.dto.PersonDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Person} and its DTO {@link PersonDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    PersonDTO toDto(Person person);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "removeCourse", ignore = true)
    Person toEntity(PersonDTO personDTO);

    default Person fromId(Long id) {
        if (id == null) {
            return null;
        }
        Person person = new Person();
        person.setId(id);
        return person;
    }
}
