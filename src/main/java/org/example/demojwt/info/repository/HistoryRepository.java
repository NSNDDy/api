package org.example.demojwt.info.repository;

import org.example.demojwt.info.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT * FROM messages WHERE room_id = :roomId ORDER BY id ASC", nativeQuery = true)
    List<Message> findAllbyMessage(@Param("roomId") String roomId);
}
