package com.management.task.service;

import com.management.task.converter.TaskConverter;
import com.management.task.dto.Task;
import com.management.task.exceptions.NotFoundException;
import com.management.task.model.TaskModel;
import com.management.task.repository.TaskRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
public class TaskService {

    private final TaskRepository taskRepository;

    private static final String TASK_NOT_FOUND = "Task not found";

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAll() {
        return taskRepository.findAll().stream().map(TaskConverter::convertTaskModelToTaskDto)
            .toList();

        }

    public void createTask(Task taskDto) {
        TaskModel taskModel = TaskConverter.convertTaskDtoToTaskModel(taskDto);
        taskRepository.save(taskModel);
    }

    public Task getTaskById(String id) {
        Optional<TaskModel> taskModelOptional = taskRepository.findById(id);
        if(taskModelOptional.isEmpty()) {
            throw new NotFoundException(TASK_NOT_FOUND);
        }
        return TaskConverter.convertTaskModelToTaskDto(taskModelOptional.get());
    }
    public void updateTask(Task taskDto, String id) {
        Optional<TaskModel> taskDtoOptional = taskRepository.findById(id);
        if(taskDtoOptional.isEmpty()) {
            throw new NotFoundException(TASK_NOT_FOUND);
        }
        taskDtoOptional.get().setDescription(taskDto.getDescription());
        taskRepository.save(taskDtoOptional.get());

    }

    public List<Task> getAllCompleteTasks() {
        return taskRepository.findByComplete(true).stream().map(TaskConverter::convertTaskModelToTaskDto)
            .toList();
    }

    public List<Task> getAllInCompleteTasks() {
        return taskRepository.findByComplete(false).stream().map(TaskConverter::convertTaskModelToTaskDto)
                .toList();
    }

    public void completeTask(String id) {
        Optional<TaskModel> taskDtoOptional = taskRepository.findById(id);
        if(taskDtoOptional.isEmpty()) {
            throw new NotFoundException(TASK_NOT_FOUND);
        }
        taskDtoOptional.get().setComplete(true);
        taskRepository.save(taskDtoOptional.get());
    }

    public void inCompleteTask(String id) {
        Optional<TaskModel> taskDtoOptional = taskRepository.findById(id);
        if(taskDtoOptional.isEmpty()) {
            throw new NotFoundException(TASK_NOT_FOUND);
        }
        taskDtoOptional.get().setComplete(false);
        taskRepository.save(taskDtoOptional.get());
    }

    public void deleteTask(String id) {
        Optional<TaskModel> taskDtoOptional = taskRepository.findById(id);
        if(taskDtoOptional.isEmpty()) {
            throw new NotFoundException(TASK_NOT_FOUND);
        }
        taskRepository.deleteById(id);
    }

}
