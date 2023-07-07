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
import com.management.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

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

}
