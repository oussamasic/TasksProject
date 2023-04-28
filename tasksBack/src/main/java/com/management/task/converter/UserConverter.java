package com.management.task.converter;

import com.management.task.dto.User;
import com.management.task.model.UserModel;
import org.springframework.beans.BeanUtils;

public class UserConverter {

    public static User convertUserModelToUserDto(UserModel userModel) {
        User userDto = new User();
        BeanUtils.copyProperties(userModel, userDto);
        return userDto;
    }

    public static UserModel convertUserDtoToUserModel(User user) {
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(user, userModel);
        return userModel;
    }
}
