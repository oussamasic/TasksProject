/*
 * <OZ TASKS>
 * <project to manage user tasks>
 * Copyright (C) <2023>  <ZEROUALI Oussama>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
    void when_getAllTask_ok_should_return_ok() {

        List<Task> expectedResponse = new ArrayList<>();
        Mockito
            .when(taskService.getAll())
            .thenReturn(expectedResponse);

        List<Task> responseDto = taskController.getAllTasks(null);
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
    void testInCompleteTaskById() {
        taskController.inCompleteTask("taskId");
        verify(taskService).inCompleteTask("taskId");
    }

    @Test
    void testUpdateTask() {
        Task task = new Task();
        task.setDescription("description");
        taskController.updateTask("taskId", task);
        verify(taskService).updateTask(task, "taskId");
    }

    @Test
    void when_getAll_CompletedTasks_ok_should_return_ok() {

        List<Task> expectedResponse = new ArrayList<>();
        Mockito
            .when(taskService.getAllCompleteTasks())
            .thenReturn(expectedResponse);

        List<Task> responseDto = taskController.getAllTasks("complete");
        Assertions.assertEquals(responseDto, expectedResponse);
    }

    @Test
    void testDeleteTaskById() {
        taskController.deleteTask("taskId");
        verify(taskService).deleteTask("taskId");
    }

    @Test
    void testCompleteTaskById() {
        taskController.completeTask("taskId");
        verify(taskService).completeTask("taskId");
    }

    @Test
    void when_getAll_InCompletedTasks_ok_should_return_ok() {

        List<Task> expectedResponse = new ArrayList<>();
        Mockito
            .when(taskService.getAllInCompleteTasks())
            .thenReturn(expectedResponse);

        List<Task> responseDto = taskController.getAllTasks("incomplete");
        Assertions.assertEquals(responseDto, expectedResponse);
    }
}
