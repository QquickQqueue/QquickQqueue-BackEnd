package com.example.qquickqqueue.domain.actor.repository;

import com.example.qquickqqueue.domain.actor.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    Optional<Actor> findByActorName(String name);

    List<Actor> findByActorNameIn(List<String> allActorNames);
}
