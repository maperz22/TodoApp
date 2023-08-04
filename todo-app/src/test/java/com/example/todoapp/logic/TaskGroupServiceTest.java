package com.example.todoapp.logic;

import com.example.todoapp.model.TaskGroup;
import com.example.todoapp.model.TaskGroupRepository;
import com.example.todoapp.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("Should throw IllegalStateException")
    void toggleGroup_GroupHasUndoneTasks_throwsIllegalStateException() {
        // given
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(true);
        // system to test
        var toTest = new TaskGroupService(null, mockTaskRepository);


        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        // then
        assertThat(exception).hasMessageContaining("has undone tasks");

    }

    @Test
    @DisplayName("Should throw IllegalArgumentException")
    void toggleGroup_CannotFindGroupById_throwsIllegalArgumentException() {
        // given
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);

        var mockGroupRepo = mock(TaskGroupRepository.class);
        when(mockGroupRepo.findById(anyInt())).thenReturn(Optional.empty());
        // system to test
        var toTest = new TaskGroupService(mockGroupRepo, mockTaskRepository);


        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        // then
        assertThat(exception).hasMessageContaining("with given id");

    }

    @Test
    @DisplayName("Should be ok")
    void toggleGroup_EverythingWentWell() {
        // given
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);

        var group = new TaskGroup();
        var resultDone = group.isDone();

        var mockGroupRepo = mock(TaskGroupRepository.class);
        when(mockGroupRepo.findById(anyInt())).thenReturn(Optional.of(group));
        // system to test
        var toTest = new TaskGroupService(mockGroupRepo, mockTaskRepository);


        // when
        toTest.toggleGroup(group.getId());

        // then
        assertThat(group.isDone()).isEqualTo(!resultDone);

    }




}