package com.example.todoapp.model.projection;

import com.example.todoapp.model.Task;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupTaskReadModel {

    public GroupTaskReadModel(Task source) {
        description = source.getDescription();
        done = source.isDone();
    }

    private String description;
    private boolean done;
}
