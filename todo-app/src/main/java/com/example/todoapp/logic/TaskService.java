package com.example.todoapp.logic;

import com.example.todoapp.model.Task;
import com.example.todoapp.model.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository repository;

    @Async
    public CompletableFuture<List<Task>> findAllAsync(){
        return CompletableFuture.supplyAsync(repository::findAll);
    }

}
