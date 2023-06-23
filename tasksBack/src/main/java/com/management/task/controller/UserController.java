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
import com.management.task.service.UserService;
import com.management.task.utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.logging.Logger;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api" + CommonConstants.USERS)
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public void createUser(final @Valid @RequestBody User user) {
        LOGGER.info("Create User");
        userService.createUser(user);
    }

    @PostMapping("/login")
    public void userLogin(final @Valid @RequestBody User user) {
        LOGGER.info("login User");
        userService.login(user);
    }

    @PostMapping("/logout/{tokenId}")
    public void serLogout(final @PathVariable("tokenId") String tokenId) {
        LOGGER.info("logout User");
        userService.logout(tokenId);
    }
}
