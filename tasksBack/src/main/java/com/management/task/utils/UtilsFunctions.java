package com.management.task.utils;

import com.management.task.dto.Task;
import com.management.task.exceptions.BadRequestException;

import java.util.Objects;

public class UtilsFunctions {
    public static void checkDescription(Task task) throws BadRequestException {
        if(Objects.nonNull(task) && Objects.nonNull(task.getDescription()) && task.getDescription().length()<5) {
            throw new BadRequestException("Invalide description, the min length is 5");
        }
    }
}
