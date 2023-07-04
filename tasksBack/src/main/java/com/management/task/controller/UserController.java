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

import com.management.task.dto.User;
import com.management.task.exceptions.BadRequestException;
import com.management.task.service.UserService;
import com.management.task.utils.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/login")
    public ResponseEntity<String> userLogin(final @Valid @RequestBody User user) {
        LOGGER.debug("login User");
        return new ResponseEntity<>(userService.login(user), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public void userLogout(@RequestHeader(value = "Authorization") final String tokenUser) {
        LOGGER.info("logout User");
        if(Objects.isNull(tokenUser)) {
            LOGGER.debug("login User");
            throw new BadRequestException("The logout operation can't be done");
        }
        String token;

        if (tokenUser.startsWith("Bearer ")) {
            token = tokenUser.substring(7);
            userService.logout(token);
        }
    }
}
