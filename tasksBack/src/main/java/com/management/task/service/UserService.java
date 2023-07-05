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

import com.management.task.converter.UserConverter;
import com.management.task.dto.User;
import com.management.task.exceptions.BadRequestException;
import com.management.task.exceptions.NotFoundException;
import com.management.task.model.UserModel;
import com.management.task.repository.UserRepository;
import com.management.task.service.kafka.UserProducerService;
import com.management.task.utils.PasswordEncoder;
import com.management.task.utils.UtilsFunctions;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Getter
@Setter
public class UserService {

    private final UserRepository userRepository;

    private final UserProducerService userProducerService;

    private final JwtService jwtService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private static final String  REQUEST_BODY_MANDATORY = "The request body should not be null";

    @Autowired
    public UserService(UserRepository userRepository, UserProducerService userProducerService,
                       JwtService jwtService) {
        this.userRepository = userRepository;
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
}
