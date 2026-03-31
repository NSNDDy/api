package org.example.demojwt.info.repository;

import org.example.demojwt.info.entity.SavingsGoal;
import org.example.demojwt.info.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {
    List<SavingsGoal> findByUser(User user);
}
