package com.example.todoapp.logic;

import com.example.todoapp.TaskConfigurationProperties;
import com.example.todoapp.model.*;
import com.example.todoapp.model.projection.GroupReadModel;
import com.example.todoapp.model.projection.GroupTaskWriteModel;
import com.example.todoapp.model.projection.GroupWriteModel;
import com.example.todoapp.model.projection.ProjectWriteModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties configurationProperties;
    private TaskGroupService service;

    public List<Project> readAll () {
        return repository.findAll();
    }

    public Project save(ProjectWriteModel toSave){
        return repository.save(toSave.toProject());
    }

    public GroupReadModel creatGroup(int projectId, LocalDateTime deadline){
        if (!configurationProperties.getTemplate().isAllowMultipleTasks()
                &&
                taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        return repository.findById(projectId).map(project -> {
            var targetGroup = new GroupWriteModel();
            targetGroup.setDescription(project.getDescription());
            targetGroup.setTasks(
                    project.getSteps().stream()
                            .map(step -> {
                                        var task = new GroupTaskWriteModel();
                                        task.setDescription(step.getDescription());
                                        task.setDeadline(deadline.plusDays(step.getDaysToDeadline()));
                                        return task;
                                    }
                                    ) .collect(Collectors.toList())
                    );
                return service.createGroup(targetGroup, project);
                })
                .orElseThrow(
                () -> new IllegalArgumentException("Project with given id not found"));
    }

}
