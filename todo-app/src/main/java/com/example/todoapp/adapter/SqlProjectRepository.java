package com.example.todoapp.adapter;

import com.example.todoapp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlProjectRepository extends ProjectRepository,JpaRepository<Project, Integer> {
    @Override
    @Query("from Project g join fetch g.steps")
    List<Project> findAll();

}
