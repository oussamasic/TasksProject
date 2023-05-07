package com.management.task.service;

import com.management.task.converter.UserConverter;
import com.management.task.dto.User;
import com.management.task.exceptions.BadRequestException;
import com.management.task.exceptions.NotFoundException;
import com.management.task.model.Token;
import com.management.task.model.UserModel;
import com.management.task.repository.TokenRepository;
import com.management.task.repository.UserRepository;
import com.management.task.service.kafka.UserProducerService;
import com.management.task.utils.PasswordEncoder;
import com.management.task.utils.UtilsFunctions;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Getter
@Setter
public class UserService {

    private final UserRepository userRepository;

    private final UserProducerService userProducerService;

    private final TokenRepository tokenRepository;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private static final String TOKEN_PREFIX = "TOKEN";


    @Autowired
    public UserService(UserRepository userRepository, UserProducerService userProducerService,
        TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.userProducerService = userProducerService;
        this.tokenRepository = tokenRepository;
    }

    public void createUser(User user) {
        LOGGER.info("creating user");
        if(Objects.isNull(user)) {
            LOGGER.warn("The request body should not be null");
            throw new BadRequestException("The request body should not be null");
        }
        //user.setStatus(UserStatus.INACTIF);
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

    public void login(User user) {
        if(Objects.isNull(user)) {
            LOGGER.warn("The request body should not be null");
            throw new BadRequestException("The request body should not be null");
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

        Token token = new Token();
        token.setUserRefId(userModel.getId());
        String tokenId = UUID.randomUUID().toString();
        token.setId(tokenId);
        token.setUpdatedDate(new Date());

        tokenRepository.save(token);
    }

    public void logout(String tokenId) {
        if(tokenId == null) {
            LOGGER.error("invalid token");
            throw new BadRequestException("invalid token");
        }
        final Optional<Token> optToken = tokenRepository.findById(tokenId);

        if (optToken.isEmpty()) {
            LOGGER.error("invalid request");
            throw new BadRequestException("invalid request");
        }

        tokenRepository.deleteById(tokenId);
    }
}
