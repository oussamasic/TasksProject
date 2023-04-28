package com.management.task.service;

import com.management.task.converter.UserConverter;
import com.management.task.dto.User;
import com.management.task.exceptions.BadRequestException;
import com.management.task.model.UserModel;
import com.management.task.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Getter
@Setter
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        UserModel userModel = UserConverter.convertUserDtoToUserModel(user);
        if(Objects.nonNull(getUserByEmail(user.getEmail())) ) {
            throw new BadRequestException("user with the same email exists");
        }
        userRepository.save(userModel);
    }

    private UserModel getUserByEmail(String email) {
        Optional<UserModel> userModelOptional = userRepository.findByEmail(email);
        return userModelOptional.orElse(null);

    }
}
