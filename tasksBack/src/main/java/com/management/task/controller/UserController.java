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
import com.management.task.dto.User;
import com.management.task.exceptions.BadRequestException;
import com.management.task.exceptions.UnAuthorizedException;
import com.management.task.service.UserService;
import com.management.task.utils.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api" + CommonConstants.USERS)
public class UserController {

    @Autowired
    private final UserService userService;


    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public void createUser(final @Valid @RequestBody User user) {
        LOGGER.debug("Create User");
        userService.createUser(user);
    }

    @PostMapping(CommonConstants.LOGIN)
    public ResponseEntity<String> userLogin(final @Valid @RequestBody User user) {
        LOGGER.debug("login User");
        return new ResponseEntity<>(userService.login(user), HttpStatus.OK);
    }

    @PostMapping(CommonConstants.LOGOUT)
    public void userLogout(@RequestHeader(value = "Authorization") final String userToken) {
        LOGGER.debug("logout User");
        if(Objects.isNull(userToken)) {
            LOGGER.error("The logout operation can't be done");
            throw new BadRequestException("The logout operation can't be done");
        }
        String token;

        if (userToken.startsWith("Bearer ")) {
            token = userToken.substring(7);
            userService.logout(token);
        } else {
            LOGGER.error("The token is not correct");
            throw new BadRequestException("The token is not correct");
        }
    }

    @PostMapping("/{userId}/tasks")
    public void createUserTask(@RequestBody Task task, @PathVariable("userId") String userId)
            throws BadRequestException, UnAuthorizedException {
        LOGGER.debug("Create a task for an user");
        userService.createUserTask(task,userId);
    }
    @GetMapping("/{userId}/tasks")
    public List<Task> getAllUserTasks(@PathVariable("userId") String userId) {
        LOGGER.debug("get all the user tasks");
        return userService.getAllUserTasks(userId);
    }

    @GetMapping("/{userId}/tasks/{taskId}")
    public Task getUserTaskDetails(@PathVariable("taskId") String taskId, @PathVariable("userId") String userId) {
        LOGGER.debug("get a task details of an user");
        return userService.getUserTaskDetails(taskId, userId);
    }

    @PutMapping("/{userId}/tasks/{taskId}")
    public void updateUserTask(@RequestBody Task task, @PathVariable("taskId") String taskId,
                               @PathVariable("userId") String userId) {
        LOGGER.debug("update the task");
        userService.updateUserTask(task, taskId, userId);

    }

    @DeleteMapping("/{userId}/tasks/{taskId}")
    public void deleteUserTask(@PathVariable("taskId") String taskId, @PathVariable("userId") String userId) {
        LOGGER.debug("Delete the task");
        userService.deleteUserTask(taskId, userId);
    }
    @DeleteMapping("/{userId}/tasks")
    public void deleteAllUserTasks(@PathVariable("userId") String userId) {
        LOGGER.debug("Delete all the user tasks");
        userService.deleteAllUserTasks(userId);
    }


}
