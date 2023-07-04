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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api" +CommonConstants.TASKS)
public class TaskController {

    private final TaskService taskService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(CommonConstants.ALL_TASKS)
    public List<Task> getAll() {
        LOGGER.debug("Get All Tasks");
        return taskService.getAll();
    }

    @GetMapping(CommonConstants.COMPLETE_TASKS)
    public List<Task> getAllCompleteTasks() {
        LOGGER.debug("Get All Complete Tasks");
        return taskService.getAllCompleteTasks();
    }

    @GetMapping(CommonConstants.INCOMPLETE_TASKS)
    public List<Task> getAllInCompleteTasks() {
        LOGGER.debug("Get All InComplete Tasks");
        return taskService.getAllInCompleteTasks();
    }

    @GetMapping(CommonConstants.PATH_ID)
    public Task getTaskById(final @PathVariable("taskId") String id) {
        LOGGER.debug("Get task by Id {} " , id);
        return taskService.getTaskById(id);
    }

    @PutMapping(CommonConstants.PATH_ID)
    public void updateTask(final @PathVariable("taskId") String id, @RequestBody @Valid final Task task) {
        LOGGER.debug("Update task by Id {} ", id);
        UtilsFunctions.checkDescription(task);
        taskService.updateTask(task,id);
    }

    @PutMapping(CommonConstants.PATH_ID + "/complete")
    public void completeTask(final @PathVariable("taskId") String id) {
        LOGGER.debug("Complete a task by Id {} ", id);
        taskService.completeTask(id);
    }

    @PutMapping(CommonConstants.PATH_ID + "/incomplete")
    public void inCompleteTask(final @PathVariable("taskId") String id) {
        LOGGER.debug("inComplete a task by Id {} " , id);
        taskService.inCompleteTask(id);
    }

    @DeleteMapping(CommonConstants.PATH_ID + "/delete")
    public void deleteTask(final @PathVariable("taskId") String id) {
        LOGGER.debug("Delete task by Id {} " ,id);
        taskService.deleteTask(id);
    }

    @PostMapping()
    public void createTask(final @Valid @RequestBody Task task) {
        LOGGER.debug("Create a Task");
        taskService.createTask(task);
    }

}
