package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.written_entities.TimePeriod;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.TimePeriodDTO;
import de.uni_stuttgart.it_rex.course.utils.written.TimePeriodUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = {CourseServiceApp.class, TestSecurityConfiguration.class})
class TimePeriodMapperIT {

  private static final UUID UPDATE_ID = UUID.randomUUID();

  @Autowired
  private TimePeriodMapper timePeriodMapper;

  @Test
  void updateTimePeriodFromTimePeriodDTO() {
    TimePeriod toUpdate = TimePeriodUtil.createTimePeriod();
    TimePeriodDTO update = new TimePeriodDTO();

    update.setId(UPDATE_ID);
    update.setEndDate(null);

    TimePeriod expected = new TimePeriod();
    expected.setId(update.getId());
    expected.setStartDate(toUpdate.getStartDate());
    expected.setEndDate(toUpdate.getEndDate());

    timePeriodMapper.updateTimePeriodFromTimePeriodDTO(update, toUpdate);

    TimePeriodUtil.equalsTimePeriod(expected, toUpdate);
  }

  @Test
  void toDTO() {
    TimePeriod TimePeriod = TimePeriodUtil.createTimePeriod();
    TimePeriodDTO expected = new TimePeriodDTO();

    expected.setId(TimePeriod.getId());
    expected.setStartDate(TimePeriod.getStartDate());
    expected.setEndDate(TimePeriod.getEndDate());
    expected.setContentReferenceIds(List.of());

    TimePeriodDTO result = timePeriodMapper.toDTO(TimePeriod);
    TimePeriodUtil.equalsTimePeriodDTO(expected, result);
  }

  @Test
  void toEntity() {
    TimePeriodDTO TimePeriodDTO
        = TimePeriodUtil.createTimePeriodDTO();
    TimePeriod expected = new TimePeriod();

    expected.setId(TimePeriodDTO.getId());
    expected.setStartDate(TimePeriodDTO.getStartDate());
    expected.setEndDate(TimePeriodDTO.getEndDate());

    TimePeriod result = timePeriodMapper.toEntity(TimePeriodDTO);
    TimePeriodUtil.equalsTimePeriod(expected, result);
  }
}
