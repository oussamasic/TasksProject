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
