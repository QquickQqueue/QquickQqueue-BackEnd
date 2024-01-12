package com.example.qquickqqueue.domain.stadium.service;

import com.example.qquickqqueue.domain.seat.entity.Seat;
import com.example.qquickqqueue.domain.seat.repository.SeatRepository;
import com.example.qquickqqueue.domain.stadium.dto.request.StadiumRequestDto;
import com.example.qquickqqueue.domain.stadium.entity.Stadium;
import com.example.qquickqqueue.domain.stadium.repository.StadiumRepository;
import com.example.qquickqqueue.util.Message;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StadiumService {
    private final StadiumRepository stadiumRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public ResponseEntity<Message> saveStadium(StadiumRequestDto stadiumRequestDto) {
        Stadium stadium = stadiumRepository.save(Stadium.builder()
                .stadiumName(stadiumRequestDto.getStadiumName())
                .address(stadiumRequestDto.getStadiumAddress())
                .build());
        stadiumRequestDto.getSeatList().forEach(s ->
                seatRepository.save(Seat.builder()
                        .stadium(stadium)
                        .rowNum(s.getRowNum())
                        .columnNum(s.getColumnNum())
                        .grade(s.getGrade())
                        .build())
        );
        return new ResponseEntity<>(new Message("공연장 등록 성공", null), HttpStatus.OK);
    }
}
