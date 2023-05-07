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
