package com.example.qquickqqueue.domain.musical.service;

import com.example.qquickqqueue.domain.actor.dto.ActorResponseDto;
import com.example.qquickqqueue.domain.actor.entity.Actor;
import com.example.qquickqqueue.domain.actor.repository.ActorRepository;
import com.example.qquickqqueue.domain.casting.entity.Casting;
import com.example.qquickqqueue.domain.casting.repository.CastingRepository;
import com.example.qquickqqueue.domain.enumPackage.Grade;
import com.example.qquickqqueue.domain.musical.dto.MusicalResponseDto;
import com.example.qquickqqueue.domain.musical.dto.MusicalRoundInfoResponseDto;
import com.example.qquickqqueue.domain.musical.dto.MusicalSaveRequestDto;
import com.example.qquickqqueue.domain.musical.entity.Musical;
import com.example.qquickqqueue.domain.musical.repository.MusicalRepository;
import com.example.qquickqqueue.domain.schedule.dto.ScheduleResponseDto;
import com.example.qquickqqueue.domain.schedule.entity.Schedule;
import com.example.qquickqqueue.domain.schedule.repository.ScheduleRepository;
import com.example.qquickqqueue.domain.scheduleSeat.dto.ScheduleSeatResponseDto;
import com.example.qquickqqueue.domain.scheduleSeat.entity.ScheduleSeat;
import com.example.qquickqqueue.domain.scheduleSeat.repository.ScheduleSeatRepository;
import com.example.qquickqqueue.domain.seat.entity.Seat;
import com.example.qquickqqueue.domain.seat.repository.SeatRepository;
import com.example.qquickqqueue.domain.seatGrade.entity.SeatGrade;
import com.example.qquickqqueue.domain.seatGrade.repository.SeatGradeRepository;
import com.example.qquickqqueue.domain.stadium.entity.Stadium;
import com.example.qquickqqueue.domain.stadium.repository.StadiumRepository;
import com.example.qquickqqueue.util.Message;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MusicalService {
    private final MusicalRepository musicalRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleSeatRepository scheduleSeatRepository;
    private final CastingRepository castingRepository;
    private final SeatGradeRepository seatGradeRepository;
    private final StadiumRepository stadiumRepository;
    private final SeatRepository seatRepository;
    private final ActorRepository actorRepository;

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
                () -> new EntityNotFoundException("뮤지컬을 찾을 수 없습니다. Invalid Musical Id : " + musicalId)
        );
        return new ResponseEntity<>(new Message("조회 성공", MusicalResponseDto.builder()
                .id(musical.getId())
                .title(musical.getTitle())
                .thumbnailUrl(musical.getThumbnailUrl())
                .stadiumName(musical.getStadium().getStadiumName())
                .rating(musical.getRating())
                .description(musical.getDescription())
                .startDate(musical.getStartDate())
                .endDate(musical.getEndDate())
                .runningTime(musical.getRunningTime())
                .scheduleList(scheduleRepository.findAllByMusical_Id(musicalId)
                        .stream()
                        .map(schedule -> ScheduleResponseDto.builder()
                                .id(schedule.getId())
                                .startTime(schedule.getStartTime())
                                .endTime(schedule.getEndTime())
                                .isDeleted(schedule.isDeleted())
                                .build()).toList())
                .build()), HttpStatus.OK);
    }

    public ResponseEntity<Message> readMusicalRoundInfoByDate(Long scheduleId) {
        List<ScheduleSeat> scheduleSeats = scheduleSeatRepository.findAllBySchedule_IdAndIsReserved(scheduleId, false);
        int vip = 0, r = 0, s = 0, a = 0;
        for (ScheduleSeat seat : scheduleSeats) {
            switch (seat.getSeatGrade().getGrade()) {
                case VIP -> vip++;
                case R -> r++;
                case S -> s++;
                default -> a++;
            }
        }
        return new ResponseEntity<>(new Message("조회 성공", MusicalRoundInfoResponseDto.builder()
                .sumVIP(vip).sumR(r).sumS(s).sumA(a)
                .actors(castingRepository.findAllBySchedule_Id(scheduleId)
                        .stream()
                        .map(c -> ActorResponseDto.builder()
                                .actorName(c.getActor().getActorName())
                                .build())
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

    public ResponseEntity<Message> searchMusicals(String title, Pageable pageable) {
        return new ResponseEntity<>(new Message("조회 성공", musicalRepository.findAllByTitleContaining(title, pageable)
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

    @Transactional
    public ResponseEntity<Message> saveMusical(MusicalSaveRequestDto musicalSaveRequestDto) {
        Long stadiumId = musicalSaveRequestDto.getStadiumId();

        Optional<Musical> optionalMusical = musicalRepository.findByStartDateBetweenAndStadium_Id(
                musicalSaveRequestDto.getStartDate(), musicalSaveRequestDto.getEndDate(), stadiumId
        );

        if (optionalMusical.isPresent())
            throw new IllegalArgumentException("이미 등록된 뮤지컬이 있는 기간입니다.");

        Stadium stadium = stadiumRepository.findById(stadiumId).orElseThrow(
                () -> new EntityNotFoundException("등록되지 않은 공연장입니다. Stadium Id : " + stadiumId)
        );

        Musical musical = musicalRepository.save(Musical.builder()
                .title(musicalSaveRequestDto.getTitle())
                .description(musicalSaveRequestDto.getDescription())
                .thumbnailUrl(musicalSaveRequestDto.getThumbnailUrl())
                .stadium(stadium)
                .startDate(musicalSaveRequestDto.getStartDate())
                .endDate(musicalSaveRequestDto.getEndDate())
                .runningTime(musicalSaveRequestDto.getRunningTime())
                .rating(musicalSaveRequestDto.getRating())
                .build());

        List<SeatGrade> seatGradeList = seatGradeRepository.saveAll(
                List.of(SeatGrade.builder().grade(Grade.VIP).price(musicalSaveRequestDto.getPriceOfVip()).build()
                        , SeatGrade.builder().grade(Grade.R).price(musicalSaveRequestDto.getPriceOfR()).build()
                        , SeatGrade.builder().grade(Grade.S).price(musicalSaveRequestDto.getPriceOfS()).build()
                        , SeatGrade.builder().grade(Grade.A).price(musicalSaveRequestDto.getPriceOfA()).build())
        );

        List<Schedule> scheduleList = new ArrayList<>();

        musicalSaveRequestDto.getScheduleList().forEach(scheduleRequestDto -> {
                    Schedule schedule = scheduleRepository.save(Schedule.builder()
                            .startTime(scheduleRequestDto.getStartTime())
                            .endTime(scheduleRequestDto.getEndTime())
                            .musical(musical)
                            .build());
                    scheduleList.add(schedule);

                    scheduleRequestDto.getActorName().forEach(s -> {
                        Actor actor = actorRepository.findByActorName(s).orElseThrow(
                                () -> new EntityNotFoundException("등록되지 않은 배우입니다. 배우이름 : " + s)
                        );

                        castingRepository.save(Casting.builder()
                                .schedule(schedule)
                                .actor(actor)
                                .musical(musical)
                                .build());
                    });
                }
        );

        List<Seat> seatList = seatRepository.findAllByStadium(stadium);

        scheduleList.forEach(s ->
                seatList.forEach(seat -> {
                            SeatGrade seatGrade;
                            switch (seat.getGrade()) {
                                case VIP -> seatGrade = seatGradeList.get(0);
                                case R -> seatGrade = seatGradeList.get(1);
                                case S -> seatGrade = seatGradeList.get(2);
                                default -> seatGrade = seatGradeList.get(3);
                            }
                            scheduleSeatRepository.save(ScheduleSeat.builder()
                                    .isReserved(false)
                                    .schedule(s)
                                    .seat(seat)
                                    .seatGrade(seatGrade)
                                    .build());
                        }
                )
        );
        return new ResponseEntity<>(new Message("뮤지컬 등록 성공", null), HttpStatus.OK);
    }
}