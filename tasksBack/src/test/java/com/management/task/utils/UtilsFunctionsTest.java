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

package com.management.task.utils;

import com.management.task.dto.Task;
import com.management.task.exceptions.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class UtilsFunctionsTest {

    @Test
    void checkDescriptionTestShouldBeOK() {
        // Given
        Task task = new Task("id", "description", true, "userId",
                new Date(),"taskTitle", new Date(), new Date());
        // Then
        assertThatCode(()->UtilsFunctions.checkDescription(task))
            .doesNotThrowAnyException();

    }

    @Test
    void checkDescriptionTestShouldBeKO() {
        // Given
        Task task = new Task("id", "de", true, "userId",
                new Date(),"taskTitle", new Date(), new Date());
        // Then
        assertThatCode(()->UtilsFunctions.checkDescription(task))
            .isInstanceOf(BadRequestException.class);

    }

    @ParameterizedTest
    @ValueSource(strings = {"用户名@领域.电脑", "00323...787''''àà@@@domain.com", "usernàme.dfd@d@main.com", "USERNAME_00dfd@&&é&é/**/545.com",
        "user_name@@@doma....in.com", "*---58787@domain.com", "username@domain.co.om...", "USERNAME_00dfd@domain.com."})
    void testIsPatternEmailMatchesWithBadEmails(String emailAddress) {
        Assertions.assertFalse( UtilsFunctions.isPatternEmailMatches(emailAddress));
    }

    @ParameterizedTest
    @ValueSource(strings = {"username1254@domain.com", "157878545@email.com", "username.dfd@domain.com",  "testtest@7878545-7878.com",
        "user_name@domain.com", "username@domain.co.om", "USERNAME_00dfd@domain.com"})
    void testIsPatternEmailMatchesWithGoodEmails(String emailAddress) {
        assertTrue(UtilsFunctions.isPatternEmailMatches(emailAddress));
    }

}
