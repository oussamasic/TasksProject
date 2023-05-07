package com.management.task.service;

import com.management.task.converter.UserConverter;
import com.management.task.dto.User;
import com.management.task.exceptions.BadRequestException;
import com.management.task.model.UserModel;
import com.management.task.repository.UserRepository;
import com.management.task.service.kafka.UserProducerService;
import com.management.task.utils.PasswordEncoder;
import com.management.task.utils.UserStatus;
import com.management.task.utils.UtilsFunctions;
import lombok.Getter;
import lombok.Setter;
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

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, UserProducerService userProducerService) {
        this.userRepository = userRepository;
        this.userProducerService = userProducerService;
    }



    public void createUser(User user) {
        LOGGER.info("creating user");
        if(Objects.isNull(user)) {
            LOGGER.warn("The request body should not be null");
            throw new BadRequestException("The request body should not be null");
        }
        user.setStatus(UserStatus.INACTIF);
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

        LOGGER.info("User created with id : {} ", userModel.getId());
        this.userProducerService.createUSer(user);

    }

    private UserModel getUserByEmail(String email) {
        Optional<UserModel> userModelOptional = userRepository.findByEmail(email);
        return userModelOptional.orElse(null);

    }
}
