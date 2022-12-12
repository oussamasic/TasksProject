package com.management.task.model;

import com.management.task.utils.MongoDbCollections;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;


@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = MongoDbCollections.TASKS)
public class TaskModel {

    @Id
    private String id;
    @NotNull
    @Length(min = 5)
    private String description;
    private boolean complete;
}
