package com.danil.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.danil.spring.model.File;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
    @Query("From File f WHERE f.location = :location AND f.status != 'DELETED'")
    Optional<File> findByLocation(@Param("location") String location);

    @Query("From File f WHERE f.id = :id AND f.status != 'DELETED'")
    Optional<File> findById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE File f SET f.status = 'DELETED' WHERE f.location = :location")
    void deleteByLocation(@Param("location") String location);

    @Query("From File f WHERE f.status != 'DELETED'")
    List<File> findAll();
}
