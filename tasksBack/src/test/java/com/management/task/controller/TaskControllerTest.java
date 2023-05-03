package com.management.task.controller;

import com.management.task.dto.Task;
import com.management.task.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class TaskControllerTest {

    @MockBean
    TaskService taskService;

    private TaskController taskController;

    @BeforeEach
    public void setUp() {
        taskController = new TaskController(taskService);
    }

    @Test
    void when_getTaskById_ok_should_return_ok() {

        Task expectedResponse = new Task();
        Mockito
            .when(taskService.getTaskById("id"))
            .thenReturn(expectedResponse);
        Task responseDto = taskController.getTaskById("id");
        Assertions.assertEquals(responseDto, expectedResponse);
    }

    @Test
    void when_getAll_ok_should_return_ok() {

        List<Task> expectedResponse = new ArrayList<>();
        Mockito
            .when(taskService.getAll())
            .thenReturn(expectedResponse);

        List<Task> responseDto = taskController.getAll();
        Assertions.assertEquals(responseDto, expectedResponse);
    }

    @Test
    void testCreateTask() {
        Task task = new Task();
        task.setDescription(null);
        taskController.createTask(task);
        verify(taskService).createTask(task);
    }

    @Test
    void testUpdateTask() {
        Task task = new Task();
        task.setDescription("description");
        taskController.updateTask("taskId", task);
        verify(taskService).updateTask(task, "taskId");
    }

    @Test
    void when_getAllCompleteTasks_ok_should_return_ok() {

        List<Task> expectedResponse = new ArrayList<>();
        Mockito
            .when(taskService.getAllCompleteTasks())
            .thenReturn(expectedResponse);

        List<Task> responseDto = taskController.getAllCompleteTasks();
        Assertions.assertEquals(responseDto, expectedResponse);
    }

    @Test
    void testDeleteTaskById() {
        taskController.deleteTask("taskId");
        verify(taskService).deleteTask("taskId");
    }

    @Test
    void when_getAllInCompleteTasks_ok_should_return_ok() {

        List<Task> expectedResponse = new ArrayList<>();
        Mockito
            .when(taskService.getAllInCompleteTasks())
            .thenReturn(expectedResponse);

        List<Task> responseDto = taskController.getAllInCompleteTasks();
        Assertions.assertEquals(responseDto, expectedResponse);
    }
}
