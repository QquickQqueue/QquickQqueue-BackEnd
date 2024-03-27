package com.example.qquickqqueue.domain.ticket.service;

import com.example.qquickqqueue.domain.members.entity.Members;
import com.example.qquickqqueue.domain.musical.entity.Musical;
import com.example.qquickqqueue.domain.schedule.entity.Schedule;
import com.example.qquickqqueue.domain.scheduleSeat.entity.ScheduleSeat;
import com.example.qquickqqueue.domain.scheduleSeat.repository.ScheduleSeatRepository;
import com.example.qquickqqueue.domain.ticket.dto.TicketRequestDto;
import com.example.qquickqqueue.domain.ticket.dto.TicketResponseDto;
import com.example.qquickqqueue.domain.ticket.entity.Ticket;
import com.example.qquickqqueue.domain.ticket.repository.TicketRepository;
import com.example.qquickqqueue.util.Message;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final ScheduleSeatRepository scheduleSeatRepository;
    private final RedissonClient redissonClient;

    @Transactional
    public ResponseEntity<Message> createTicket(TicketRequestDto ticketRequestDto, Members members) {

        RLock rLock = redissonClient.getLock("ticketLock:" + ticketRequestDto.getScheduleSeatId());

        long waitTime = 5L;
        long leaseTime = 3L;
        TimeUnit timeUnit = TimeUnit.SECONDS;

        Schedule schedule = null;
        Musical musical = null;
        ScheduleSeat scheduleSeat = null;

        try {
            boolean available = rLock.tryLock(waitTime, leaseTime, timeUnit);
            if (!available) {
                throw new EntityExistsException("이미 선택된 좌석입니다.");
            }

            scheduleSeat = scheduleSeatRepository.findById(ticketRequestDto.getScheduleSeatId()).orElseThrow(
                    () -> new EntityNotFoundException("해당 회차와 좌석을 찾을 수 없습니다. scheduleSeat-id : " + ticketRequestDto.getScheduleSeatId())
            );

            schedule = scheduleSeat.getSchedule();
            musical = schedule.getMusical();

            if (scheduleSeat.isReserved()) {
                throw new EntityExistsException("이미 선택된 좌석입니다.");
            } else {
                scheduleSeat.setReserved(true);
                scheduleSeatRepository.saveAndFlush(scheduleSeat);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error(e.getMessage());
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }

        Ticket newTicket = Ticket.builder()
                .members(members)
                .musical(musical)
                .schedule(schedule)
                .stadium(musical.getStadium())
                .seatGrade(scheduleSeat.getSeatGrade())
                .seat(scheduleSeat.getSeat())
                .status(true)
                .build();
        Ticket savedTicket = ticketRepository.save(newTicket);

        TicketResponseDto responseDto = TicketResponseDto.builder()
                .ticketId(savedTicket.getId())
                .email(savedTicket.getMembers().getEmail())
                .musicalTitle(savedTicket.getMusical().getTitle())
                .scheduleId(savedTicket.getSchedule().getId())
                .stadiumName(savedTicket.getStadium().getStadiumName())
                .seatGrade(savedTicket.getSeatGrade().getGrade())
                .rowNum(savedTicket.getSeat().getRowNum())
                .columnNum(savedTicket.getSeat().getColumnNum())
                .status(savedTicket.isStatus())
                .build();
        return new ResponseEntity<>(new Message("티켓 예매 성공", responseDto), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> cancelTicket(Long ticketId) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);

        if (optionalTicket.isEmpty()) {
            throw new EntityNotFoundException("해당 티켓을 찾을 수 없습니다. ticket-id : " + ticketId);
        } else if (!optionalTicket.get().isStatus()) {
            throw new EntityExistsException("이미 취소된 티켓입니다. ticket-id : " + ticketId);
        } else {
            Ticket ticket = optionalTicket.get();

            ticket.setStatus(false);
            ticketRepository.save(ticket);

            ScheduleSeat scheduleSeat = scheduleSeatRepository.findByScheduleIdAndSeatId(ticket.getSchedule().getId(), ticket.getSeat().getId());

            scheduleSeat.setReserved(false);
            scheduleSeatRepository.save(scheduleSeat);

            return new ResponseEntity<>(new Message("티켓 취소 성공 ticket-id : " + ticketId, null), HttpStatus.OK);
        }
    }

    public ResponseEntity<Message> readTickets(Pageable pageable, Members members) {
        Page<Ticket> ticketsPage = ticketRepository.findAllByMembers_Email(pageable, members.getEmail());
        Page<TicketResponseDto> responseDtoPage = ticketsPage.map(ticket -> TicketResponseDto.builder()
                .ticketId(ticket.getId())
                .email(ticket.getMembers().getEmail())
                .musicalTitle(ticket.getMusical().getTitle())
                .scheduleId(ticket.getSchedule().getId())
                .stadiumName(ticket.getStadium().getStadiumName())
                .seatGrade(ticket.getSeatGrade().getGrade())
                .rowNum(ticket.getSeat().getRowNum())
                .columnNum(ticket.getSeat().getColumnNum())
                .status(ticket.isStatus())
                .build());
        return new ResponseEntity<>(new Message("티켓 목록 조회 성공 email : " + members.getEmail(), responseDtoPage), HttpStatus.OK);
    }
}
