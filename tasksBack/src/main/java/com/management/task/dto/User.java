package com.management.task.dto;

import com.management.task.utils.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private String id;
    @Size(max = 100)
    private String firstName;
    @Size(max = 100)
    private String lastName;
    @NotNull(message = "email cannot be null")
    @Email(message = "Email should be valid")
    private String email;
    private boolean authorized;
    private UserStatus status;
    private String password;

}
