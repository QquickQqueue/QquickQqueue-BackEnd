package com.example.qquickqqueue.domain.musical.service;

import com.example.qquickqqueue.domain.actor.entity.Actor;
import com.example.qquickqqueue.domain.casting.entity.Casting;
import com.example.qquickqqueue.domain.casting.repository.CastingRepository;
import com.example.qquickqqueue.domain.enumPackage.Gender;
import com.example.qquickqqueue.domain.enumPackage.Grade;
import com.example.qquickqqueue.domain.enumPackage.Rating;
import com.example.qquickqqueue.domain.musical.dto.MusicalResponseDto;
import com.example.qquickqqueue.domain.musical.dto.MusicalRoundInfoResponseDto;
import com.example.qquickqqueue.domain.musical.entity.Musical;
import com.example.qquickqqueue.domain.musical.repository.MusicalRepository;
import com.example.qquickqqueue.domain.schedule.dto.ScheduleResponseDto;
import com.example.qquickqqueue.domain.schedule.entity.Schedule;
import com.example.qquickqqueue.domain.schedule.repository.ScheduleRepository;
import com.example.qquickqqueue.domain.scheduleSeat.dto.ScheduleSeatResponseDto;
import com.example.qquickqqueue.domain.scheduleSeat.entity.ScheduleSeat;
import com.example.qquickqqueue.domain.scheduleSeat.repository.ScheduleSeatRepository;
import com.example.qquickqqueue.domain.seat.entity.Seat;
import com.example.qquickqqueue.domain.seatGrade.entity.SeatGrade;
import com.example.qquickqqueue.domain.stadium.entity.Stadium;
import com.example.qquickqqueue.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class MusicalServiceTest {
    @Mock
    private MusicalRepository musicalRepository;
    @Mock
    private ScheduleSeatRepository scheduleSeatRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private CastingRepository castingRepository;
    @InjectMocks
    private MusicalService musicalService;

    Musical musical = Musical.builder()
            .id(1L)
            .title("musical.getTitle()")
            .thumbnailUrl("musical.getThumbnailUrl()")
            .rating(Rating.PG12)
            .stadium(Stadium.builder()
                    .stadiumName("여주")
                    .build())
            .description("musical.getDescription()")
            .startDate(LocalDate.of(2023, 3, 4))
            .endDate(LocalDate.of(2023, 5, 4))
            .runningTime(LocalTime.of(3, 3, 3))
            .build();

    List<Musical> musicalList = new ArrayList<>();

    @BeforeEach
    void loop() {
        for (int i = 0; i < 10; i++) {
            musicalList.add(musical);
        }
    }

    @Nested
    @DisplayName("readMusicals Method Test")
    class ReadMusicals {
        @Test
        @DisplayName("readMusicals Method Test")
        void readMusicalsTest() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            Page<Musical> musicalPage = new PageImpl<>(musicalList);

            when(musicalRepository.findAll(pageable)).thenReturn(musicalPage);

            Page<MusicalResponseDto> musicalResponseDtoPage = new PageImpl<>(musicalList.stream().map(musical -> MusicalResponseDto.builder()
                    .title(musical.getTitle())
                    .thumbnailUrl(musical.getThumbnailUrl())
                    .rating(musical.getRating())
                    .description(musical.getDescription())
                    .startDate(musical.getStartDate())
                    .endDate(musical.getEndDate())
                    .runningTime(musical.getRunningTime())
                    .build()).toList());

            // when
            ResponseEntity<Message> response = musicalService.readMusicals(pageable);
            Page<MusicalResponseDto> responseValue = (Page<MusicalResponseDto>) response.getBody().getData();

            // then
            assertEquals("조회 성공", response.getBody().getMessage());
            assertEquals(musicalResponseDtoPage.getTotalPages(), responseValue.getTotalPages());
            assertEquals(musicalResponseDtoPage.getContent().get(1).getTitle(), responseValue.getContent().get(1).getTitle());
        }
    }

    @Nested
    @DisplayName("readMusical Method Test")
    class ReadMusical {
        @Test
        @DisplayName("readMusical Method Success Test")
        void readMusicalTest() {
            // given
            Long musicalId = 1L;

            Actor actor = new Actor(1L, "name", Gender.MALE);
            Schedule schedule1 = Schedule.builder()
                    .id(1L).musical(musical).isDeleted(false).startTime(LocalDateTime.now()).endTime(LocalDateTime.now())
                    .build();
            Schedule schedule2 = Schedule.builder()
                    .id(2L).musical(musical).isDeleted(false).startTime(LocalDateTime.now()).endTime(LocalDateTime.now())
                    .build();
            Schedule schedule3 = Schedule.builder()
                    .id(3L).musical(musical).isDeleted(false).startTime(LocalDateTime.now()).endTime(LocalDateTime.now())
                    .build();

            List<Schedule> schedules = List.of(schedule1, schedule2, schedule3);

            ScheduleResponseDto scheduleResponseDto1 = ScheduleResponseDto.builder()
                    .id(1L)
                    .build();
            ScheduleResponseDto scheduleResponseDto2 = ScheduleResponseDto.builder()
                    .id(2L)
                    .build();
            ScheduleResponseDto scheduleResponseDto3 = ScheduleResponseDto.builder()
                    .id(3L)
                    .build();

            MusicalResponseDto musicalResponseDto = MusicalResponseDto.builder()
                    .id(musical.getId()).title(musical.getTitle())
                    .build();

            when(musicalRepository.findById(musicalId)).thenReturn(Optional.of(musical));
            when(scheduleRepository.findAllByMusical_Id(musicalId)).thenReturn(schedules);

            // when
            ResponseEntity<Message> response = musicalService.readMusical(musicalId);
            MusicalResponseDto responseValue = (MusicalResponseDto) response.getBody().getData();

            // then
            assertEquals("조회 성공", response.getBody().getMessage());
            assertEquals(musicalResponseDto.getId(), responseValue.getId());
            assertEquals(musicalResponseDto.getTitle(), responseValue.getTitle());
            assertEquals(scheduleResponseDto1.getId(), responseValue.getScheduleList().get(0).getId());
            assertEquals(scheduleResponseDto2.getId(), responseValue.getScheduleList().get(1).getId());
            assertEquals(scheduleResponseDto3.getId(), responseValue.getScheduleList().get(2).getId());
        }

        @Test
        @DisplayName("readMusical Method Exception Test")
        void readMusicalExceptionTest() {
            // given
            Long musicalId = 2L;

            when(musicalRepository.findById(musicalId)).thenReturn(Optional.empty());

            // when & then
            assertThrows(IllegalArgumentException.class, () -> musicalService.readMusical(musicalId));
        }
    }

    @Nested
    @DisplayName("readMusicalRoundInfoByDate Method Test")
    class ReadMusicalRoundInfoByDate {
        @Test
        @DisplayName("readMusicalRoundInfoByDate Method Test")
        void readMusicalRoundInfoByDateTest() {
            // given
            Actor actor = new Actor(1L, "name", Gender.MALE);

            Schedule schedule = Schedule.builder()
                    .id(1L).startTime(LocalDateTime.now()).endTime(LocalDateTime.now()).actor(actor).musical(musical).isDeleted(false)
                    .build();

            Seat seat = Seat.builder()
                    .id(1L).columnNum(1).rowNum(3).stadium(new Stadium(1L, "name", "address"))
                    .build();

            SeatGrade seatGrade1 = new SeatGrade(1L, Grade.R, 1000);
            SeatGrade seatGrade2 = new SeatGrade(2L, Grade.R, 1000);
            SeatGrade seatGrade3 = new SeatGrade(3L, Grade.VIP, 1000);
            SeatGrade seatGrade4 = new SeatGrade(4L, Grade.S, 1000);
            SeatGrade seatGrade5 = new SeatGrade(5L, Grade.S, 1000);
            SeatGrade seatGrade9 = new SeatGrade(8L, Grade.A, 1000);

            ScheduleSeat scheduleSeat1 = ScheduleSeat.builder()
                    .id(1L).schedule(schedule).seat(seat).seatGrade(seatGrade1).isReserved(false).build();
            ScheduleSeat scheduleSeat2 = ScheduleSeat.builder()
                    .id(1L).schedule(schedule).seat(seat).seatGrade(seatGrade2).isReserved(false).build();
            ScheduleSeat scheduleSeat3 = ScheduleSeat.builder()
                    .id(1L).schedule(schedule).seat(seat).seatGrade(seatGrade3).isReserved(false).build();
            ScheduleSeat scheduleSeat4 = ScheduleSeat.builder()
                    .id(1L).schedule(schedule).seat(seat).seatGrade(seatGrade4).isReserved(false).build();
            ScheduleSeat scheduleSeat5 = ScheduleSeat.builder()
                    .id(1L).schedule(schedule).seat(seat).seatGrade(seatGrade5).isReserved(false).build();
            ScheduleSeat scheduleSeat9 = ScheduleSeat.builder()
                    .id(1L).schedule(schedule).seat(seat).seatGrade(seatGrade9).isReserved(false).build();

            Casting casting1 = Casting.builder()
                    .id(1L).actor(actor).musical(musical).schedule(schedule).build();
            Casting casting2 = Casting.builder()
                    .id(1L).actor(actor).musical(musical).schedule(schedule).build();
            Casting casting3 = Casting.builder()
                    .id(1L).actor(actor).musical(musical).schedule(schedule).build();
            Casting casting4 = Casting.builder()
                    .id(1L).actor(actor).musical(musical).schedule(schedule).build();
            Casting casting5 = Casting.builder()
                    .id(1L).actor(actor).musical(musical).schedule(schedule).build();


            List<ScheduleSeat> scheduleSeats = List.of(scheduleSeat1, scheduleSeat2, scheduleSeat3, scheduleSeat4, scheduleSeat5, scheduleSeat9);
            List<Casting> castings = List.of(casting1, casting2, casting3, casting4, casting5);

            Long scheduleId = 1L;

            when(scheduleSeatRepository.findAllBySchedule_IdAndIsReserved(scheduleId, false)).thenReturn(scheduleSeats);
            when(castingRepository.findAllBySchedule_Id(scheduleId)).thenReturn(castings);

            // when
            ResponseEntity<Message> response = musicalService.readMusicalRoundInfoByDate(scheduleId);
            MusicalRoundInfoResponseDto responseValue = (MusicalRoundInfoResponseDto) response.getBody().getData();

            // then
            assertEquals("조회 성공", response.getBody().getMessage());
            assertEquals(1, responseValue.getSumVIP());
            assertEquals(2, responseValue.getSumR());
            assertEquals(2, responseValue.getSumS());
            assertEquals(1, responseValue.getSumA());
            assertEquals(List.of(actor, actor, actor, actor, actor), responseValue.getActors());
            assertEquals(actor, responseValue.getActors().get(1));
        }
    }

    @Nested
    @DisplayName("readMusicalSeatInfo Method Test")
    class ReadMusicalSeatInfo {
        @Test
        @DisplayName("readMusicalSeatInfo Method Test")
        void readMusicalSeatInfoTest() {
            // given
            Actor actor = new Actor(1L, "name", Gender.MALE);

            Seat seat1 = Seat.builder()
                    .id(1L).columnNum(1).rowNum(3).stadium(new Stadium(1L, "name", "address"))
                    .build();
            Seat seat2 = Seat.builder()
                    .id(1L).columnNum(2).rowNum(4).stadium(new Stadium(1L, "name", "address"))
                    .build();

            SeatGrade seatGrade1 = new SeatGrade(1L, Grade.R, 1000);
            SeatGrade seatGrade2 = new SeatGrade(2L, Grade.VIP, 1000);

            Schedule schedule1 = Schedule.builder()
                    .id(1L).startTime(LocalDateTime.now()).endTime(LocalDateTime.now()).actor(actor).musical(musical).isDeleted(false)
                    .build();

            ScheduleSeat scheduleSeat1 = ScheduleSeat.builder()
                    .id(1L).isReserved(false).seat(seat1).seatGrade(seatGrade1).schedule(schedule1)
                    .build();
            ScheduleSeat scheduleSeat2 = ScheduleSeat.builder()
                    .id(2L).isReserved(false).seat(seat2).seatGrade(seatGrade2).schedule(schedule1)
                    .build();

            List<ScheduleSeat> scheduleSeats = List.of(scheduleSeat1, scheduleSeat2);

            ScheduleSeatResponseDto scheduleSeatResponseDto1 = ScheduleSeatResponseDto.builder()
                    .id(scheduleSeat1.getId())
                    .isReserved(scheduleSeat1.isReserved())
                    .grade(scheduleSeat1.getSeatGrade().getGrade())
                    .price(scheduleSeat1.getSeatGrade().getPrice())
                    .columnNum(scheduleSeat1.getSeat().getColumnNum())
                    .rowNum(scheduleSeat1.getSeat().getRowNum())
                    .build();
            ScheduleSeatResponseDto scheduleSeatResponseDto2 = ScheduleSeatResponseDto.builder()
                    .id(scheduleSeat2.getId())
                    .isReserved(scheduleSeat2.isReserved())
                    .grade(scheduleSeat2.getSeatGrade().getGrade())
                    .price(scheduleSeat2.getSeatGrade().getPrice())
                    .columnNum(scheduleSeat2.getSeat().getColumnNum())
                    .rowNum(scheduleSeat2.getSeat().getRowNum())
                    .build();

            Long scheduleId = 1L;

            when(scheduleSeatRepository.findAllBySchedule_Id(scheduleId)).thenReturn(scheduleSeats);

            // when
            ResponseEntity<Message> response = musicalService.readMusicalSeatInfo(scheduleId);
            List<ScheduleSeatResponseDto> responseValue = (List<ScheduleSeatResponseDto>) response.getBody().getData();

            // then
            assertEquals("조회 성공", response.getBody().getMessage());
            assertEquals(scheduleSeatResponseDto1.getId(), responseValue.get(0).getId());
            assertEquals(scheduleSeatResponseDto2.getId(), responseValue.get(1).getId());

            assertEquals(scheduleSeatResponseDto1.getGrade(), responseValue.get(0).getGrade());
            assertEquals(scheduleSeatResponseDto2.getGrade(), responseValue.get(1).getGrade());

            assertEquals(scheduleSeatResponseDto1.getPrice(), responseValue.get(0).getPrice());
            assertEquals(scheduleSeatResponseDto2.getPrice(), responseValue.get(1).getPrice());

            assertEquals(scheduleSeatResponseDto1.getColumnNum(), responseValue.get(0).getColumnNum());
            assertEquals(scheduleSeatResponseDto2.getColumnNum(), responseValue.get(1).getColumnNum());

            assertEquals(scheduleSeatResponseDto1.getRowNum(), responseValue.get(0).getRowNum());
            assertEquals(scheduleSeatResponseDto2.getRowNum(), responseValue.get(1).getRowNum());
        }
    }

    @Nested
    @DisplayName("searchMusicals Method Test")
    class SearchMusicals {
        @Test
        @DisplayName("searchMusicals Method Test")
        void searchMusicalsTest() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            String searchKeyword = "musical";

            Page<Musical> musicalPage = new PageImpl<>(musicalList);

            when(musicalRepository.findAllByTitleContaining(searchKeyword, pageable)).thenReturn(musicalPage);

            Page<MusicalResponseDto> musicalResponseDtoPage = new PageImpl<>(musicalList.stream().map(musical -> MusicalResponseDto.builder()
                    .title(musical.getTitle())
                    .thumbnailUrl(musical.getThumbnailUrl())
                    .rating(musical.getRating())
                    .description(musical.getDescription())
                    .startDate(musical.getStartDate())
                    .endDate(musical.getEndDate())
                    .runningTime(musical.getRunningTime())
                    .build()).toList());

            // when
            ResponseEntity<Message> response = musicalService.searchMusicals(searchKeyword, pageable);
            Page<MusicalResponseDto> responseValue = (Page<MusicalResponseDto>) response.getBody().getData();

            // then
            assertEquals("조회 성공", response.getBody().getMessage());
            assertEquals(musicalResponseDtoPage.getTotalPages(), responseValue.getTotalPages());
            assertEquals(musicalResponseDtoPage.getContent().get(1).getTitle(), responseValue.getContent().get(1).getTitle());
        }
    }
}