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

import java.util.stream.IntStream;

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
        IntStream.rangeClosed(1, stadiumRequestDto.getMaxRowNum()).boxed()
                .flatMap(i -> IntStream.rangeClosed(1, stadiumRequestDto.getMaxColumnNum())
                        .mapToObj(j -> Seat.builder()
                                .stadium(stadium)
                                .rowNum(i)
                                .columnNum(j)
                                .build()))
                .forEach(seatRepository::save);
        return new ResponseEntity<>(new Message("공연장 등록 성공", null), HttpStatus.OK);
    }
}
