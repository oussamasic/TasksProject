package com.management.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {

    private String id;
    @NotNull(message = "Description cannot be null")
    @Size(min = 10, max = 100)
    private String description;
    private boolean complete;
}
