package com.example.qquickqqueue.domain.casting.repository;

import com.example.qquickqqueue.domain.casting.entity.Casting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CastingRepository extends JpaRepository<Casting, Long> {
    List<Casting> findAllBySchedule_Id(Long scheduleId);
}
