package com.management.task.repository;

import com.management.task.model.TaskModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


/**
 * MongoDB repository for tasks.
 *
 */
public interface TaskRepository extends MongoRepository<TaskModel, String> {
    public List<TaskModel> findByComplete(boolean complete);
}
