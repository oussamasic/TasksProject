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
import com.management.task.utils.CommonConstants;
import com.management.task.utils.UtilsFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api" +CommonConstants.TASKS)
public class TaskController {

    private final TaskService taskService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    public List<Task> getAllTasks(@RequestParam(required = false) String status) {

        List<Task> taskList = new ArrayList<>();

        if(Objects.isNull(status)) {
            LOGGER.info("Get All Tasks");
            taskList =  taskService.getAll();
        }

        else if(status.equals("complete")) {
            LOGGER.info("Get All Complete Tasks");
            taskList =  taskService.getAllCompleteTasks();
        }

        else if(status.equals("incomplete")) {
            LOGGER.info("Get All InComplete Tasks");
            taskList = taskService.getAllInCompleteTasks();
        }

        return taskList;
    }

    @GetMapping(CommonConstants.PATH_ID)
    public Task getTaskById(final @PathVariable("taskId") String id) {
        LOGGER.info("Get task by Id {} " , id);
        return taskService.getTaskById(id);
    }

    @PutMapping(CommonConstants.PATH_ID)
    public void updateTask(final @PathVariable("taskId") String id, @RequestBody @Valid final Task task) {
        LOGGER.info("Update task by Id {} ", id);
        UtilsFunctions.checkDescription(task);
        taskService.updateTask(task,id);
    }

    @PutMapping(CommonConstants.PATH_ID + "/complete")
    public void completeTask(final @PathVariable("taskId") String id) {
        LOGGER.info("Complete a task by Id {} ", id);
        taskService.completeTask(id);
    }

    @PutMapping(CommonConstants.PATH_ID + "/incomplete")
    public void inCompleteTask(final @PathVariable("taskId") String id) {
        LOGGER.info("inComplete a task by Id {} " , id);
        taskService.inCompleteTask(id);
    }

    @DeleteMapping(CommonConstants.PATH_ID + "/delete")
    public void deleteTask(final @PathVariable("taskId") String id) {
        LOGGER.info("Delete task by Id {} " ,id);
        taskService.deleteTask(id);
    }

    @PostMapping()
    public void createTask(final @Valid @RequestBody Task task) {
        LOGGER.info("Create a Task");
        taskService.createTask(task);
    }

    @GetMapping()
    public List<Task> getAllPaginatedTasks(@RequestParam(required = false) String status,
                                           @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "0") int page) {

        List<Task> taskList = new ArrayList<>();

        if(Objects.isNull(status)) {
            LOGGER.info("Get All paginated Tasks");
            taskList =  taskService.getAllPaginatedTasks(size, page);
        }

        else if(status.equals("complete")) {
            LOGGER.info("Get All paginated Complete Tasks");
            taskList =  taskService.getAllPaginatedCompletedTasks(size, page);
        }

        else if(status.equals("incomplete")) {
            LOGGER.info("Get All paginated InComplete Tasks");
            taskList = taskService.getAllPaginatedInCompletedTasks(size, page);
        }

        return taskList;
    }

}
