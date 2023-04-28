package com.management.task.repository;

import com.management.task.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * MongoDB repository for users.
 *
 */
public interface UserRepository extends MongoRepository<UserModel, String> {

    Optional<UserModel> findByEmail(String mail);
}
