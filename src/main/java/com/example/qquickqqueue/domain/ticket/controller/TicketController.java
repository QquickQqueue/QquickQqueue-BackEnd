package com.example.qquickqqueue.domain.ticket.controller;

import com.example.qquickqqueue.domain.ticket.dto.TicketRequestDto;
import com.example.qquickqqueue.domain.ticket.service.TicketService;
import com.example.qquickqqueue.security.userDetails.UserDetailsImpl;
import com.example.qquickqqueue.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<Message> createTicket(@RequestBody @Validated TicketRequestDto ticketRequestDto,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ticketService.createTicket(ticketRequestDto, userDetails.getMember());
    }

    @PostMapping("/cancel/{ticket-id}")
    public ResponseEntity<Message> cancelTicket(@PathVariable(name = "ticket-id") Long ticketId) {
        return ticketService.cancelTicket(ticketId);
    }

    @GetMapping
    public ResponseEntity<Message> readTickets(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ticketService.readTickets(pageable, userDetails.getMember());
    }
}
