package com.example.todoapp.logic;


import com.example.todoapp.model.Project;
import com.example.todoapp.model.TaskGroup;
import com.example.todoapp.model.TaskGroupRepository;
import com.example.todoapp.model.TaskRepository;
import com.example.todoapp.model.projection.GroupReadModel;
import com.example.todoapp.model.projection.GroupWriteModel;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    public GroupReadModel createGroup(GroupWriteModel source) {
        return createGroup(source, null);
    }

    GroupReadModel createGroup(GroupWriteModel source, Project project) {
        TaskGroup result = repository.save(source.toGroup(project));
        return new GroupReadModel (result);
    }

    public void deleteById(int groupId) {
        if(!repository.existsById(groupId)) {
            throw new IllegalArgumentException("Group does not exists");
        } else {
            repository.deleteById(groupId);
        }
    }

    public List<GroupReadModel> readAll(){
        return repository.findAll().stream().map(GroupReadModel::new).collect(Collectors.toList());
    }

    public void toggleGroup (int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Do all the task first!");
        }
        TaskGroup result = repository.findById(groupId).orElseThrow(
                () -> new IllegalArgumentException("TaskGroup with given id not found")
        );
        result.setDone(!result.isDone());
        repository.save(result);
    }


}
