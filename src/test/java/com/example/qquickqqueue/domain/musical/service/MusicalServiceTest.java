package com.example.qquickqqueue.domain.musical.service;

import com.example.qquickqqueue.domain.enumPackage.Rating;
import com.example.qquickqqueue.domain.musical.dto.MusicalResponseDto;
import com.example.qquickqqueue.domain.musical.entity.Musical;
import com.example.qquickqqueue.domain.musical.repository.MusicalRepository;
import com.example.qquickqqueue.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class MusicalServiceTest {
    @Mock
    private MusicalRepository musicalRepository;
    @InjectMocks
    private MusicalService musicalService;

    Musical musical = Musical.builder()
            .id(1L)
            .title("musical.getTitle()")
            .thumbnailUrl("musical.getThumbnailUrl()")
            .rating(Rating.PG12)
            .description("musical.getDescription()")
            .startDate(new Date(2023, 3, 3))
            .endDate(new Date(2023, 3, 3))
            .runningTime(new Time(32)).build();

    List<Musical> musicalList = new ArrayList<>();

    @BeforeEach
    void loop() {
        for (int i = 0; i < 10; i++) {
            musicalList.add(musical);
        }
    }

    @Nested
    @DisplayName("readMusicals Method Test")
    class ReadMusicals {
        @Test
        void readMusicalsTest() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            Page<Musical> musicalPage = new PageImpl<>(musicalList);

            when(musicalRepository.findAll(pageable)).thenReturn(musicalPage);

            Page<MusicalResponseDto> musicalResponseDtoPage = new PageImpl<>(musicalList.stream().map(musical -> MusicalResponseDto.builder()
                    .title(musical.getTitle())
                    .thumbnailUrl(musical.getThumbnailUrl())
                    .rating(musical.getRating())
                    .description(musical.getDescription())
                    .startDate(musical.getStartDate())
                    .endDate(musical.getEndDate())
                    .runningTime(musical.getRunningTime())
                    .build()).toList());

            // when
            ResponseEntity<Message> response = musicalService.readMusicals(pageable);
            Page<MusicalResponseDto> responseValue = (Page<MusicalResponseDto>) response.getBody().getData();

            // then
            assertEquals("조회 성공", response.getBody().getMessage());
            assertEquals(musicalResponseDtoPage.getTotalPages(), responseValue.getTotalPages());
            assertEquals(musicalResponseDtoPage.getContent().get(1).getTitle(), responseValue.getContent().get(1).getTitle());
        }
    }

    @Nested
    @DisplayName("readMusical Method Test")
    class ReadMusical {
        @Test
        @DisplayName("readMusical Method Success Test")
        void readMusicalTest() {
            // given
            Long musicalId = 1L;

            MusicalResponseDto musicalResponseDto = MusicalResponseDto.builder()
                    .id(musical.getId())
                    .title(musical.getTitle())
                    .build();

            when(musicalRepository.findById(musicalId)).thenReturn(Optional.of(musical));

            // when
            ResponseEntity<Message> response = musicalService.readMusical(musicalId);
            MusicalResponseDto responseValue = (MusicalResponseDto) response.getBody().getData();

            // then
            assertEquals("조회 성공", response.getBody().getMessage());
            assertEquals(musicalResponseDto.getId(), responseValue.getId());
            assertEquals(musicalResponseDto.getTitle(), responseValue.getTitle());
        }

        @Test
        @DisplayName("readMusical Method Exception Test")
        void readMusicalExceptionTest() {
            // given
            Long musicalId = 2L;

            when(musicalRepository.findById(musicalId)).thenReturn(Optional.empty());

            // when & then
            assertThrows(IllegalArgumentException.class, () -> musicalService.readMusical(musicalId));
        }
    }
}