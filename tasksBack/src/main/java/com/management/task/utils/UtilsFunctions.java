/*
 * <OZ TASKS>
 * <project to manage user tasks>
 * Copyright (C) <2023>  <ZEROUALI Oussama>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
            throw new BadRequestException("InValide description, the min length is 5");
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
