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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.SecureRandom;
import java.util.Objects;


public class PasswordEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordEncoder.class);

    private PasswordEncoder() {
        throw new IllegalStateException("Utility class");
    }

    public static String hashPassword(String passwordPlainText) {
        // Define the BCrypt workload to use when generating password hashes. 10-31 is a valid value.
        final int workload = 14;
        String salt = BCrypt.gensalt(workload, new SecureRandom());
        return BCrypt.hashpw(passwordPlainText, salt);
    }

    public static boolean checkPassword(String passwordPlainText, String hashedPassword) {

        if(Objects.isNull(hashedPassword) || !hashedPassword.startsWith("$2a$")) {
            LOGGER.error("Invalid hashed password provided for comparison");
            throw new IllegalArgumentException("Invalid hashed password provided for comparison");
        }
        return BCrypt.checkpw(passwordPlainText, hashedPassword);
    }
}
