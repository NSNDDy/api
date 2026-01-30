package org.example.demojwt.info.repository;

import org.example.demojwt.info.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRoomIdOrderByCreatedAtAsc(String roomId);
}
