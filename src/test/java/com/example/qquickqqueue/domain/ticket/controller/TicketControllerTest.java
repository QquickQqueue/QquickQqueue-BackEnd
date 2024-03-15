package com.example.qquickqqueue.domain.ticket.controller;

import com.example.qquickqqueue.domain.enumPackage.Grade;
import com.example.qquickqqueue.domain.members.entity.Members;
import com.example.qquickqqueue.domain.ticket.dto.TicketRequestDto;
import com.example.qquickqqueue.domain.ticket.dto.TicketResponseDto;
import com.example.qquickqqueue.domain.ticket.service.TicketService;
import com.example.qquickqqueue.security.userDetails.UserDetailsImpl;
import com.example.qquickqqueue.util.Message;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class TicketControllerTest {
    @Mock
    private TicketService ticketService;
    @InjectMocks
    private TicketController ticketController;

    @Nested
    @DisplayName("createTicket Method Test")
    class CreateTicket {
        @Test
        @DisplayName("createTicket Method Test")
        void createTicketTest() {
            // given
            TicketRequestDto requestDto = TicketRequestDto.builder()
                    .scheduleSeatId(1L)
                    .build();

            Members mockMember = Members.builder().email("asdf1234@gmail.com").build();
            UserDetailsImpl userDetails = new UserDetailsImpl(mockMember, "asdf1234@gmail.com");

            TicketResponseDto mockResponseDto = TicketResponseDto.builder()
                    .ticketId(1L)
                    .email("asdf1234@gmail.com")
                    .musicalTitle("Test Musical")
                    .scheduleId(1L)
                    .stadiumName("Test Stadium")
                    .seatGrade(Grade.valueOf("VIP"))
                    .rowNum(3)
                    .columnNum(1)
                    .status(true)
                    .build();

            ResponseEntity<Message> mockResponseEntity = new ResponseEntity<>(new Message("티켓 예매 성공", mockResponseDto), HttpStatus.OK);
            when(ticketService.createTicket(requestDto, mockMember)).thenReturn(mockResponseEntity);

            // when
            ResponseEntity<Message> responseEntity = ticketController.createTicket(requestDto, userDetails);

            // then
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals("티켓 예매 성공", Objects.requireNonNull(responseEntity.getBody()).getMessage());

            TicketResponseDto responseDto = (TicketResponseDto) responseEntity.getBody().getData();
            assertEquals(mockResponseDto.getTicketId(), responseDto.getTicketId());
            assertEquals(mockResponseDto.getEmail(), responseDto.getEmail());
            assertEquals(mockResponseDto.getMusicalTitle(), responseDto.getMusicalTitle());
            assertEquals(mockResponseDto.getScheduleId(), responseDto.getScheduleId());
            assertEquals(mockResponseDto.getStadiumName(), responseDto.getStadiumName());
            assertEquals(mockResponseDto.getSeatGrade(), responseDto.getSeatGrade());
            assertEquals(mockResponseDto.getRowNum(), responseDto.getRowNum());
            assertEquals(mockResponseDto.getColumnNum(), responseDto.getColumnNum());
            assertEquals(mockResponseDto.isStatus(), responseDto.isStatus());
        }
    }

    @Nested
    @DisplayName("cancelTicket Method Test")
    class CancelTicket {
        @Test
        @DisplayName("cancelTicket Method Test")
        void cancelTicketTest() {
            // given
            Long ticketId = 1L;

            ResponseEntity<Message> mockResponseEntity = new ResponseEntity<>(new Message("티켓 취소 성공 ticket-id : " + ticketId, null), HttpStatus.OK);
            when(ticketService.cancelTicket(ticketId)).thenReturn(mockResponseEntity);

            // when
            ResponseEntity<Message> responseEntity = ticketController.cancelTicket(ticketId);

            // then
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals("티켓 취소 성공 ticket-id : " + ticketId, Objects.requireNonNull(responseEntity.getBody()).getMessage());
        }
    }

    @Nested
    @DisplayName("readTickets Method Test")
    class ReadTicketsTest {
        @Test
        @DisplayName("readTickets Method Test")
        void readTicketsTest() {
            // given
            Pageable pageable = PageRequest.of(0, 5, Sort.by("id"));

            Members mockMember = Members.builder().email("asdf1234@gmail.com").build();
            UserDetailsImpl userDetails = new UserDetailsImpl(mockMember, "asdf1234@gmail.com");

            Page<TicketResponseDto> mockResponseDtoPage = new PageImpl<>(List.of(
                    TicketResponseDto.builder().ticketId(1L).build(),
                    TicketResponseDto.builder().ticketId(2L).build(),
                    TicketResponseDto.builder().ticketId(3L).build()
            ));

            ResponseEntity<Message> mockResponseEntity = new ResponseEntity<>(new Message("티켓 목록 조회 성공 email : " + mockMember.getEmail(), mockResponseDtoPage), HttpStatus.OK);
            when(ticketService.readTickets(pageable, mockMember)).thenReturn(mockResponseEntity);

            // when
            ResponseEntity<Message> responseEntity = ticketController.readTickets(pageable, userDetails);

            // then
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals("티켓 목록 조회 성공 email : " + mockMember.getEmail(), Objects.requireNonNull(responseEntity.getBody()).getMessage());
        }
    }
}
