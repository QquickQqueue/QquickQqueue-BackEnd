package com.example.qquickqqueue.domain.stadium.repository;

import com.example.qquickqqueue.domain.stadium.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Long> {
}
