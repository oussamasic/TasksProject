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

import com.itextpdf.text.DocumentException;
import com.management.task.dto.Task;
import com.management.task.dto.User;
import com.management.task.exceptions.BadRequestException;
import com.management.task.exceptions.UnAuthorizedException;
import com.management.task.service.UserService;
import com.management.task.utils.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api" + CommonConstants.USERS)
public class UserController {


    private final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public void createUser(final @Valid @RequestBody User user) {
        LOGGER.info("Create User");
        userService.createUser(user);
    }

    @PostMapping(CommonConstants.LOGIN)
    public ResponseEntity<String> userLogin(final @Valid @RequestBody User user) {
        LOGGER.info("login User");
        return new ResponseEntity<>(userService.login(user), HttpStatus.OK);
    }

    @PostMapping(CommonConstants.LOGOUT)
    public void userLogout(@RequestHeader(value = "Authorization") final String userToken) {
        LOGGER.info("logout User");
        if (Objects.isNull(userToken)) {
            LOGGER.error("The logout operation can't be done");
            throw new BadRequestException("The logout operation can't be done");
        }
        String token;

        if (userToken.startsWith("Bearer ")) {
            token = userToken.substring(7);
            userService.logout(token);
        } else {
            LOGGER.error("The token is not correct");
            throw new BadRequestException("The token is not correct");
        }
    }

    @PostMapping(CommonConstants.USER_TASKS)
    public void createUserTask(@RequestBody Task task, @PathVariable("userId") String userId)
        throws BadRequestException, UnAuthorizedException {
        LOGGER.info("Create a task for an user");
        userService.createUserTask(task, userId);
    }

    @GetMapping(CommonConstants.USER_TASKS)
    public List<Task> getAllUserTasks(@PathVariable("userId") String userId) {
        LOGGER.info("get all the user tasks");
        return userService.getAllUserTasks(userId);
    }

    @GetMapping(CommonConstants.USER_TASK_DETAILS)
    public Task getUserTaskDetails(@PathVariable("taskId") String taskId, @PathVariable("userId") String userId) {
        LOGGER.info("get a task details of an user");
        return userService.getUserTaskDetails(taskId, userId);
    }

    @PutMapping(CommonConstants.USER_TASK_DETAILS)
    public void updateUserTask(@RequestBody Task task, @PathVariable("taskId") String taskId,
        @PathVariable("userId") String userId) {
        LOGGER.info("update the task");
        userService.updateUserTask(task, taskId, userId);

    }

    @DeleteMapping(CommonConstants.USER_TASK_DETAILS)
    public void deleteUserTask(@PathVariable("taskId") String taskId, @PathVariable("userId") String userId) {
        LOGGER.info("Delete the task");
        userService.deleteUserTask(taskId, userId);
    }

    @DeleteMapping(CommonConstants.USER_TASKS)
    public void deleteAllUserTasks(@PathVariable("userId") String userId) {
        LOGGER.info("Delete all the user tasks");
        userService.deleteAllUserTasks(userId);
    }

    @GetMapping()
    public List<User> getAllPaginatedUsers(@RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "0") int page) {
        LOGGER.info("get All Paginated Users from page : {} with {} as size ", page, size);
        return userService.getAllPaginatedUsers(size, page);
    }

    @GetMapping(value = CommonConstants.USER_TASKS + "/report", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> downloadUserTasksReportNormal(@PathVariable("userId") String userId)
        throws DocumentException {

        LOGGER.info("Download the task list of user with id : {}", userId);

        ByteArrayOutputStream pdfStream =
            userService.downloadUserTasksReportNormal();

        return new ResponseEntity<>(pdfStream.toByteArray(), HttpStatus.OK);
    }

    @GetMapping(params = "email")
    public User findUserByEmail(@RequestParam final String email) {
        LOGGER.info("Find the user by the email : {}", email);
        return userService.findUserByEmail(email);
    }

/*    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Mono<byte[]>>   downloadFile() throws IOException {

            File file = new File("src/main/resources/report.pdf");
            InputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            inputStream.read(bytes);
            inputStream.close();
            //return ResponseEntity.ok().body(bytes);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(Mono.just(bytes));


    }*/

    @GetMapping(value = CommonConstants.TASKS + "/web-flux-report",
        produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Mono<byte[]>> downloadWebFluxReport()
        throws DocumentException {

        LOGGER.info("Download the task list of user");

        ByteArrayOutputStream pdfStream =
            userService.downloadUserTasksReportNormal();

        return ResponseEntity.ok()
            .cacheControl(CacheControl.noCache())
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(Mono.just(pdfStream.toByteArray()));
    }

    public Flux<DataBuffer> load() {

            final Resource resource = new ClassPathResource("report.pdf");

            if (resource.exists() || resource.isReadable()) {
                return DataBufferUtils.read(resource, new DefaultDataBufferFactory(), 4096);
            } else {
                throw new RuntimeException("Could not read the file!");
            }

    }

    InputStreamResource convertDataBufferFileToInputStreamResponse(Flux<DataBuffer> dataBufferFlux) throws IOException {
        PipedInputStream isPipe;
        try {
            PipedOutputStream osPipe = new PipedOutputStream();
            isPipe = new PipedInputStream(osPipe);
            DataBufferUtils.write(dataBufferFlux, osPipe)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnComplete(() -> {
                    try {
                        osPipe.close();
                    } catch (IOException ignored) {
                    }
                }).subscribe(DataBufferUtils.releaseConsumer());
        } catch (IOException ioException) {
            LOGGER.error("Error loading stream {} ", ioException.getMessage());
            throw new IOException("Error loading stream {} ", ioException);
        }
        return new InputStreamResource(isPipe);
    }
}
