package com.example.qquickqqueue.domain.ticket.repository;

import com.example.qquickqqueue.domain.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
