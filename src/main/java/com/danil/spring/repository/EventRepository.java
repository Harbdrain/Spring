package com.danil.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.danil.spring.model.Event;

import jakarta.transaction.Transactional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO events (user_id, file_id, status) VALUES(:userId, :fileId, 'ACTIVE')", nativeQuery = true)
    void saveByIds(@Param("userId") Integer userId, @Param("fileId") Integer fileId);

    @Query("FROM Event e LEFT JOIN FETCH e.user u LEFT JOIN FETCH e.file f WHERE e.status != 'DELETED' AND u.status != 'DELETED' AND f.status != 'DELETED'")
    List<Event> findAll();

    @Query("FROM Event e LEFT JOIN FETCH e.user u LEFT JOIN FETCH e.file f WHERE e.id = :id AND e.status != 'DELETED' AND u.status != 'DELETED' AND f.status != 'DELETED'")
    Optional<Event> findById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE Event e SET e.status = 'DELETED' WHERE e.id = :id")
    void deleteById(@Param("id") Integer id);
}
