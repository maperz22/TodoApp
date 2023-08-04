package com.example.todoapp.model.projection;

import com.example.todoapp.model.Task;
import com.example.todoapp.model.TaskGroup;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Getter @Setter
public class GroupReadModel {
    private int id;
    private String description;
    /**
     * Deadline ostatniego taska w grupie
     */
    private LocalDateTime deadline;
    private Set<GroupTaskReadModel> tasks;

    public GroupReadModel(TaskGroup source) {
        id = source.getId();
        description = source.getDescription();
        source.getTasks().stream()
                .map(Task::getDeadline)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .ifPresent(localDateTime -> deadline = localDateTime);

        tasks = source.getTasks().stream()
                .map(GroupTaskReadModel::new).
                collect(Collectors.toSet());
    }
}
