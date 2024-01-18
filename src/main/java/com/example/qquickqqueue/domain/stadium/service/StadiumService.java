package com.example.qquickqqueue.domain.stadium.service;

import com.example.qquickqqueue.domain.enumPackage.Grade;
import com.example.qquickqqueue.domain.seat.entity.Seat;
import com.example.qquickqqueue.domain.seat.repository.SeatRepository;
import com.example.qquickqqueue.domain.stadium.dto.request.StadiumRequestDto;
import com.example.qquickqqueue.domain.stadium.entity.Stadium;
import com.example.qquickqqueue.domain.stadium.repository.StadiumRepository;
import com.example.qquickqqueue.util.Message;
import jakarta.transaction.Transactional;
import lombok.Generated;
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

        int totalRows = stadiumRequestDto.getRowNum();
        int totalColumns = stadiumRequestDto.getColumnNum();

        IntStream.rangeClosed(1, totalRows)
                .boxed()
                .flatMap(rowNum ->
                        IntStream.rangeClosed(1, totalColumns)
                                .mapToObj(columnNum ->
                                        Seat.builder()
                                                .stadium(stadium)
                                                .rowNum(rowNum)
                                                .columnNum(columnNum)
                                                .grade(calculateGrade(rowNum, columnNum, totalRows, totalColumns))
                                                .build()
                                )
                )
                .forEach(seatRepository::save);
        return new ResponseEntity<>(new Message("공연장 등록 성공", null), HttpStatus.OK);
    }

    @Generated
    private Grade calculateGrade(int rowNum, int columnNum, int totalRows, int totalColumns) {
        if (totalRows == 20 && totalColumns == 20) {
            return Grade.valueOf(calculateGrade20by20(rowNum, columnNum));
        } else if (totalRows == 30 && totalColumns == 30) {
            return Grade.valueOf(calculateGrade30by30(rowNum, columnNum));
        } else if (totalRows == 40 && totalColumns == 30) {
            return Grade.valueOf(calculateGrade40by30(rowNum, columnNum));
        } else {
            throw new IllegalArgumentException("row와 column은 20*20, 30*30, 40*30만 허용됩니다.");
        }
    }

    @Generated
    private String calculateGrade20by20(int rowNum, int columnNum) {
        if (rowNum <= 8 && columnNum >= 5 && columnNum <= 16) {
            return "VIP";
        } else if (rowNum <= 12) {
            return "R";
        } else if (rowNum <= 16) {
            return "S";
        } else {
            return "A";
        }
    }

    @Generated
    private String calculateGrade30by30(int rowNum, int columnNum) {
        if (rowNum <= 10 && columnNum >= 6 && columnNum <= 25) {
            return "VIP";
        } else if (rowNum <= 15) {
            return "R";
        } else if (rowNum <= 22) {
            return "S";
        } else {
            return "A";
        }
    }

    @Generated
    private String calculateGrade40by30(int rowNum, int columnNum) {
        if (rowNum <= 12 && columnNum >= 6 && columnNum <= 25) {
            return "VIP";
        } else if (rowNum <= 18) {
            return "R";
        } else if (rowNum <= 28) {
            return "S";
        } else {
            return "A";
        }
    }
}
