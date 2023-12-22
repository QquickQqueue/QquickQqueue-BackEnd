package com.example.qquickqqueue.domain.musical.service;

import com.example.qquickqqueue.domain.casting.entity.Casting;
import com.example.qquickqqueue.domain.casting.repository.CastingRepository;
import com.example.qquickqqueue.domain.musical.dto.MusicalResponseDto;
import com.example.qquickqqueue.domain.musical.dto.MusicalRoundInfoResponseDto;
import com.example.qquickqqueue.domain.musical.entity.Musical;
import com.example.qquickqqueue.domain.musical.repository.MusicalRepository;
import com.example.qquickqqueue.domain.schedule.dto.ScheduleResponseDto;
import com.example.qquickqqueue.domain.schedule.repository.ScheduleRepository;
import com.example.qquickqqueue.domain.scheduleSeat.dto.ScheduleSeatResponseDto;
import com.example.qquickqqueue.domain.scheduleSeat.entity.ScheduleSeat;
import com.example.qquickqqueue.domain.scheduleSeat.repository.ScheduleSeatRepository;
import com.example.qquickqqueue.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicalService {
    private final MusicalRepository musicalRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleSeatRepository scheduleSeatRepository;
    private final CastingRepository castingRepository;

    public ResponseEntity<Message> readMusicals(Pageable pageable) {
        return new ResponseEntity<>(new Message("조회 성공", musicalRepository.findAll(pageable)
                .map(musical -> MusicalResponseDto.builder()
                        .id(musical.getId())
                        .title(musical.getTitle())
                        .thumbnailUrl(musical.getThumbnailUrl())
                        .rating(musical.getRating())
                        .description(musical.getDescription())
                        .startDate(musical.getStartDate())
                        .endDate(musical.getEndDate())
                        .runningTime(musical.getRunningTime())
                        .build())), HttpStatus.OK);
    }

    public ResponseEntity<Message> readMusical(Long musicalId) {
        Musical musical = musicalRepository.findById(musicalId).orElseThrow(
                () -> new IllegalArgumentException("뮤지컬을 찾을 수 없습니다. Invalid Musical Id : " + musicalId)
        );
        return new ResponseEntity<>(new Message("조회 성공", MusicalResponseDto.builder()
                .id(musical.getId())
                .title(musical.getTitle())
                .thumbnailUrl(musical.getThumbnailUrl())
                .rating(musical.getRating())
                .description(musical.getDescription())
                .startDate(musical.getStartDate())
                .endDate(musical.getEndDate())
                .runningTime(musical.getRunningTime())
                .scheduleList(scheduleRepository.findAllByMusical_Id(musicalId)
                        .stream().map(schedule -> ScheduleResponseDto.builder()
                                .id(schedule.getId())
                                .startTime(schedule.getStartTime())
                                .endTime(schedule.getEndTime())
                                .isDeleted(schedule.isDeleted())
                                .build()).toList())
                .build()), HttpStatus.OK);
    }

    public ResponseEntity<Message> readMusicalRoundInfoByDate(Long scheduleId) {
        List<ScheduleSeat> scheduleSeats = scheduleSeatRepository.findAllBySchedule_IdAndIsReserved(scheduleId, false);
        int vip = 0, r = 0, s = 0, a = 0, b = 0, c = 0;
        for (ScheduleSeat seat : scheduleSeats) {
            switch(seat.getSeatGrade().getGrade()) {
                case VIP -> vip++;
                case R -> r++;
                case S -> s++;
                case A -> a++;
                case B -> b++;
                case C -> c++;
            }
        }
        return new ResponseEntity<>(new Message("조회 성공", MusicalRoundInfoResponseDto.builder()
                .sumVIP(vip).sumR(r).sumS(s).sumA(a).sumB(b).sumC(c)
                .actors(castingRepository.findAllBySchedule_Id(scheduleId)
                        .stream()
                        .map(Casting::getActor)
                        .toList())
                .build()), HttpStatus.OK);
    }

    public ResponseEntity<Message> readMusicalSeatInfo(Long scheduleId) {
        return new ResponseEntity<>(new Message("조회 성공", scheduleSeatRepository.findAllBySchedule_Id(scheduleId)
                .stream()
                .map(scheduleSeat -> ScheduleSeatResponseDto.builder()
                        .id(scheduleSeat.getId())
                        .isReserved(scheduleSeat.isReserved())
                        .grade(scheduleSeat.getSeatGrade().getGrade())
                        .price(scheduleSeat.getSeatGrade().getPrice())
                        .columnNum(scheduleSeat.getSeat().getColumnNum())
                        .rowNum(scheduleSeat.getSeat().getRowNum())
                        .build()).toList()), HttpStatus.OK);
    }
}
