package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.TimePeriod;
import de.uni_stuttgart.it_rex.course.repository.written.TimePeriodRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.TimePeriodDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.TimePeriodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link TimePeriod}.
 */
@Service
@Transactional
public class TimePeriodService {

  /**
   * Logger.
   */
  private static final Logger LOGGER
      = LoggerFactory.getLogger(TimePeriodService.class);

  /**
   * TimePeriod Repository.
   */
  private final TimePeriodRepository timePeriodRepository;

  /**
   * TimePeriod mapper.
   */
  private final TimePeriodMapper timePeriodMapper;

  /**
   * Constructor.
   *
   * @param newTimePeriodRepository
   * @param newTimePeriodMapper
   */
  @Autowired
  public TimePeriodService(final TimePeriodRepository newTimePeriodRepository,
                           final TimePeriodMapper newTimePeriodMapper) {
    this.timePeriodRepository = newTimePeriodRepository;
    this.timePeriodMapper = newTimePeriodMapper;
  }

  /**
   * Save a TimePeriod.
   *
   * @param timePeriodDTO the TimePeriod to save.
   * @return the persisted TimePeriod.
   */
  public TimePeriodDTO save(final TimePeriodDTO timePeriodDTO) {
    LOGGER.debug("Request to save TimePeriod : {}", timePeriodDTO);
    final TimePeriod TimePeriod = timePeriodRepository
        .save(timePeriodMapper.toEntity(timePeriodDTO));
    return timePeriodMapper.toDTO(TimePeriod);
  }

  /**
   * Get all the TimePeriods.
   *
   * @return the list of TimePeriods.
   */
  @Transactional(readOnly = true)
  public List<TimePeriodDTO> findAll() {
    LOGGER.debug("Request to get all TimePeriods");
    return timePeriodMapper.toDTO(timePeriodRepository.findAll());
  }

  /**
   * Get one TimePeriod by id.
   *
   * @param id the id of the TimePeriod.
   * @return the TimePeriod.
   */
  @Transactional(readOnly = true)
  public Optional<TimePeriodDTO> findOne(final UUID id) {
    LOGGER.debug("Request to get TimePeriod : {}", id);
    return timePeriodMapper.toDTO(timePeriodRepository.findById(id));
  }

  /**
   * Delete the TimePeriod by id.
   *
   * @param id the id of the TimePeriod.
   */
  @Transactional
  public void delete(final UUID id) {
    LOGGER.debug("Request to delete TimePeriod : {}", id);

    final Optional<TimePeriod> toDeleteOptional = timePeriodRepository.findById(id);

    if (!toDeleteOptional.isPresent()) {
      return;
    }
    timePeriodRepository.deleteById(id);
  }

  /**
   * Update a TimePeriod without overwriting it.
   *
   * @param timePeriodDTO the TimePeriod that is used as an update.
   * @return the persisted TimePeriod.
   */
  @Transactional
  public TimePeriodDTO patch(final TimePeriodDTO timePeriodDTO) {
    LOGGER.debug("Request to update TimePeriod : {}", timePeriodDTO);
    Optional<TimePeriod> oldTimePeriod =
        timePeriodRepository.findById(timePeriodDTO.getId());

    if (oldTimePeriod.isPresent()) {
      TimePeriod oldTimePeriodEntity = oldTimePeriod.get();
      timePeriodMapper.updateTimePeriodFromTimePeriodDTO(timePeriodDTO, oldTimePeriodEntity);
      return timePeriodMapper.toDTO(timePeriodRepository.save(oldTimePeriodEntity));
    }
    return null;
  }
}
