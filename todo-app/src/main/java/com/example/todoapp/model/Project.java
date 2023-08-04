package com.example.todoapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "projects")
@Getter @Setter @NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Project's description must not be null")
    private String description;

    @OneToMany(mappedBy = "project")
    @Setter(value = AccessLevel.PACKAGE)
    private Set<TaskGroup> groups;

    @OneToMany(cascade = CascadeType.ALL ,mappedBy = "project")
    private Set<ProjectStep> steps;


}
