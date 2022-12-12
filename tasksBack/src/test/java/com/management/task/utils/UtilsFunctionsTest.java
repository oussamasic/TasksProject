package com.management.task.utils;

import com.management.task.dto.Task;
import com.management.task.exceptions.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(SpringExtension.class)
public class UtilsFunctionsTest {

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
}
