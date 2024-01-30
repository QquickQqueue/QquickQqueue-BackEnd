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
import com.example.qquickqqueue.domain.musical.repository.MusicalJdbcRepository;
import com.example.qquickqqueue.domain.musical.repository.MusicalRepository;
import com.example.qquickqqueue.domain.schedule.dto.ScheduleResponseDto;
import com.example.qquickqqueue.domain.schedule.entity.Schedule;
import com.example.qquickqqueue.domain.schedule.repository.ScheduleRepository;
import com.example.qquickqqueue.domain.scheduleSeat.dto.ScheduleSeatResponseDto;
import com.example.qquickqqueue.domain.scheduleSeat.entity.ScheduleSeat;
import com.example.qquickqqueue.domain.scheduleSeat.repository.impl.ScheduleSeatCustomRepositoryImpl;
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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private final MusicalJdbcRepository musicalJdbcRepository;
    private final ScheduleSeatCustomRepositoryImpl scheduleSeatCustomRepository;

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
        List<ScheduleSeat> scheduleSeats = scheduleSeatCustomRepository.findAllBySchedule_IdAndIsReserved(scheduleId, false);
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
        return new ResponseEntity<>(new Message("조회 성공", scheduleSeatCustomRepository.findAllBySchedule_Id(scheduleId)
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

        musicalRepository.findByStartDateBetweenAndStadium_Id(
                        musicalSaveRequestDto.getStartDate(), musicalSaveRequestDto.getEndDate(), stadiumId)
                .ifPresent(existingMusical -> {
                    throw new IllegalArgumentException("이미 등록된 뮤지컬이 있는 기간입니다.");
                });

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

        List<Schedule> scheduleList = scheduleRepository.saveAll(musicalSaveRequestDto.getScheduleList().stream()
                .map(scheduleRequestDto -> Schedule.builder()
                        .startTime(scheduleRequestDto.getStartTime())
                        .endTime(scheduleRequestDto.getEndTime())
                        .musical(musical)
                        .build()).toList());

        List<String> allActorNames = musicalSaveRequestDto.getScheduleList().stream()
                .flatMap(scheduleRequestDto -> scheduleRequestDto.getActorName().stream())
                .distinct().toList();

        List<Actor> allActors = actorRepository.findByActorNameIn(allActorNames);

        if (allActors.size() != allActorNames.size()) {
            throw new EntityNotFoundException("등록되지 않은 배우가 있습니다.");
        }

        Map<String, Actor> actorMap = allActors.stream()
                .collect(Collectors.toMap(Actor::getActorName, Function.identity()));

        List<Casting> castingList = new ArrayList<>();
        IntStream.range(0, scheduleList.size())
                .forEach(i -> {
                    Schedule schedule = scheduleList.get(i);
                    List<String> actorNames = musicalSaveRequestDto.getScheduleList().get(i).getActorName();
                    actorNames.forEach(actorName -> castingList.add(Casting.builder()
                            .schedule(schedule)
                            .actor(actorMap.get(actorName))
                            .musical(musical)
                            .build()));
                });
        castingRepository.saveAll(castingList);

        Map<Grade, SeatGrade> seatGradeMap = new EnumMap<>(Grade.class);
        seatGradeMap.put(Grade.VIP, seatGradeRepository.save(SeatGrade.builder().grade(Grade.VIP).price(musicalSaveRequestDto.getPriceOfVip()).build()));
        seatGradeMap.put(Grade.R, seatGradeRepository.save(SeatGrade.builder().grade(Grade.R).price(musicalSaveRequestDto.getPriceOfVip()).build()));
        seatGradeMap.put(Grade.S, seatGradeRepository.save(SeatGrade.builder().grade(Grade.S).price(musicalSaveRequestDto.getPriceOfVip()).build()));
        seatGradeMap.put(Grade.A, seatGradeRepository.save(SeatGrade.builder().grade(Grade.A).price(musicalSaveRequestDto.getPriceOfVip()).build()));

        List<Seat> seatList = seatRepository.findAllByStadium(stadium);

        musicalJdbcRepository.insertScheduleSeatList(scheduleList.stream()
                .flatMap(s -> seatList.stream()
                        .map(seat -> ScheduleSeat.builder()
                                .isReserved(false)
                                .schedule(s)
                                .seat(seat)
                                .seatGrade(seatGradeMap.get(seat.getGrade()))
                                .build())).toList());

        return new ResponseEntity<>(new Message("뮤지컬 등록 성공", null), HttpStatus.OK);
    }
}