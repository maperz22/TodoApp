package com.example.todoapp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("task")
@Getter @Setter
public class TaskConfigurationProperties {
    private Template template;

    @Setter @Getter
    public static class Template{
        private boolean allowMultipleTasks;
    }
}
