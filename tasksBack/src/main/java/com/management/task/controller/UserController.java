package com.management.task.controller;

import com.management.task.dto.User;
import com.management.task.service.UserService;
import com.management.task.utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public void createUser(@RequestBody final User user) {
        LOGGER.info("Create User");
        //UtilsFunctions.checkDescription(task);
        userService.createUser(user);
    }
}
