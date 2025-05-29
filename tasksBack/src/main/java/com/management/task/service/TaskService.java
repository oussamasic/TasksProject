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
import com.management.task.dto.User;
import com.management.task.exceptions.NotFoundException;
import com.management.task.exceptions.UnAuthorizedException;
import com.management.task.model.TaskModel;
import com.management.task.repository.TaskRepository;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Getter
@Setter
public class TaskService {

    private final TaskRepository taskRepository;

    private static final String TASK_NOT_FOUND = "Task not found";

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAll() {
        LOGGER.debug("get all Task");
        return taskRepository.findAll().stream().map(TaskConverter::convertTaskModelToTaskDto)
            .toList();

        }

    public void createTask(Task taskDto) {
        LOGGER.debug("create a task");
        User authenticatedUser = getAuthenticatedUser();
        if(Objects.isNull(authenticatedUser)) {
            LOGGER.error("You are not authorized to download the reports");
            throw new UnAuthorizedException("You are not authorized to download the reports");
        }

        taskDto.setUserId(authenticatedUser.getId());
        TaskModel taskModel = TaskConverter.convertTaskDtoToTaskModel(taskDto);
        taskRepository.save(taskModel);
    }

    public Task getTaskById(String id) {
        LOGGER.debug("get a task by id : {}", id);
        Optional<TaskModel> taskModelOptional = taskRepository.findById(id);
        if(taskModelOptional.isEmpty()) {
            LOGGER.error(TASK_NOT_FOUND);
            throw new NotFoundException(TASK_NOT_FOUND);
        }
        return TaskConverter.convertTaskModelToTaskDto(taskModelOptional.get());
    }
    public void updateTask(Task taskDto, String id) {
        LOGGER.debug("update a task");
        Optional<TaskModel> taskDtoOptional = taskRepository.findById(id);
        if(taskDtoOptional.isEmpty()) {
            LOGGER.error(TASK_NOT_FOUND);
            throw new NotFoundException(TASK_NOT_FOUND);
        }
        taskDtoOptional.get().setDescription(taskDto.getDescription());
        taskRepository.save(taskDtoOptional.get());

    }

    public List<Task> getAllCompleteTasks() {
        LOGGER.debug("get all completed task");
        return taskRepository.findByComplete(true).stream().map(TaskConverter::convertTaskModelToTaskDto)
            .toList();
    }

    public List<Task> getAllInCompleteTasks() {
        LOGGER.debug("get all uncompleted tasks");
        return taskRepository.findByComplete(false).stream().map(TaskConverter::convertTaskModelToTaskDto)
                .toList();
    }

    public void completeTask(String id) {
        LOGGER.debug("complete th task with id : {}", id);
        Optional<TaskModel> taskDtoOptional = taskRepository.findById(id);
        if(taskDtoOptional.isEmpty()) {
            LOGGER.error(TASK_NOT_FOUND);
            throw new NotFoundException(TASK_NOT_FOUND);
        }
        taskDtoOptional.get().setComplete(true);
        taskRepository.save(taskDtoOptional.get());
    }

    public void inCompleteTask(String id) {
        LOGGER.debug("make incomplete th task with id : {}", id);
        Optional<TaskModel> taskDtoOptional = taskRepository.findById(id);
        if(taskDtoOptional.isEmpty()) {
            LOGGER.error(TASK_NOT_FOUND);
            throw new NotFoundException(TASK_NOT_FOUND);
        }
        taskDtoOptional.get().setComplete(false);
        taskRepository.save(taskDtoOptional.get());
    }

    public void deleteTask(String id) {
        LOGGER.debug("delete th task with id : {}", id);
        Optional<TaskModel> taskDtoOptional = taskRepository.findById(id);
        if(taskDtoOptional.isEmpty()) {
            LOGGER.error(TASK_NOT_FOUND);
            throw new NotFoundException(TASK_NOT_FOUND);
        }
        taskRepository.deleteById(id);
    }

    public List<Task> getAllPaginatedTasks(int size, int page) {
        LOGGER.debug("get all Paginated Task");
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAll(pageable).stream().map(TaskConverter::convertTaskModelToTaskDto)
                .toList();

    }

    public List<Task> getAllPaginatedCompletedTasks(int size, int page) {
        LOGGER.debug("get all Paginated Completed Task");
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findByComplete(true, pageable).stream().map(TaskConverter::convertTaskModelToTaskDto)
                .toList();

    }

    public List<Task> getAllPaginatedInCompletedTasks(int size, int page) {
        LOGGER.debug("get all Paginated InCompleted Task");
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findByComplete(false, pageable).stream().map(TaskConverter::convertTaskModelToTaskDto)
                .toList();

    }

    public User getAuthenticatedUser() {
        LocalDateTime dd = LocalDateTime.now();
        Date tt = new Date(String.valueOf(dd));
        tt.getTime();
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return (User) auth.getPrincipal();
        }
        throw new UnAuthorizedException("User is not connected");
    }

}
