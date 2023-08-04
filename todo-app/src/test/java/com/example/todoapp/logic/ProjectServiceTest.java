package com.example.todoapp.logic;

import com.example.todoapp.TaskConfigurationProperties;
import com.example.todoapp.model.*;


import com.example.todoapp.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("Should throw IllegalStateException when configured to allow just 1 group and the other undone groups")
    void creatGroup_noMultipleGroupsConfig_And_undoneGroupExists_throwsIllegalStateException() {
        // given
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(false);
        // system to test
        var toTest = new ProjectService(null, mockGroupRepository, mockConfig, null);

        // when
        var exception = catchThrowable(() -> toTest.creatGroup(0, LocalDateTime.now()));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone group");

    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when there is no project under this ID")
    void creatGroup_noProjectUnderId_throwsIllegalArgumentException() {
        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system to test
        var toTest = new ProjectService(mockRepository, null, mockConfig, null);

        // when
        var exception = catchThrowable(() -> toTest.creatGroup(0, LocalDateTime.now()));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");

    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when only 1 group and there is no groups nor projects under this ID")
    void creatGroup_noMultipleGroupsConfig_And_undoneGroupExists_noProjectUnderId_throwsIllegalArgumentException() {
        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // and
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system to test
        var toTest = new ProjectService(mockRepository, mockGroupRepository, mockConfig, null);

        // when
        var exception = catchThrowable(() -> toTest.creatGroup(0, LocalDateTime.now()));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");

    }

    @Test
    @DisplayName("Should create a new group from project")
    void createGroup_configurationOk_existingProject_createsAndSavesGroup(){
        // given
        var today = LocalDate.now().atStartOfDay();
        // and
        var project = projectWith("bar", Set.of(-1,-2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        // and
        var inMemoryGroupRepository = inMemoryGroupRepository();
        var serviceWithInMemRepo = new TaskGroupService(inMemoryGroupRepository, null);
        int countBeforeCall = inMemoryGroupRepository.count();
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system to test
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepository, mockConfig, serviceWithInMemRepo);

        // when
        GroupReadModel result =  toTest.creatGroup(1, today);

        // then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepository.count());
    }



    private inMemoryGroupRepository inMemoryGroupRepository() {
        return new inMemoryGroupRepository();
    }


    private static class inMemoryGroupRepository implements TaskGroupRepository{

                private int index = 0;
                private Map<Integer, TaskGroup> map = new HashMap<>();
                public int count() {
                    return map.values().size();
                }

                @Override
                public List<TaskGroup> findAll() {
                    return new ArrayList<TaskGroup>(map.values());
                }

                @Override
                public Optional<TaskGroup> findById(Integer Id) {
                    return Optional.ofNullable(map.get(Id));
                }

                @Override
                public TaskGroup save(TaskGroup entity) {
                    if(entity.getId() == 0) {
                        try {
                            var field = TaskGroup.class.getDeclaredField("id");
                            field.setAccessible(true);
                            field.set(entity, ++index);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    map.put(entity.getId(), entity);

                    return entity;
                }

        @Override
        public void deleteById(Integer Id) {
            map.remove(Id);
        }


        @Override
                public boolean existsByDoneIsFalseAndProject_Id(Integer ProjectId) {
                    return map.values().stream().filter(group -> !group.isDone())
                            .anyMatch(group -> group.getProject() != null && group.getProject().getId() == ProjectId);
                }

        @Override
        public boolean existsByDescription(String description) {
            return map.values().stream().anyMatch(group -> group.getDescription().equals(description));
        }

        @Override
        public boolean existsById(Integer GroupId) {
            return false;
        }
    };


    private TaskGroupRepository groupRepositoryReturning(boolean t) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(t);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(final boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);

        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {
        Set<ProjectStep> steps = daysToDeadline.stream()
            .map(days -> {
                var step = mock(ProjectStep.class);
                when(step.getDescription()).thenReturn("foo");
                when(step.getDaysToDeadline()).thenReturn(days);
                return step;
            })
            .collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

}