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
import com.management.task.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @MockBean
    UserService userService;

    private UserController userController;

    @BeforeEach
    public void setUp() {
        userController = new UserController(userService);
    }

    @Test
    void testCreateUser() {
        // Given
        User user = new User();
        user.setEmail("mail@example.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");

        // When
        userController.createUser(user);

        // Then
        verify(userService).createUser(user);
    }

    @Test
    void testLoginUser() {
        // Given
        User user = new User();
        user.setEmail("mail@example.com");
        user.setPassword("password");

        // When
        userController.userLogin(user);

        // Then
        verify(userService).login(user);
    }

    @Test
    void test_logout_user_ko_when_token_is_null() {

        assertThatCode(() ->
                userController.userLogout(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The logout operation can't be done");
    }

    @Test
    void test_logout_user_ko_when_token_is_not_correct() {
        // Given
        String initialToken = "userToken";

        // Then
        assertThatCode(() ->
                userController.userLogout(initialToken))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The token is not correct");
    }

    @Test
    void test_logout_user_ok_when_token_is_correct() {
        // Given
        String initialToken = "Bearer userToken";
        String userToken = "userToken";

        // When
        userController.userLogout(initialToken);

        // Then
        verify(userService).logout(userToken);
    }

    @Test
    void testCreateUserTask_ok_when_all_conditions_are_respected() {
        // Given
        Task task = new Task("id", "description", true, "userId",
                new Date(),"taskTitle", new Date(), new Date());
        String userId = "userId";

        // When
        userController.createUserTask(task, userId);

        // Then
        verify(userService).createUserTask(task, userId);
    }

    @Test
    void testGetAllUserTask() {
        // Given
        String userId = "userId";
        List<Task> expectedResponse = new ArrayList<>();

        // When
        Mockito
                .when(userService.getAllUserTasks(userId))
                .thenReturn(expectedResponse);
        List<Task> responseDto = userController.getAllUserTasks(userId);

        // Then
        Assertions.assertEquals(responseDto, expectedResponse);
    }

    @Test
    void testGetUserTaskDetails() {
        // Given
        String userId = "userId";
        String taskId = "taskId";
        Task expectedResponse = new Task();

        // When
        Mockito
                .when(userService.getUserTaskDetails(taskId, userId))
                .thenReturn(expectedResponse);
        Task responseDto = userController.getUserTaskDetails(taskId, userId);

        // Then
        Assertions.assertEquals(responseDto, expectedResponse);
    }

    @Test
    void testUpdateUserTask() {

        // Given
        String userId = "userId";
        String taskId = "taskId";
        Task task = new Task("id", "description", true, "userId",
                new Date(),"taskTitle", new Date(), new Date());

        // When
        userController.updateUserTask(task, taskId, userId);

        // Then
        verify(userService).updateUserTask(task, taskId, userId);
    }

    @Test
    void testDeleteUserTask() {

        // Given
        String userId = "userId";
        String taskId = "taskId";

        // When
        userController.deleteUserTask(taskId, userId);

        // Then
        verify(userService).deleteUserTask(taskId, userId);
    }

    @Test
    void testDeleteAllUserTasks() {

        // Given
        String userId = "userId";
        String taskId = "taskId";

        // When
        userController.deleteAllUserTasks(userId);

        // Then
        verify(userService).deleteAllUserTasks(userId);
    }
}
