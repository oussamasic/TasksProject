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

package com.management.task.service;

import com.management.task.converter.TaskConverter;
import com.management.task.dto.Task;
import com.management.task.exceptions.NotFoundException;
import com.management.task.model.TaskModel;
import com.management.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

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
        Task task = new Task("id", "description", false, "userId",
                new Date(),"taskTitle", new Date(), new Date());
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

    @Test
    void testGetTaskById_ok_when_task_is_in_DB() {

        // Given
        String taskId = "taskId";
        TaskModel taskModel = new TaskModel("taskId", "description", false, "userId",
                new Date(),"taskTitle", new Date(), new Date());
        // When
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskModel));
        Task response = taskService.getTaskById(taskId);
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("taskTitle");
        assertThat(response.getDescription()).isEqualTo("description");
        assertThat(response.getId()).isEqualTo(taskId);

    }

    @Test
    void testGetTaskById_ko_when_task_not_in_DB() {

        // Given
        String taskId = "taskId";

        // When
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Then
        assertThatCode(()-> taskService.getTaskById(taskId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Task not found");

    }

    @Test
    void testUpdateTask_ok_when_task_is_in_DB() {

        // Given
        String taskId = "taskId";
        Task task = new Task("taskId", "description", true, "userId",
                new Date(),"taskTitle", new Date(), new Date());
        TaskModel taskModel = new TaskModel("taskId", "description", false, "userId",
                new Date(),"taskTitle", new Date(), new Date());
        // When
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskModel));
        // Then
        assertThatCode(() ->taskService.updateTask(task,taskId))
                .doesNotThrowAnyException();


    }

    @Test
    void testUpdateTask_ko_when_task_not_in_DB() {

        // Given
        String taskId = "taskId";
        Task task = new Task("taskId", "description", true, "userId",
                new Date(),"taskTitle", new Date(), new Date());

        // When
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Then
        assertThatCode(()-> taskService.updateTask(task, taskId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Task not found");

    }

    @Test
    void testCompleteTask_ok_when_task_is_in_DB() {

        // Given
        String taskId = "taskId";
        TaskModel taskModel = new TaskModel("taskId", "description", false, "userId",
                new Date(),"taskTitle", new Date(), new Date());
        // When
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskModel));
        // Then
        assertThatCode(() ->taskService.completeTask(taskId))
                .doesNotThrowAnyException();
        verify(taskRepository).save(taskModel);

    }

    @Test
    void testIncompleteTask_ok_when_task_is_in_DB() {

        // Given
        String taskId = "taskId";
        TaskModel taskModel = new TaskModel("taskId", "description", false, "userId",
                new Date(),"taskTitle", new Date(), new Date());
        // When
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskModel));
        // Then

        assertThatCode(() ->taskService.inCompleteTask(taskId))
                .doesNotThrowAnyException();
        verify(taskRepository).save(taskModel);

    }

    @Test
    void testCompleteTask_ko_when_task_not_in_DB() {

        // Given
        String taskId = "taskId";

        // When
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Then
        assertThatCode(()-> taskService.completeTask(taskId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Task not found");

    }

    @Test
    void testIncompleteTask_ko_when_task_not_in_DB() {

        // Given
        String taskId = "taskId";

        // When
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Then
        assertThatCode(()-> taskService.inCompleteTask(taskId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Task not found");

    }

    @Test
    void testDeleteTask_ok_when_task_is_in_DB() {

        // Given
        String taskId = "taskId";
        TaskModel taskModel = new TaskModel("taskId", "description", false, "userId",
                new Date(),"taskTitle", new Date(), new Date());
        // When
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskModel));
        // Then
        assertThatCode(() ->taskService.deleteTask(taskId))
                .doesNotThrowAnyException();
        verify(taskRepository).deleteById(taskId);

    }

    @Test
    void testDeleteTask_ko_when_task_not_in_DB() {

        // Given
        String taskId = "taskId";

        // When
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Then
        assertThatCode(()-> taskService.deleteTask(taskId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Task not found");

    }

}
