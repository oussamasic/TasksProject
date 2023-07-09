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
import com.management.task.utils.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class UserConverterTest {

    @Test
    void convertUserDtoToUserModelTest() {
        // Given
        User userDto = new User("userId", "firstName", "lastName", "mail@example.com",
                true, UserStatus.ACTIF,"password");
        // When
        UserModel userModel = UserConverter.convertUserDtoToUserModel(userDto);

        // Then
        assertThat(userModel.getFirstName()).isEqualTo("firstName");
        assertThat(userModel.getId()).isEqualTo("userId");
        assertThat(userModel.getLastName()).isEqualTo("lastName");
        assertThat(userModel.getPassword()).isEqualTo("password");
        assertThat(userModel.getEmail()).isEqualTo("mail@example.com");
        assertThat(userModel).isEqualToComparingFieldByField(userDto);

    }

    @Test
    void convertUserModelToUserDtoTest() {
        // Given
        UserModel userModel = new UserModel("userId","firstName", "lastName","mail@example.com",
                true, UserStatus.ACTIF, "password");
        // When
        User userDto = UserConverter.convertUserModelToUserDto(userModel);

        // Then
        assertThat(userDto.getFirstName()).isEqualTo("firstName");
        assertThat(userDto.getId()).isEqualTo("userId");
        assertThat(userDto.getLastName()).isEqualTo("lastName");
        assertThat(userDto.getPassword()).isEqualTo("password");
        assertThat(userDto.getEmail()).isEqualTo("mail@example.com");
        assertThat(userDto).isEqualToComparingFieldByField(userModel);
    }
}
