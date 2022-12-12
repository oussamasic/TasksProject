package com.management.task.controller;


import com.management.task.dto.Task;
import com.management.task.service.TaskService;
import com.management.task.utils.CommonConstants;
import com.management.task.utils.UtilsFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api" +CommonConstants.TASKS)
public class TaskController {

    private TaskService taskService;

    private static final Logger LOGGER = Logger.getLogger(TaskController.class.getName());

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(CommonConstants.ALL_TASKS)
    public List<Task> getAll() {
        LOGGER.info("Get All Tasks");
        return taskService.getAll();
    }

    @GetMapping(CommonConstants.COMPLETE_TASKS)
    public List<Task> getAllCompleteTasks() {
        LOGGER.info("Get All Complete Tasks");
        return taskService.getAllCompleteTasks();
    }

    @GetMapping(CommonConstants.INCOMPLETE_TASKS)
    public List<Task> getAllInCompleteTasks() {
        LOGGER.info("Get All InComplete Tasks");
        return taskService.getAllInCompleteTasks();
    }

    @GetMapping(CommonConstants.PATH_ID)
    public Task getTaskById(final @PathVariable("taskId") String id) {
        LOGGER.log(Level.FINE, "Get task by Id {} " , id);
        return taskService.getTaskById(id);
    }

    @PutMapping(CommonConstants.PATH_ID)
    public void updateTask(final @PathVariable("taskId") String id, @RequestBody final Task task) {
        LOGGER.log(Level.FINE, "Update task by Id {} " , id);
        UtilsFunctions.checkDescription(task);
        taskService.updateTask(task,id);
    }

    @PutMapping(CommonConstants.PATH_ID + "/complete")
    public void completeTask(final @PathVariable("taskId") String id) {
        LOGGER.log(Level.FINE, "Complete a task by Id {} " , id);
        taskService.completeTask(id);
    }

    @PutMapping(CommonConstants.PATH_ID + "/incomplete")
    public void inCompleteTask(final @PathVariable("taskId") String id) {
        LOGGER.log(Level.FINE, "inComplete a task by Id {} " , id);
        taskService.inCompleteTask(id);
    }

    @DeleteMapping(CommonConstants.PATH_ID + "/delete")
    public void deleteTask(final @PathVariable("taskId") String id) {
        LOGGER.log(Level.FINE, "Delete task by Id {} " ,id);
        taskService.deleteTask(id);
    }

    @PostMapping()
    public void createTask(@RequestBody final Task task) {
        LOGGER.info("Create a Task");
        UtilsFunctions.checkDescription(task);
        taskService.createTask(task);
    }

}
