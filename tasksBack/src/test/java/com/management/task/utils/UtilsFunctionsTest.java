package com.management.task.utils;

import com.management.task.dto.Task;
import com.management.task.exceptions.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class UtilsFunctionsTest {

    @Test
    void checkDescriptionTestShouldBeOK() {
        // Given
        Task task = new Task("id", "description", true);
        // Then
        assertThatCode(()->UtilsFunctions.checkDescription(task))
            .doesNotThrowAnyException();

    }

    @Test
    void checkDescriptionTestShouldBeKO() {
        // Given
        Task task = new Task("id", "de", true);
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
