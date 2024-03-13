package com.example.qquickqqueue.domain.scheduleSeat.repository.impl;

import com.example.qquickqqueue.domain.scheduleSeat.entity.QScheduleSeat;
import com.example.qquickqqueue.domain.scheduleSeat.entity.ScheduleSeat;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScheduleSeatCustomRepositoryImpl Test")
class ScheduleSeatCustomRepositoryImplTest {
    @Mock
    private JPAQueryFactory queryFactory;
    @Mock
    private JPAQuery<ScheduleSeat> jpaQuery;
    @InjectMocks
    private ScheduleSeatCustomRepositoryImpl scheduleSeatCustomRepositoryImpl;

    private final Long scheduleId = 1L;

    @BeforeEach
    void setUp() {
        when(queryFactory.selectFrom(any(QScheduleSeat.class))).thenReturn(jpaQuery);
    }

    @Test
    @DisplayName("findAllBySchedule_IdAndIsReserved Test")
    void findAllBySchedule_IdAndIsReserved() {
        // given
        List<ScheduleSeat> scheduleSeatList = Arrays.asList(mock(ScheduleSeat.class), mock(ScheduleSeat.class));

        when(jpaQuery.join(any(EntityPath.class))).thenReturn(jpaQuery);
        when(jpaQuery.fetchJoin()).thenReturn(jpaQuery);
        when(jpaQuery.where((Predicate) any())).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(scheduleSeatList);

        // when
        List<ScheduleSeat> result = scheduleSeatCustomRepositoryImpl.findAllBySchedule_IdAndIsReserved(scheduleId, true);

        // then
        assertNotNull(result);
        assertEquals(scheduleSeatList.size(), result.size());
        verify(jpaQuery, times(1)).fetch();
    }

    @Test
    @DisplayName("findAllBySchedule_Id Test")
    void findAllBySchedule_Id() {
        // given
        List<ScheduleSeat> scheduleSeatList = Arrays.asList(mock(ScheduleSeat.class), mock(ScheduleSeat.class));

        when(jpaQuery.join(any(EntityPath.class))).thenReturn(jpaQuery);
        when(jpaQuery.fetchJoin()).thenReturn(jpaQuery);
        when(jpaQuery.where((Predicate) any())).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(scheduleSeatList);

        // when
        List<ScheduleSeat> result = scheduleSeatCustomRepositoryImpl.findAllBySchedule_Id(scheduleId);

        // then
        assertNotNull(result);
        assertEquals(scheduleSeatList.size(), result.size());
        verify(jpaQuery, times(1)).fetch();
    }

    @Test
    @DisplayName("findById Test")
    void findById() {
        // given
        Long seatId = 1L;
        ScheduleSeat scheduleSeat = mock(ScheduleSeat.class);
        when(jpaQuery.where((Predicate) any())).thenReturn(jpaQuery);
        when(jpaQuery.fetchOne()).thenReturn(scheduleSeat);

        // when
        Optional<ScheduleSeat> result = scheduleSeatCustomRepositoryImpl.findById(seatId);

        // then
        assertTrue(result.isPresent());
        assertEquals(scheduleSeat, result.get());
        verify(jpaQuery, times(1)).fetchOne();
    }
}
