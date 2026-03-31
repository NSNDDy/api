package org.example.demojwt.info.repository;

import org.example.demojwt.info.entity.Transaction;
import org.example.demojwt.info.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserAndDateBetweenOrderByDateDescCreatedAtDesc(User user, LocalDate startDate, LocalDate endDate);
    List<Transaction> findByUserOrderByDateDescCreatedAtDesc(User user);
}
