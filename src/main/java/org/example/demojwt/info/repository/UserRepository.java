package org.example.demojwt.info.repository;

import org.example.demojwt.info.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    List<User> findAllByUsernameContaining(String username);

    @Query(value = "SELECT COUNT(*) FROM users", nativeQuery = true)
    int counAllUser();
}
