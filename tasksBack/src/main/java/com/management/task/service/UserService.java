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
import com.management.task.converter.UserConverter;
import com.management.task.dto.Task;
import com.management.task.dto.User;
import com.management.task.exceptions.BadRequestException;
import com.management.task.exceptions.NotFoundException;
import com.management.task.exceptions.UnAuthorizedException;
import com.management.task.model.TaskModel;
import com.management.task.model.UserModel;
import com.management.task.repository.TaskRepository;
import com.management.task.repository.UserRepository;
import com.management.task.service.kafka.UserProducerService;
import com.management.task.utils.PasswordEncoder;
import com.management.task.utils.UtilsFunctions;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Getter
@Setter
public class UserService {

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final UserProducerService userProducerService;

    private final JwtService jwtService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private static final String  REQUEST_BODY_MANDATORY = "The request body should not be null";
    private static final String  TASK_BODY_MANDATORY = "the Task should not be null";

    private static final String TASK_NOT_FOUND = "Task not found";

    @Autowired
    public UserService(UserRepository userRepository, TaskRepository taskRepository, UserProducerService userProducerService,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.userProducerService = userProducerService;
        this.jwtService = jwtService;
    }

    public void createUser(User user) {
        LOGGER.info("creating user");
        if(Objects.isNull(user)) {
            LOGGER.warn(REQUEST_BODY_MANDATORY);
            throw new BadRequestException(REQUEST_BODY_MANDATORY);
        }

         if(!UtilsFunctions.isPatternEmailMatches(user.getEmail())) {
            LOGGER.error("The user email passed is not valid");
            throw new BadRequestException("The user email passed is not valid");
        }

        if(Objects.nonNull(getUserByEmail(user.getEmail())) ) {
            LOGGER.error("user with the same email exists");
            throw new BadRequestException("user with the same email exists");
        }
        UserModel userModel = UserConverter.convertUserDtoToUserModel(user);

        if(Objects.isNull(userModel.getPassword()) ) {
            LOGGER.error("no password passed in the request");
            throw new BadRequestException("you must specify a password");
        }

        final String hashedPassword = PasswordEncoder.hashPassword(userModel.getPassword());
        userModel.setPassword(hashedPassword);

        userRepository.save(userModel);

        LOGGER.debug("User created with id : {} ", userModel.getId());
        this.userProducerService.createUSer(user);

    }

    private UserModel getUserByEmail(String email) {
        Optional<UserModel> userModelOptional = userRepository.findByEmail(email);
        return userModelOptional.orElse(null);
    }

    public String login(User user) {
        LOGGER.debug("Process : user login");
        if(Objects.isNull(user)) {
            LOGGER.warn(REQUEST_BODY_MANDATORY);
            throw new BadRequestException(REQUEST_BODY_MANDATORY);
        }

        if(Objects.isNull(user.getEmail()) || user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            LOGGER.error("email should not be empty");
            throw new BadRequestException("email should not be empty");
        }

        if(Objects.isNull(user.getPassword()) || user.getPassword().isEmpty() || user.getPassword().isBlank()) {
            LOGGER.error("password should not be empty");
            throw new BadRequestException("password should not be empty");
        }

        UserModel userModel = getUserByEmail(user.getEmail());
        if(Objects.isNull(userModel) ) {
            LOGGER.error("no user found with this email : {}", user.getEmail());
            throw new NotFoundException("no user found with this email : {}" + user.getEmail());
        }

        if(!PasswordEncoder.checkPassword(user.getPassword(), userModel.getPassword())) {
            LOGGER.error("bad password");
            throw new BadRequestException("Bad password");
        }

        LOGGER.debug("Generating JWT token for the user");
        return jwtService.generateToken(userModel);

    }

    public void logout(String jwtToken) {
        LOGGER.debug("Process : user logout");

        jwtService.logout(jwtToken);
    }

    public User getAuthenticatedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return (User) auth.getPrincipal();
        }
        throw new UnAuthorizedException("User is not connected");
    }

    public void createUserTask(Task task, String userId) {
        LOGGER.debug("Create a task for the user : {}", userId);

        User authenticatedUser = getAuthenticatedUser();
        if(!Objects.equals(authenticatedUser.getId(), userId)) {
            LOGGER.error("You are not authorized to create this task");
            throw new UnAuthorizedException("You are not authorized to create this task");
        }
        if(Objects.isNull(task)) {
            LOGGER.error(TASK_BODY_MANDATORY);
            throw new BadRequestException(TASK_BODY_MANDATORY);
        }

        TaskModel taskModel = TaskConverter.convertTaskDtoToTaskModel(task);
        taskModel.setUserId(userId);
        taskModel.setCreationDate(new Date());
        taskRepository.save(taskModel);
    }

    public List<Task> getAllUserTasks(String userId) {
        LOGGER.debug("get all the tasks of the user : {}", userId);
        User authenticatedUser = getAuthenticatedUser();
        if(!Objects.equals(authenticatedUser.getId(), userId)) {
            LOGGER.error("You are not authorized to get the list of tasks");
            throw new UnAuthorizedException("You are not authorized to get the list of tasks");
        }
        List<TaskModel> taskModelList = taskRepository.findByUserId(userId);
        List<Task> taskList = new ArrayList<>();
        if(!taskModelList.isEmpty()) {
            taskModelList.forEach(taskModel ->
                    taskList.add(TaskConverter.convertTaskModelToTaskDto(taskModel)));
        }
        return taskList;
    }

    public Task getUserTaskDetails(String taskId, String userId) {
        LOGGER.debug("get the task details {} of the user : {}", taskId, userId);
        User authenticatedUser = getAuthenticatedUser();
        if(!Objects.equals(authenticatedUser.getId(), userId)) {
            LOGGER.error("You are not authorized to get the details  of this task");
            throw new UnAuthorizedException("You are not authorized to get the details  of this task");
        }

        Optional<TaskModel> taskModel = taskRepository.findById(taskId);
        Task task  = null;
        if(taskModel.isPresent()) {
            task = TaskConverter.convertTaskModelToTaskDto(taskModel.get());
        }
        return task;
    }

    public void updateUserTask(Task task,  String taskId, String userId) {
        LOGGER.debug("update the task {} ", taskId);
        User authenticatedUser = getAuthenticatedUser();
        if(!Objects.equals(authenticatedUser.getId(), userId)) {
            LOGGER.error("You are not authorized to update this task");
            throw new UnAuthorizedException("You are not authorized to update this task");
        }
        if(Objects.isNull(task)) {
            LOGGER.error(TASK_BODY_MANDATORY);
            throw new BadRequestException(TASK_BODY_MANDATORY);
        }
        Optional<TaskModel> taskModel = taskRepository.findById(taskId);
        if(taskModel.isEmpty()) {
                LOGGER.error(TASK_NOT_FOUND);
                throw new NotFoundException(TASK_NOT_FOUND);
        }
        TaskModel taskModelFromDto = TaskConverter.convertTaskDtoToTaskModel(task);
        taskModel.get().setDescription(taskModelFromDto.getDescription());
        taskModel.get().setEndDate(taskModelFromDto.getEndDate());
        taskModel.get().setStartDate(taskModelFromDto.getStartDate());
        taskModel.get().setTitle(taskModelFromDto.getTitle());
        taskRepository.save(taskModel.get());
    }

    public void deleteUserTask(String taskId, String userId) {
        LOGGER.debug("Delete the task {} ", taskId);
        User authenticatedUser = getAuthenticatedUser();
        if(!Objects.equals(authenticatedUser.getId(), userId)) {
            LOGGER.error("You are not authorized to delete this task");
            throw new UnAuthorizedException("You are not authorized to delete this task");
        }
        Optional<TaskModel> taskDtoOptional = taskRepository.findById(taskId);
        if(taskDtoOptional.isEmpty()) {
            LOGGER.error(TASK_NOT_FOUND);
            throw new NotFoundException(TASK_NOT_FOUND);
        }

        taskRepository.deleteById(taskId);
    }

    public void deleteAllUserTasks(String userId) {
        LOGGER.debug("Delete all the tasks of the user : {} ", userId);

        User authenticatedUser = getAuthenticatedUser();
        if(!Objects.equals(authenticatedUser.getId(), userId)) {
            LOGGER.error("You are not authorized to delete the tasks");
            throw new UnAuthorizedException("You are not authorized to delete the tasks");
        }

        taskRepository.deleteByUserId(userId);
    }
}
