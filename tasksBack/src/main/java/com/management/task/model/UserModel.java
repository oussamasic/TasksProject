package com.management.task.model;

import com.management.task.utils.MongoDbCollections;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = MongoDbCollections.USERS)
public class UserModel {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean authorized;
    private String status;
    private String password;

}
