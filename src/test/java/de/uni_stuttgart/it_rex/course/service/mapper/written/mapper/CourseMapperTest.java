package de.uni_stuttgart.it_rex.course.service.mapper.written.mapper;

import de.uni_stuttgart.it_rex.course.service.written.mapper.CourseMapper;
import de.uni_stuttgart.it_rex.course.service.written.mapper.CourseMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CourseMapperTest {

    private static final UUID id = UUID.randomUUID();
    private CourseMapper courseMapper;

    @BeforeEach
    public void setUp() {
        courseMapper = new CourseMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        assertThat(courseMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(courseMapper.fromId(null)).isNull();
    }
}
