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
import com.management.task.utils.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;
    @MockBean
    private UserProducerService userProducerService;
    @MockBean
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository,taskRepository,
                userProducerService,jwtService);

        // add a connected user to springContext
        User userDto = new User("userId", "firstName", "lastName", "mail@example.com",
                true, UserStatus.ACTIF,"password");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDto, null,
                        new ArrayList<>());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Test
    void test_all_mocked_services() {
        assertThat(userRepository).isNotNull();
        assertThat(taskRepository).isNotNull();
        assertThat(userService).isNotNull();
        assertThat(jwtService).isNotNull();
        assertThat(userProducerService).isNotNull();
    }

    @Test
    void testLoginOfUsers_ko_when_email_provided_is_empty() {
        // Given
        User userDto = new User("userId", "firstName", "lastName", "",
                true, UserStatus.ACTIF,"password");

        assertThatCode(()-> userService.login(userDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("email should not be empty");

    }

    @Test
    void testCreateUser_ok_when_all_conditions_are_respected() {
        // Given
        User userDto = new User("userId", "firstName", "lastName", "mail@example.com",
                true, UserStatus.ACTIF,"password");
        // When
        userService.createUser(userDto);

        // Then
        verify(userRepository, atLeastOnce()).findByEmail(userDto.getEmail());
    }

    @Test
    void testCreateUser_ko_when_other_user_exists_with_the_same_email() {
        // Given
        User userDto = new User("userId", "firstName", "lastName", "mail@example.com",
                true, UserStatus.ACTIF,"password");
        UserModel userModel = new UserModel("userId","firstName", "lastName","mail@example.com",
                true, UserStatus.ACTIF,
                PasswordEncoder.hashPassword("password"));
        // When
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(userModel));

        // Then
        assertThatCode(()-> userService.createUser(userDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("user with the same email exists");
    }

    @Test
    void testCreateUser_ko_when_user_body_is_null() {

        assertThatCode(()-> userService.createUser(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The request body should not be null");
    }

    @Test
    void testLoginOfUsers_ok_when_all_conditions_are_respected() {
        // Given
        User userDto = new User("userId", "firstName", "lastName", "mail@example.com",
                true, UserStatus.ACTIF,"password");

        UserModel userModel = new UserModel("userId","firstName", "lastName","mail@example.com",
                true, UserStatus.ACTIF,
                PasswordEncoder.hashPassword("password"));

        // When
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(userModel));
        userService.login(userDto);

        // Then
        verify(jwtService).generateToken(userModel);
    }

    @Test
    void testLoginOfUsers_ko_when_no_user_found_in_DB() {
        // Given
        User userDto = new User("userId", "firstName", "lastName", "mail@example.com",
                true, UserStatus.ACTIF,"password");

        // When
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());

        // Then
        assertThatCode(()-> userService.login(userDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("no user found with this email : {}" + userDto.getEmail());
    }

    @Test
    void testLoginOfUsers_ko_when_no_user_object_is_given() {

        assertThatCode(()-> userService.login(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The request body should not be null");
    }

    @Test
    void testLogoutOfUsers() {
        String jwtToken = "tokenTokenTokenJWT";
        // When
        userService.logout(jwtToken);

        // Then
        verify(jwtService).logout(jwtToken);
    }

    @Test
    void testLoginOfUsers_ko_when_no_email_provided() {
        // Given
        User userDto = new User("userId", "firstName", "lastName", null,
                true, UserStatus.ACTIF,"password");

        assertThatCode(()-> userService.login(userDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("email should not be empty");

    }

    @Test
    void testLoginOfUsers_ko_when_no_password_provided() {
        // Given
        User userDto = new User("userId", "firstName", "lastName", "test@test.com",
                true, UserStatus.ACTIF,"");

        assertThatCode(()-> userService.login(userDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("password should not be empty");

    }

    @Test
    void testCreateUser_ko_when_no_password_given() {
        // Given
        User userDto = new User("userId", "firstName", "lastName", "mail@example.com",
                true, UserStatus.ACTIF,null);

        // Then
        assertThatCode(()-> userService.createUser(userDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("you must specify a password");
    }

    @Test
    void testGetAllUserTasks() {
        // Given
        String userId = "userId";
        // When
        userService.getAllUserTasks(userId);

        // Then
        verify(taskRepository, Mockito.atLeast(1)).findByUserId(userId);
    }

    @Test
    void testGetAllUserTasks_ok() {
        // Given
        String userId = "userId";
        // When
        when(taskRepository.findByUserId(userId)).thenReturn(new ArrayList<>());
        List<Task> list = userService.getAllUserTasks(userId);

        // Then
        verify(taskRepository, Mockito.atLeast(1)).findByUserId(userId);
        assertThat(list).isEmpty();
    }

    @Test
    void testDeleteAllUserTasks_ok_when_all_conditions_are_validated() {
        // Given
        String userId = "userId";
        // When
        userService.deleteAllUserTasks(userId);

        // Then
        verify(taskRepository, Mockito.atLeast(1)).deleteByUserId(userId);

    }

    @Test
    void testDeleteAllUserTasks_ko_when_connected_user_is_not_the_requestor() {
        // Given
        String userId = "userId00";

        // Then
        assertThatCode(() -> userService.deleteAllUserTasks(userId))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage("You are not authorized to delete the tasks");

    }

    @Test
    void testGetUserTaskDetails_ok_when_task_is_not_null() {
        // Given
        String userId = "userId";
        String taskId = "taskId";
        Task task = new Task("taskId", "description", true, "userId",
                new Date(),"taskTitle", new Date(), new Date());
        TaskModel taskModel = new TaskModel("taskId", "description", false, "userId",
                new Date(),"taskTitle", new Date(), new Date());

        // When
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskModel));
        userService.updateUserTask(task, taskId, userId);

        // Then
        verify(taskRepository, atLeastOnce()).findById(taskId);

    }

    @Test
    void testGetUserTaskDetails_ko_when_task_is_null() {
        // Given
        String userId = "userId";
        String taskId = "taskId";

        // Then
        assertThatCode(() ->
                userService.updateUserTask(null, taskId, userId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("the Task should not be null");
    }
    @Test
    void testDeleteUserTask_ok_when_all_conditions_are_validated() {
        // Given
        String userId = "userId";
        String taskId = "taskId";
        TaskModel taskModel = new TaskModel("taskId", "description", false, "userId",
                new Date(),"taskTitle", new Date(), new Date());

        // When
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskModel));
        userService.deleteUserTask(taskId, userId);

        // Then
        verify(taskRepository, Mockito.atLeast(1)).findById(taskId);
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void testDeleteUserTask_ko_when_connected_user_is_not_the_requestor() {
        // Given
        String userId = "userId00";
        String taskId = "taskId";

        // Then
        assertThatCode(()-> userService.deleteUserTask(taskId, userId))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage("You are not authorized to delete this task");
    }

    @Test
    void testCreateUserTask_ko_when_connected_user_is_not_the_requestor() {
        // Given
        String userId = "userId00";
        Task task = new Task("taskId", "description", true, "userId",
                new Date(),"taskTitle", new Date(), new Date());

        // Then
        assertThatCode(() -> userService.createUserTask(task, userId))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage("You are not authorized to create this task");

    }

    @Test
    void testDeleteUserTask_ko_when_task_not_found() {
        // Given
        String userId = "userId";
        String taskId = "taskId";

        // When
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Then
        assertThatCode(()-> userService.deleteUserTask(taskId, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Task not found");
    }

    @Test
    void testUpdateUserTask() {
        // Given
        String userId = "userId";
        String taskId = "taskId";
        // When
        userService.getUserTaskDetails(taskId, userId);
        // Then
        verify(taskRepository, Mockito.atLeast(1)).findById(taskId);
    }

    @Test
    void testCreateUser_ko_when_email_is_not_valid() {
        // Given
        User userDto = new User("userId", "firstName", "lastName", "*---587名领域87@domain.com",
                true, UserStatus.ACTIF,"password");

        // Then
        assertThatCode(()-> userService.createUser(userDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The user email passed is not valid");
    }

    @Test
    void testCreateUserTask_ok_when_all_conditions_are_respected() {
        // Given
        String userId = "userId";
        Task task = new Task("taskId", "description", true, "userId",
                new Date(),"taskTitle", new Date(), new Date());

        // Then
        assertThatCode(() -> userService.createUserTask(task, userId)).doesNotThrowAnyException();

    }

    @Test
    void testCreateUserTask_ko_when_task_body_is_null() {
        // Given
        String userId = "userId";

        // Then
        assertThatCode(() -> userService.createUserTask(null, userId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("the Task should not be null");

    }

    @Test
    void testLoginOfUsers_ko_when_password_provided_is_not_correct() {
        // Given
        User userDto = new User("userId", "firstName", "lastName", "mail@example.com",
                true, UserStatus.ACTIF,"test-test");

        UserModel userModel = new UserModel("userId","firstName", "lastName","mail@example.com",
                true, UserStatus.ACTIF,
                PasswordEncoder.hashPassword("password"));

        // When
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(userModel));

        // Then
        assertThatCode(()-> userService.login(userDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Bad password");
    }

    @Test
    void testGetAllUserTasks_ko_when_connected_user_is_not_the_requestor() {
        // Given
        String userId = "userId00";

        // Then
        assertThatCode(() -> userService.getAllUserTasks(userId))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage("You are not authorized to get the list of tasks");

    }

    @Test
    void testGetUserTaskDetails_ko_when_connected_user_is_not_the_requestor() {
        // Given
        String userId = "userId00";
        String taskId = "taskId";

        // Then
        assertThatCode(() -> userService.getUserTaskDetails(taskId, userId))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage("You are not authorized to get the details  of this task");

    }
    @Test
    void testUpdateUserTask_ko_when_connected_user_is_not_the_requestor() {
        // Given
        String userId = "userId00";
        String taskId = "taskId";
        Task task = new Task("taskId", "description", true, "userId",
                new Date(),"taskTitle", new Date(), new Date());

        // Then
        assertThatCode(() -> userService.updateUserTask(task, taskId, userId))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage("You are not authorized to update this task");

    }

    @Test
    void testUpdateUserTask_ko_when_no_task_found_in_DB() {
        // Given
        String userId = "userId";
        String taskId = "taskId";
        Task task = new Task("taskId", "description", true, "userId",
                new Date(),"taskTitle", new Date(), new Date());

        // When
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Then
        assertThatCode(() -> userService.updateUserTask(task, taskId, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Task not found");

    }
}
