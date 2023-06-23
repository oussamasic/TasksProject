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

import com.management.task.dto.Task;
import com.management.task.model.TaskModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class TaskConverterTest {

    @Test
    void convertTaskToTaskModelTest() {
        // Given
        Task task = new Task("id", "description", true);
        // When
        TaskModel taskModel = TaskConverter.convertTaskDtoToTaskModel(task);

        // Then
        assertThat(taskModel.getDescription()).isEqualTo("description");
        assertThat(taskModel.getId()).isEqualTo("id");
        assertThat(taskModel.isComplete()).isTrue();
        assertThat(taskModel).isEqualToComparingFieldByField(task);
    }

    @Test
    void convertTaskModelToTask() {
        // Given
        TaskModel taskModel = new TaskModel("id", "description", false);

        // When
        Task task = TaskConverter.convertTaskModelToTaskDto(taskModel);

        // Then
        assertThat(task.getDescription()).isEqualTo("description");
        assertThat(task.getId()).isEqualTo("id");
        assertThat(task.isComplete()).isFalse();
        assertThat(task).isEqualToComparingFieldByField(taskModel);
    }

}
