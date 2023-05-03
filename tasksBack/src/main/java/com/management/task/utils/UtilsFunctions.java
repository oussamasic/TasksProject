package com.management.task.utils;

import com.management.task.dto.Task;
import com.management.task.exceptions.BadRequestException;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;
import java.util.regex.Pattern;

public class UtilsFunctions {

    private static final String REGEX_PATTERN = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
        + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$";

    private UtilsFunctions() {
        throw new IllegalStateException("Utility class");
    }
    public static void checkDescription(Task task) throws BadRequestException {
        if(Objects.nonNull(task)  && task.getDescription().length()<5) {
            throw new BadRequestException("Invalide description, the min length is 5");
        }
    }

    public static boolean isPatternEmailMatches(String emailAddress) {
        if(EmailValidator.getInstance().isValid(emailAddress)) {
          return Pattern.compile(REGEX_PATTERN)
                .matcher(emailAddress)
                .matches();
        }
        return false;
    }
}
