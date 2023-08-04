package com.example.todoapp.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Embeddable
class Audit {
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    @PrePersist
    void prePersist () {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate(){
        updatedOn = LocalDateTime.now();
    }
}
