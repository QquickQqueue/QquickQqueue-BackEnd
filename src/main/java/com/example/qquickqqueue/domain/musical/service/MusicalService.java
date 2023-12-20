package com.example.qquickqqueue.domain.musical.service;

import com.example.qquickqqueue.domain.musical.dto.MusicalResponseDto;
import com.example.qquickqqueue.domain.musical.entity.Musical;
import com.example.qquickqqueue.domain.musical.repository.MusicalRepository;
import com.example.qquickqqueue.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MusicalService {
    private final MusicalRepository musicalRepository;

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
                .build()), HttpStatus.OK);
    }
}
