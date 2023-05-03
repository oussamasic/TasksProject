/*
 *     <OZ TASKS>
 *     <project to manage user tasks>
 *     Copyright (C) <2023>  <ZEROUALI Oussama>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.management.task.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(SpringExtension.class)
class PasswordEncoderTest {

    @Test
    void test_hashPassword() {
        // Given
        String passwordPlainText = "test";
        // When
        String hashedPassword = PasswordEncoder.hashPassword(passwordPlainText);
        // Then
        assertThatCode(()->PasswordEncoder.hashPassword(passwordPlainText))
            .doesNotThrowAnyException();
        Assertions.assertNotNull(hashedPassword);
        Assertions.assertTrue(PasswordEncoder.checkPassword("test", hashedPassword));
    }

    @Test
    void test_checkPassword_with_bad_hashed_password() {
        String hashedPassword = "tototitihello";
        String passwordPlainText = "test";

        assertThatCode(() -> PasswordEncoder.checkPassword(passwordPlainText,hashedPassword))
            .hasMessage("Invalid hashed password provided for comparison")
            .isInstanceOf(IllegalArgumentException.class);
    }
}
