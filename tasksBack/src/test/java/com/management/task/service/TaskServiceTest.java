package com.management.task.service;

import com.management.task.converter.TaskConverter;
import com.management.task.dto.Task;
import com.management.task.model.TaskModel;
import com.management.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class TaskServiceTest {


    private TaskService taskService;

    private TaskRepository taskRepository;


    @BeforeEach
    public void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void testCreateTask() {
        Task task = new Task("id", "description", false);
        taskService.createTask(task);
        verify(taskRepository).save(ArgumentMatchers.refEq(TaskConverter.convertTaskDtoToTaskModel(task)));
    }

    @Test
    void testGetAllTasks() {
        taskService.getAll();
        verify(taskRepository).findAll();
    }

    @Test
    void testGetAllInCompleteTasks() {
        taskService.getAllInCompleteTasks();
        verify(taskRepository).findByComplete(false);
    }

    @Test
    void testGetAllCompleteTasks() {
        taskService.getAllCompleteTasks();
        verify(taskRepository).findByComplete(true);
    }

}
