package com.example.qquickqqueue.domain.musical.repository;

import com.example.qquickqqueue.domain.musical.entity.Musical;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicalRepository extends JpaRepository<Musical, Long> {
    Page<Musical> findAllByTitleContaining(String title, Pageable pageable);
}
