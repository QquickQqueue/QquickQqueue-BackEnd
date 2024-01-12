package com.example.qquickqqueue.domain.ticket.service;

import com.example.qquickqqueue.domain.members.entity.Members;
import com.example.qquickqqueue.domain.musical.entity.Musical;
import com.example.qquickqqueue.domain.schedule.entity.Schedule;
import com.example.qquickqqueue.domain.scheduleSeat.entity.ScheduleSeat;
import com.example.qquickqqueue.domain.scheduleSeat.repository.ScheduleSeatRepository;
import com.example.qquickqqueue.domain.seat.entity.Seat;
import com.example.qquickqqueue.domain.seatGrade.entity.SeatGrade;
import com.example.qquickqqueue.domain.stadium.entity.Stadium;
import com.example.qquickqqueue.domain.ticket.dto.TicketRequestDto;
import com.example.qquickqqueue.domain.ticket.dto.TicketResponseDto;
import com.example.qquickqqueue.domain.ticket.entity.Ticket;
import com.example.qquickqqueue.domain.ticket.repository.TicketRepository;
import com.example.qquickqqueue.util.Message;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ScheduleSeatRepository scheduleSeatRepository;
    @InjectMocks
    private TicketService ticketService;

    private Members mockMember;
    private TicketRequestDto requestDto;

    @BeforeEach
    void setUp() {
        mockMember = Members.builder().email("test@example.com").build();

        requestDto = TicketRequestDto.builder()
                .scheduleSeatId(1L)
                .build();
    }

    @Nested
    @DisplayName("createTicket Method Test")
    class CreateTicket {
        @Test
        @DisplayName("createTicket Method Test - Success")
        void createTicketSuccessTest() {
            // given
            Musical musical = Musical.builder().id(1L).title("Test Musical").stadium(Stadium.builder().stadiumName("Test Stadium").build()).build();
            Schedule schedule = Schedule.builder().id(1L).musical(musical).build();
            Seat seat = Seat.builder().id(1L).build();
            SeatGrade seatGrade = SeatGrade.builder().id(1L).build();

            ScheduleSeat scheduleSeat = ScheduleSeat.builder().id(1L).schedule(schedule).seat(seat).seatGrade(seatGrade).isReserved(false).build();

            when(scheduleSeatRepository.findById(1L)).thenReturn(Optional.of(scheduleSeat));
            when(scheduleSeatRepository.save(scheduleSeat)).thenReturn(scheduleSeat);

            Ticket savedTicket = Ticket.builder()
                    .members(mockMember)
                    .musical(musical)
                    .schedule(schedule)
                    .stadium(musical.getStadium())
                    .seatGrade(seatGrade)
                    .seat(seat)
                    .status(true)
                    .build();
            when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);

            // when
            ResponseEntity<Message> responseEntity = ticketService.createTicket(requestDto, mockMember);

            // then
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals("티켓 예매 성공", Objects.requireNonNull(responseEntity.getBody()).getMessage());

            TicketResponseDto responseDto = (TicketResponseDto) responseEntity.getBody().getData();
            assertEquals(savedTicket.getId(), responseDto.getTicketId());
            assertEquals(savedTicket.getMembers().getEmail(), responseDto.getEmail());
            assertEquals(savedTicket.getMusical().getTitle(), responseDto.getMusicalTitle());
            assertEquals(savedTicket.getSchedule().getId(), responseDto.getScheduleId());
            assertEquals(savedTicket.getStadium().getStadiumName(), responseDto.getStadiumName());
            assertEquals(savedTicket.getSeatGrade().getGrade(), responseDto.getSeatGrade());
            assertEquals(savedTicket.getSeat().getRowNum(), responseDto.getRowNum());
            assertEquals(savedTicket.getSeat().getColumnNum(), responseDto.getColumnNum());
            assertEquals(savedTicket.isStatus(), responseDto.isStatus());
        }

        @Test
        @DisplayName("createTicket Method Test - ScheduleSeat Not Found")
        void createTicketScheduleSeatNotFoundTest() {
            // given
            when(scheduleSeatRepository.findById(1L)).thenReturn(Optional.empty());

            // when & then
            assertThrows(EntityNotFoundException.class, () -> ticketService.createTicket(requestDto, mockMember));
        }

        @Test
        @DisplayName("createTicket Method Test - ScheduleSeat Already Reserved")
        void createTicketSeatAlreadyReservedTest() {
            // given
            Musical musical = Musical.builder().id(1L).title("Test Musical").stadium(Stadium.builder().stadiumName("Test Stadium").build()).build();
            Schedule schedule = Schedule.builder().id(1L).musical(musical).build();
            Seat seat = Seat.builder().id(1L).build();
            SeatGrade seatGrade = SeatGrade.builder().id(1L).build();

            ScheduleSeat scheduleSeat = ScheduleSeat.builder().id(1L).schedule(schedule).seat(seat).seatGrade(seatGrade).isReserved(true).build();

            when(scheduleSeatRepository.findById(1L)).thenReturn(Optional.of(scheduleSeat));

            // when & then
            assertThrows(IllegalArgumentException.class, () -> ticketService.createTicket(requestDto, mockMember));
        }
    }

    @Nested
    @DisplayName("cancelTicket Method Test")
    class CancelTicket {
        @Test
        @DisplayName("cancelTicket Method Test - Success")
        void cancelTicketSuccessTest() {
            // given
            Long ticketId = 1L;

            Musical musical = Musical.builder().id(1L).title("Test Musical").stadium(Stadium.builder().stadiumName("Test Stadium").build()).build();
            Schedule schedule = Schedule.builder().id(1L).musical(musical).build();
            Seat seat = Seat.builder().id(1L).build();

            Ticket mockTicket = Ticket.builder()
                    .id(ticketId)
                    .members(mockMember)
                    .status(true)
                    .schedule(schedule)
                    .seat(seat)
                    .build();

            Optional<Ticket> optionalTicket = Optional.of(mockTicket);
            when(ticketRepository.findById(ticketId)).thenReturn(optionalTicket);

            ScheduleSeat mockScheduleSeat = ScheduleSeat.builder()
                    .id(1L)
                    .isReserved(true)
                    .build();
            when(scheduleSeatRepository.findByScheduleIdAndSeatId(mockTicket.getSchedule().getId(), mockTicket.getSeat().getId())).thenReturn(mockScheduleSeat);

            // when
            ResponseEntity<Message> responseEntity = ticketService.cancelTicket(ticketId);

            // then
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals("티켓 취소 성공 ticket-id : " + ticketId, Objects.requireNonNull(responseEntity.getBody()).getMessage());
        }

        @Test
        @DisplayName("cancelTicket Method Test - Ticket Not Found")
        void cancelTicketNotFoundTest() {
            // given
            Long ticketId = 1L;

            when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

            // when & then
            assertThrows(EntityNotFoundException.class, () -> ticketService.cancelTicket(ticketId));
        }

        @Test
        @DisplayName("cancelTicket Method Test - Already canceled")
        void cancelTicketAlreadyCanceledTest() {
            // given
            Long ticketId = 1L;

            Ticket mockTicket = Ticket.builder()
                    .id(ticketId)
                    .members(mockMember)
                    .status(false)
                    .build();

            Optional<Ticket> optionalTicket = Optional.of(mockTicket);
            when(ticketRepository.findById(ticketId)).thenReturn(optionalTicket);

            // when & then
            assertThrows(EntityExistsException.class, () -> ticketService.cancelTicket(ticketId));
        }
    }

    @Nested
    @DisplayName("readTickets Method Test")
    class ReadTickets {
        @Test
        @DisplayName("readTickets Method Test - Success")
        void readTicketsSuccessTest() {
            // given
            Pageable pageable = PageRequest.of(0, 5, Sort.by("id"));

            Musical musical = Musical.builder().id(1L).title("Test Musical").stadium(Stadium.builder().stadiumName("Test Stadium").build()).build();
            Schedule schedule = Schedule.builder().id(1L).musical(musical).build();
            Seat seat = Seat.builder().id(1L).build();
            SeatGrade seatGrade = SeatGrade.builder().id(1L).build();

            Page<Ticket> ticketPage = new PageImpl<>(List.of(
                    Ticket.builder().id(1L).members(mockMember).musical(musical).schedule(schedule).seat(seat).stadium(musical.getStadium()).seatGrade(seatGrade).build(),
                    Ticket.builder().id(2L).members(mockMember).musical(musical).schedule(schedule).seat(seat).stadium(musical.getStadium()).seatGrade(seatGrade).build(),
                    Ticket.builder().id(3L).members(mockMember).musical(musical).schedule(schedule).seat(seat).stadium(musical.getStadium()).seatGrade(seatGrade).build()
            ));

            when(ticketRepository.findAllByMembers_Email(pageable, mockMember.getEmail())).thenReturn(ticketPage);

            // when
            ResponseEntity<Message> responseEntity = ticketService.readTickets(pageable, mockMember);

            // then
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals("티켓 목록 조회 성공 email : " + mockMember.getEmail(), Objects.requireNonNull(responseEntity.getBody()).getMessage());
        }
    }
}
