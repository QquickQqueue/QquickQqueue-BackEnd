package com.example.qquickqqueue.domain.musical.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.example.qquickqqueue.domain.actor.dto.ActorResponseDto;
import com.example.qquickqqueue.domain.enumPackage.Grade;
import com.example.qquickqqueue.domain.enumPackage.Rating;
import com.example.qquickqqueue.domain.musical.dto.MusicalResponseDto;
import com.example.qquickqqueue.domain.musical.dto.MusicalRoundInfoResponseDto;
import com.example.qquickqqueue.domain.musical.dto.MusicalSaveRequestDto;
import com.example.qquickqqueue.domain.musical.entity.Musical;
import com.example.qquickqqueue.domain.musical.service.MusicalService;
import com.example.qquickqqueue.domain.scheduleSeat.dto.ScheduleSeatResponseDto;
import com.example.qquickqqueue.util.Message;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith({MockitoExtension.class})
class MusicalControllerTest {
    @Mock
    private MusicalService musicalService;
    @InjectMocks
    private MusicalController musicalController;

    Musical musical = Musical.builder()
            .title("musical.getTitle()")
            .thumbnailUrl("musical.getThumbnailUrl()")
            .rating(Rating.PG12)
            .description("musical.getDescription()")
            .startDate(LocalDate.of(2023, 3, 4))
            .endDate(LocalDate.of(2023, 5, 4))
            .runningTime(LocalTime.of(3, 3, 3)).build();

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
        @DisplayName("readMusicals Method Test")
        void readMusicalsTest() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            Page<MusicalResponseDto> musicalResponseDtoPage = new PageImpl<>(musicalList.stream().map(musical -> MusicalResponseDto.builder()
                    .title(musical.getTitle())
                    .thumbnailUrl(musical.getThumbnailUrl())
                    .rating(musical.getRating())
                    .description(musical.getDescription())
                    .startDate(musical.getStartDate())
                    .endDate(musical.getEndDate())
                    .runningTime(musical.getRunningTime())
                    .build()).toList());

            ResponseEntity<Message> responseEntity = new ResponseEntity<>(new Message("조회 성공", musicalResponseDtoPage), HttpStatus.OK);
            when(musicalService.readMusicals(pageable)).thenReturn(responseEntity);

            // when
            ResponseEntity<Message> response = musicalController.readMusicals(pageable);
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
        @DisplayName("readMusical Method Test")
        void readMusicalTest() {
            // given
            Long musicalId = 1L;

            ResponseEntity<Message> responseEntity = new ResponseEntity<>(new Message("조회 성공", MusicalResponseDto.builder()
                    .id(1L).build()), HttpStatus.OK);

            when(musicalService.readMusical(musicalId)).thenReturn(responseEntity);

            // when
            ResponseEntity<Message> response = musicalController.readMusical(musicalId);

            // then
            assertEquals(responseEntity.getBody().getMessage(), response.getBody().getMessage());
            assertEquals(responseEntity.getBody().getData(), response.getBody().getData());
        }
    }

    @Nested
    @DisplayName("readMusicalRoundInfoByDate Method Test")
    class ReadMusicalRoundInfoByDate {
        @Test
        @DisplayName("readMusicalRoundInfoByDate Method Test")
        void readMusicalRoundInfoByDateTest() {
            // given
            ResponseEntity<Message> responseEntity = new ResponseEntity<>(new Message("조회 성공", MusicalRoundInfoResponseDto.builder()
                    .sumVIP(2).sumR(2).sumS(3).sumA(4).actors(List.of(ActorResponseDto.builder().actorName("name").build()))
                    .build()), HttpStatus.OK);

            Long scheduleId = 1L;

            when(musicalService.readMusicalRoundInfoByDate(scheduleId)).thenReturn(responseEntity);

            // when
            ResponseEntity<Message> response = musicalController.readMusicalInfoByDate(scheduleId);

            // then
            assertEquals(responseEntity.getBody().getMessage(), response.getBody().getMessage());
            assertEquals(responseEntity.getBody().getData(), response.getBody().getData());
        }
    }

    @Nested
    @DisplayName("readMusicalSeatInfo Method Test")
    class ReadMusicalSeatInfo {
        @Test
        @DisplayName("readMusicalSeatInfo Method Test")
        void readMusicalSeatInfoTest() {
            // given
            ScheduleSeatResponseDto scheduleSeatResponseDto1 = ScheduleSeatResponseDto.builder()
                    .id(1L)
                    .isReserved(false)
                    .grade(Grade.R)
                    .price(12222)
                    .columnNum(2)
                    .rowNum(3)
                    .build();
            ScheduleSeatResponseDto scheduleSeatResponseDto2 = ScheduleSeatResponseDto.builder()
                    .id(2L)
                    .isReserved(false)
                    .grade(Grade.VIP)
                    .price(1222332)
                    .columnNum(34)
                    .rowNum(35)
                    .build();

            Long scheduleId = 1L;

            ResponseEntity<Message> responseEntity = new ResponseEntity<>(new Message("조회 성공",
                    List.of(scheduleSeatResponseDto1, scheduleSeatResponseDto2)), HttpStatus.OK);

            when(musicalService.readMusicalSeatInfo(scheduleId)).thenReturn(responseEntity);

            // when
            ResponseEntity<Message> response = musicalController.readMusicalSeatInfo(scheduleId);

            // then
            assertEquals(responseEntity.getBody().getMessage(), response.getBody().getMessage());
            assertEquals(responseEntity.getBody().getData(), response.getBody().getData());
        }
    }

    @Nested
    @DisplayName("searchMusicals Method Test")
    class SearchMusicals {
        @Test
        void searchMusicalsTest() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            String searchKeyword = "musical";

            Page<MusicalResponseDto> musicalResponseDtoPage = new PageImpl<>(musicalList.stream().map(musical -> MusicalResponseDto.builder()
                    .title(musical.getTitle())
                    .thumbnailUrl(musical.getThumbnailUrl())
                    .rating(musical.getRating())
                    .description(musical.getDescription())
                    .startDate(musical.getStartDate())
                    .endDate(musical.getEndDate())
                    .runningTime(musical.getRunningTime())
                    .build()).toList());

            ResponseEntity<Message> responseEntity = new ResponseEntity<>(new Message("조회 성공", musicalResponseDtoPage), HttpStatus.OK);
            when(musicalService.searchMusicals(searchKeyword, pageable)).thenReturn(responseEntity);

            // when
            ResponseEntity<Message> response = musicalController.searchMusicals(searchKeyword, pageable);
            Page<MusicalResponseDto> responseValue = (Page<MusicalResponseDto>) response.getBody().getData();

            // then
            assertEquals("조회 성공", response.getBody().getMessage());
            assertEquals(musicalResponseDtoPage.getTotalPages(), responseValue.getTotalPages());
            assertEquals(musicalResponseDtoPage.getContent().get(1).getTitle(), responseValue.getContent().get(1).getTitle());
        }
    }

    @Nested
    @DisplayName("saveMusical Method Test")
    class SaveMusical {
        @Test
        void saveMusicalTest() {
            // given
            MusicalSaveRequestDto musicalSaveRequestDto = MusicalSaveRequestDto.builder().build();
            ResponseEntity<Message> responseEntity = new ResponseEntity<>(new Message("뮤지컬 등록 성공", null),
                HttpStatus.OK);
            when(musicalService.saveMusical(musicalSaveRequestDto)).thenReturn(responseEntity);

            // when
            ResponseEntity<Message> response = musicalController.saveMusical(musicalSaveRequestDto);

            // then
            assertEquals(responseEntity, response);
        }
    }
}