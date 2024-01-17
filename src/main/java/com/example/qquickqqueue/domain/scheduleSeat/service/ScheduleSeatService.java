package com.example.qquickqqueue.domain.scheduleSeat.service;

import com.example.qquickqqueue.domain.scheduleSeat.dto.ScheduleSeatResponseDto;
import com.example.qquickqqueue.domain.scheduleSeat.entity.ScheduleSeat;
import com.example.qquickqqueue.domain.scheduleSeat.repository.ScheduleSeatRepository;
import com.example.qquickqqueue.util.Message;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleSeatService {
    private final ScheduleSeatRepository scheduleSeatRepository;
    public ResponseEntity<Message> readScheduleSeat(Long scheduleSeatId) {
        ScheduleSeat scheduleSeat = scheduleSeatRepository.findById(scheduleSeatId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 스케줄 좌석입니다. ScheduleSeatId : " + scheduleSeatId)
        );
        return new ResponseEntity<>(new Message("조회 성공", ScheduleSeatResponseDto.builder()
                .name(scheduleSeat.getSchedule().getMusical().getTitle())
                .price(scheduleSeat.getSeatGrade().getPrice())
                .build()), HttpStatus.OK);
    }
}
