package de.uni_stuttgart.it_rex.course.service.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.TimePeriod;
import de.uni_stuttgart.it_rex.course.repository.written.TimePeriodRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.TimePeriodDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.TimePeriodMapper;
import de.uni_stuttgart.it_rex.course.service.written.exception.InvalidTimeRangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
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
        final TimePeriod timePeriod = timePeriodRepository
            .save(timePeriodMapper.toEntity(timePeriodDTO));
        return timePeriodMapper.toDTO(timePeriod);
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

        final Optional<TimePeriod> toDeleteOptional =
            timePeriodRepository.findById(id);

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
            timePeriodMapper.updateTimePeriodFromTimePeriodDTO(timePeriodDTO,
                oldTimePeriodEntity);
            return timePeriodMapper
                .toDTO(timePeriodRepository.save(oldTimePeriodEntity));
        }
        return null;
    }

    /**
     * Create TimePeriodDTOs in the given range.
     * <p>
     * The time range is divided into weeks, starting on Monday and ending on
     * Sunday. For the first week set the range from the startDate to the first
     * occurring Sunday. For the last week set the range from last occurring
     * Monday to the endDate.
     *
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     * @param courseId  the course id of the related Course
     * @return a List of TimePeriodDTOs of the created TimePeriod entities.
     * @throws InvalidTimeRangeException if the endDate is before the startDate
     * @throws InvalidTimeRangeException if the time range is too short
     */
    public List<TimePeriodDTO> createTimePeriodDTOsInRange(
        final LocalDate startDate, final LocalDate endDate,
        final UUID courseId) {
        if (endDate.isBefore(startDate)) {
            final String msg = "End date is before start date.";
            LOGGER.error(msg);
            throw new InvalidTimeRangeException(msg);
        }
        final int shiftOneWeek = 6;

        List<TimePeriodDTO> timePeriodDTOS = new ArrayList<>();

        LocalDate firstWeekEndDate =
            startDate.with(TemporalAdjusters.nextOrSame(
                DayOfWeek.SUNDAY));
        if (!firstWeekEndDate.isAfter(endDate)) {
            timePeriodDTOS.add(
                this.createTimePeriodDTO(startDate, firstWeekEndDate,
                    courseId));
        } else {
            final String msg = "Given time range is too short";
            LOGGER.error(msg);
            throw new InvalidTimeRangeException(msg);
        }

        LocalDate nextWeekStartDate = firstWeekEndDate.plusDays(1);
        LocalDate nextWeekEndDate = nextWeekStartDate.plusDays(shiftOneWeek);

        while (!nextWeekStartDate.isAfter(endDate)) {
            if (nextWeekEndDate.isAfter(endDate)) {
                nextWeekEndDate = endDate;
            }
            timePeriodDTOS.add(
                this.createTimePeriodDTO(nextWeekStartDate, nextWeekEndDate,
                    courseId));
            nextWeekStartDate = nextWeekEndDate.plusDays(1);
            nextWeekEndDate = nextWeekStartDate.plusDays(shiftOneWeek);
        }

        return timePeriodDTOS;
    }

    /**
     * Create a new TimePeriodDTO with the given fields.
     *
     * @param startDate the start date of the TimePeriod
     * @param endDate   the end date of the TimePeriod
     * @param courseId  the course id of the related Course
     * @return the TimePeriodDTO of the created TimePeriod entity.
     * @throws InvalidTimeRangeException if the endDate is before the startDate
     */
    public TimePeriodDTO createTimePeriodDTO(final LocalDate startDate,
                                             final LocalDate endDate,
                                             final UUID courseId) {
        if (endDate.isBefore(startDate)) {
            final String msg = "End date is before start date.";
            LOGGER.error(msg);
            throw new InvalidTimeRangeException(msg);
        }
        final TimePeriodDTO newTimePeriodDTO = new TimePeriodDTO();
        newTimePeriodDTO.setStartDate(startDate);
        newTimePeriodDTO.setEndDate(endDate);
        newTimePeriodDTO.setCourseId(courseId);

        return newTimePeriodDTO;
    }
}
