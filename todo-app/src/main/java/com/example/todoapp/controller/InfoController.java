package com.example.todoapp.controller;

import com.example.todoapp.TaskConfigurationProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/info")
@AllArgsConstructor
public class InfoController {
    private DataSourceProperties dataSource;
    private TaskConfigurationProperties myProp;


    @GetMapping("/url")
    String url(){
        return dataSource.getUrl();
    }


    @GetMapping("/prop")
    boolean myProp(){
        return myProp.getTemplate().isAllowMultipleTasks();
    }
}
