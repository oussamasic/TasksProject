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
import com.management.task.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@ControllerAdvice
public class RestApiExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<ErrorDto> handleControllerException(Throwable exception) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorDto errorDto = new ErrorDto();

        if(exception instanceof AccessDeniedException || exception instanceof ForbiddenException) {
            LOGGER.info("Handle Forbidden and AccessDenied request error");
            status = HttpStatus.FORBIDDEN;
            errorDto.setError(HttpStatus.FORBIDDEN.getReasonPhrase());
            errorDto.setStatus(HttpStatus.FORBIDDEN.value());
        }
        else if (exception instanceof BadRequestException) {
            LOGGER.info("Handle Bad request error");
            status = HttpStatus.BAD_REQUEST;
            errorDto.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
            errorDto.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        else if (exception instanceof NotFoundException) {
            LOGGER.info("Handle Not found error");
            status = HttpStatus.NOT_FOUND;
            errorDto.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
            errorDto.setStatus(HttpStatus.NOT_FOUND.value());
        }
        else if (exception instanceof InternalServerException) {
            LOGGER.info("Handle Internal Server Exception error");
            status = HttpStatus.UNAUTHORIZED;
            errorDto.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
            errorDto.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        else if (exception instanceof UnAuthorizedException) {
            LOGGER.info("Handle Unauthorized request error");
            errorDto.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        errorDto.setMessage(exception.getMessage());
        errorDto.setTimeStamp(new Date());

        return new ResponseEntity<>(errorDto, status);
    }
}
