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

package com.management.task.controller;

import com.management.task.dto.ErrorDto;
import com.management.task.exceptions.BadRequestException;
import com.management.task.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;

@ExtendWith(SpringExtension.class)
class RestApiExceptionHandlerTest {

    private RestApiExceptionHandler restApiExceptionHandler;

    @BeforeEach
    public void setUp() {
        restApiExceptionHandler = new RestApiExceptionHandler();
    }

    @Test
    void test_handleControllerException_with_bad_request_exception() {
        ResponseEntity<ErrorDto> response = restApiExceptionHandler.handleControllerException(
                new BadRequestException("bad request exception"));

        Assertions.assertNotNull(restApiExceptionHandler);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(400, response.getStatusCode().value());
        Assertions.assertEquals(Objects.requireNonNull(response.getBody()).getError(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @Test
    void test_handleControllerException_with_not_found_exception() {
        ResponseEntity<ErrorDto> response = restApiExceptionHandler.handleControllerException(
                new NotFoundException("not found exception"));

        Assertions.assertNotNull(restApiExceptionHandler);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(404, response.getStatusCode().value());
        Assertions.assertEquals(Objects.requireNonNull(response.getBody()).getError(), HttpStatus.NOT_FOUND.getReasonPhrase());
    }
}
