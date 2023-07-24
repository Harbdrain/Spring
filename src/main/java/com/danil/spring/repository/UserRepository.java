package com.danil.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.danil.spring.model.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("FROM User u WHERE u.username = :username AND u.status != 'DELETED'")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("FROM User u WHERE u.status != 'DELETED'")
    List<User> findAll();

    @Query("FROM User u LEFT JOIN FETCH u.events e LEFT JOIN FETCH e.file WHERE u.username = :username")
    Optional<User> findByUsernameFull(@Param("username") String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.status = 'DELETED' WHERE u.username = :username")
    void deleteByUsername(@Param("username") String username);
}
