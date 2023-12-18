package com.example.qquickqqueue.domain.members.repository;

import com.example.qquickqqueue.domain.members.entity.Members;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembersRepository extends JpaRepository<Members, String> {

	Optional<Members> findByEmail(String email);
}
