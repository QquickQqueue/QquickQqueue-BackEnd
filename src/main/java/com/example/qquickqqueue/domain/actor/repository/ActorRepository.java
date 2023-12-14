package com.example.qquickqqueue.domain.actor.repository;

import com.example.qquickqqueue.domain.actor.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
}
