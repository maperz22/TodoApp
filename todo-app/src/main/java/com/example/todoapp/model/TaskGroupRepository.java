package com.example.todoapp.model;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {
    List<TaskGroup> findAll();
    Optional<TaskGroup> findById(Integer Id);
    TaskGroup save(TaskGroup entity);

    void deleteById(Integer Id);

    boolean existsByDoneIsFalseAndProject_Id(Integer ProjectId);

    boolean existsByDescription(String description);

    boolean existsById(Integer GroupId);

}
