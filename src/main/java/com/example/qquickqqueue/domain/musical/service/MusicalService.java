package com.example.qquickqqueue.domain.musical.service;

import com.example.qquickqqueue.domain.musical.dto.MusicalResponseDto;
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
                        .title(musical.getTitle())
                        .thumbnailUrl(musical.getThumbnailUrl())
                        .rating(musical.getRating())
                        .description(musical.getDescription())
                        .startDate(musical.getStartDate())
                        .endDate(musical.getEndDate())
                        .runningTime(musical.getRunningTime())
                        .build())), HttpStatus.OK);
    }
}
