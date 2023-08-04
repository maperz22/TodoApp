package com.example.todoapp.model;

import com.example.todoapp.model.event.TaskEvent;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter @Setter @NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Task's description must not be null")
    private String description;
    @Setter(value = AccessLevel.NONE)
    private boolean done;
    private LocalDateTime deadline;
    @Embedded
    @Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
    private Audit audit = new Audit();
    @ManyToOne
    @JoinColumn(name = "task_group_id")
    @Setter(value = AccessLevel.PACKAGE) @Getter(value = AccessLevel.PACKAGE)
    private TaskGroup group;

    public Task(String description, LocalDateTime deadline) {
        this(description, deadline, null);
    }

    public Task(String description, LocalDateTime deadline, TaskGroup group){
        this.description = description;
        this.deadline = deadline;
        if(group != null)
            this.group = group;
    }

    public TaskEvent toggle(){
        this.done = !this.done;
        return TaskEvent.changed(this);
    }

    public void updateFrom(final Task source) {
        description = source.description;
        done = source.done;
        deadline = source.getDeadline();
        group = source.getGroup();
    }

}
