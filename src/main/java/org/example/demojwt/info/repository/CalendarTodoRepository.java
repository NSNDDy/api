package org.example.demojwt.info.repository;

import org.example.demojwt.info.entity.CalendarTodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarTodoRepository extends JpaRepository<CalendarTodo, Long> {
    List<CalendarTodo> findByUser_IdAndDateOrderByCreatedAtAsc(Long userId, LocalDate date);
    List<CalendarTodo> findByUser_IdOrderByDateAscCreatedAtAsc(Long userId);
    Optional<CalendarTodo> findByIdAndUser_Id(Long id, Long userId);
}
