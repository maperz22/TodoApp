package com.example.todoapp.model.projection;

import com.example.todoapp.model.Project;
import com.example.todoapp.model.ProjectStep;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
@Setter @Getter
public class ProjectWriteModel {
    @NotBlank(message = "Project's description must not be null")
    private String description;
    @Valid
    private List<ProjectStep> steps = new ArrayList<>();

    public ProjectWriteModel(){
        steps.add(new ProjectStep());
    }

    public Project toProject() {
        var result = new Project();
        result.setDescription(description);
        steps.forEach(step -> step.setProject(result));
        result.setSteps(new HashSet<>(steps));
        return result;
    }



}
