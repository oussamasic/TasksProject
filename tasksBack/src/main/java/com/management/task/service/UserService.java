package com.management.task.service;

import com.management.task.converter.UserConverter;
import com.management.task.dto.User;
import com.management.task.exceptions.BadRequestException;
import com.management.task.model.UserModel;
import com.management.task.repository.UserRepository;
import com.management.task.utils.PasswordEncoder;
import com.management.task.utils.UserStatus;
import com.management.task.utils.UtilsFunctions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Getter
@Setter
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    public void createUser(User user) {
        LOGGER.info("creating user");
        if(Objects.isNull(user)) {
            LOGGER.log(Level.WARNING, "The request body should not be null");
            throw new BadRequestException("The request body should not be null");
        }
        user.setStatus(UserStatus.INACTIF);
        if(!UtilsFunctions.isPatternEmailMatches(user.getEmail())) {
            LOGGER.log(Level.WARNING, "The user email passed is not valid");
            throw new BadRequestException("The user email passed is not valid");
        }

        if(Objects.nonNull(getUserByEmail(user.getEmail())) ) {
            LOGGER.log(Level.WARNING, "user with the same email exists");
            throw new BadRequestException("user with the same email exists");
        }
        UserModel userModel = UserConverter.convertUserDtoToUserModel(user);
        final String hashedPassword = PasswordEncoder.hashPassword(userModel.getPassword());
        userModel.setPassword(hashedPassword);

        userRepository.save(userModel);
    }

    private UserModel getUserByEmail(String email) {
        Optional<UserModel> userModelOptional = userRepository.findByEmail(email);
        return userModelOptional.orElse(null);

    }
}
