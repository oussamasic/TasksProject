package com.management.task.converter;

import com.management.task.dto.Task;
import com.management.task.model.TaskModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
public class TaskConverter {

    public static Task convertTaskModelToTaskDto(TaskModel taskModel) {
        Task taskDto = new Task();
        BeanUtils.copyProperties(taskModel, taskDto);
        return taskDto;
    }

    public static TaskModel convertTaskDtoToTaskModel(Task taskDto) {
        TaskModel taskModel = new TaskModel();
        BeanUtils.copyProperties(taskDto, taskModel);
        return taskModel;
    }

}
