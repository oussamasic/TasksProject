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

package com.management.task.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.management.task.converter.TaskConverter;
import com.management.task.converter.UserConverter;
import com.management.task.dto.Task;
import com.management.task.dto.User;
import com.management.task.exceptions.BadRequestException;
import com.management.task.exceptions.NotFoundException;
import com.management.task.exceptions.UnAuthorizedException;
import com.management.task.model.TaskModel;
import com.management.task.model.UserModel;
import com.management.task.repository.TaskRepository;
import com.management.task.repository.UserRepository;
import com.management.task.service.kafka.UserProducerService;
import com.management.task.utils.PasswordEncoder;
import com.management.task.utils.UtilsFunctions;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Getter
@Setter
public class UserService {

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final UserProducerService userProducerService;

    private final JwtService jwtService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private static final String  REQUEST_BODY_MANDATORY = "The request body should not be null";

    private static final String  TASK_BODY_MANDATORY = "the Task should not be null";

    private static final String TASK_NOT_FOUND = "Task not found";

    @Autowired
    public UserService(UserRepository userRepository, TaskRepository taskRepository, UserProducerService userProducerService,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.userProducerService = userProducerService;
        this.jwtService = jwtService;
    }

    public void createUser(User user) {
        LOGGER.info("creating user");
        if(Objects.isNull(user)) {
            LOGGER.warn(REQUEST_BODY_MANDATORY);
            throw new BadRequestException(REQUEST_BODY_MANDATORY);
        }

         if(!UtilsFunctions.isPatternEmailMatches(user.getEmail())) {
            LOGGER.error("The user email passed is not valid");
            throw new BadRequestException("The user email passed is not valid");
        }

        if(Objects.nonNull(getUserByEmail(user.getEmail())) ) {
            LOGGER.error("user with the same email exists");
            throw new BadRequestException("user with the same email exists");
        }

        UserModel userModel = UserConverter.convertUserDtoToUserModel(user);

        if(Objects.isNull(userModel.getPassword()) ) {
            LOGGER.error("no password passed in the request");
            throw new BadRequestException("you must specify a password");
        }

        final String hashedPassword = PasswordEncoder.hashPassword(userModel.getPassword());
        userModel.setPassword(hashedPassword);

        userRepository.save(userModel);

        LOGGER.info("User created with id : {} ", userModel.getId());
        this.userProducerService.createUSer(user);

    }

    private UserModel getUserByEmail(String email) {
        Optional<UserModel> userModelOptional = userRepository.findByEmail(email);
        return userModelOptional.orElse(null);
    }

    public String login(User user) {
        LOGGER.info("Process : user login");
        if(Objects.isNull(user)) {
            LOGGER.warn(REQUEST_BODY_MANDATORY);
            throw new BadRequestException(REQUEST_BODY_MANDATORY);
        }

        if(Objects.isNull(user.getEmail()) || user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            LOGGER.error("email should not be empty");
            throw new BadRequestException("email should not be empty");
        }

        if(Objects.isNull(user.getPassword()) || user.getPassword().isEmpty() || user.getPassword().isBlank()) {
            LOGGER.error("password should not be empty");
            throw new BadRequestException("password should not be empty");
        }

        UserModel userModel = getUserByEmail(user.getEmail());
        if(Objects.isNull(userModel) ) {
            LOGGER.error("no user found with this email : {}", user.getEmail());
            throw new NotFoundException("no user found with this email : {}" + user.getEmail());
        }

        if(!PasswordEncoder.checkPassword(user.getPassword(), userModel.getPassword())) {
            LOGGER.error("bad password");
            throw new BadRequestException("Bad password");
        }

        LOGGER.info("Generating JWT token for the user");
        return jwtService.generateToken(userModel);

    }

    public void logout(String jwtToken) {
        LOGGER.info("Process : user logout");

        jwtService.logout(jwtToken);
    }

    public User getAuthenticatedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return (User) auth.getPrincipal();
        }
        throw new UnAuthorizedException("User is not connected");
    }

    public void createUserTask(Task task, String userId) {
        LOGGER.info("Create a task for the user : {}", userId);

        User authenticatedUser = getAuthenticatedUser();
        if(!Objects.equals(authenticatedUser.getId(), userId)) {
            LOGGER.error("You are not authorized to create this task");
            throw new UnAuthorizedException("You are not authorized to create this task");
        }
        if(Objects.isNull(task)) {
            LOGGER.error(TASK_BODY_MANDATORY);
            throw new BadRequestException(TASK_BODY_MANDATORY);
        }

        TaskModel taskModel = TaskConverter.convertTaskDtoToTaskModel(task);
        taskModel.setUserId(userId);
        taskModel.setCreationDate(new Date());
        taskRepository.save(taskModel);
    }

    public List<Task> getAllUserTasks(String userId) {
        LOGGER.info("get all the tasks of the user : {}", userId);
        User authenticatedUser = getAuthenticatedUser();
        if(!Objects.equals(authenticatedUser.getId(), userId)) {
            LOGGER.error("You are not authorized to get the list of tasks");
            throw new UnAuthorizedException("You are not authorized to get the list of tasks");
        }
        List<TaskModel> taskModelList = taskRepository.findByUserId(userId);
        List<Task> taskList = new ArrayList<>();
        if(!taskModelList.isEmpty()) {
            taskModelList.forEach(taskModel ->
                    taskList.add(TaskConverter.convertTaskModelToTaskDto(taskModel)));
        }
        return taskList;
    }

    public Task getUserTaskDetails(String taskId, String userId) {
        LOGGER.info("get the task details {} of the user : {}", taskId, userId);
        User authenticatedUser = getAuthenticatedUser();
        if(!Objects.equals(authenticatedUser.getId(), userId)) {
            LOGGER.error("You are not authorized to get the details  of this task");
            throw new UnAuthorizedException("You are not authorized to get the details  of this task");
        }

        Optional<TaskModel> taskModel = taskRepository.findById(taskId);
        Task task  = null;
        if(taskModel.isPresent()) {
            task = TaskConverter.convertTaskModelToTaskDto(taskModel.get());
        }
        return task;
    }

    public void updateUserTask(Task task,  String taskId, String userId) {
        LOGGER.info("update the task {} ", taskId);
        User authenticatedUser = getAuthenticatedUser();
        if(!Objects.equals(authenticatedUser.getId(), userId)) {
            LOGGER.error("You are not authorized to update this task");
            throw new UnAuthorizedException("You are not authorized to update this task");
        }
        if(Objects.isNull(task)) {
            LOGGER.error(TASK_BODY_MANDATORY);
            throw new BadRequestException(TASK_BODY_MANDATORY);
        }
        Optional<TaskModel> taskModel = taskRepository.findById(taskId);
        if(taskModel.isEmpty()) {
                LOGGER.error(TASK_NOT_FOUND);
                throw new NotFoundException(TASK_NOT_FOUND);
        }
        TaskModel taskModelFromDto = TaskConverter.convertTaskDtoToTaskModel(task);
        taskModel.get().setDescription(taskModelFromDto.getDescription());
        taskModel.get().setEndDate(taskModelFromDto.getEndDate());
        taskModel.get().setStartDate(taskModelFromDto.getStartDate());
        taskModel.get().setTitle(taskModelFromDto.getTitle());
        taskRepository.save(taskModel.get());
    }

    public void deleteUserTask(String taskId, String userId) {
        LOGGER.info("Delete the task {} ", taskId);
        User authenticatedUser = getAuthenticatedUser();
        if(!Objects.equals(authenticatedUser.getId(), userId)) {
            LOGGER.error("You are not authorized to delete this task");
            throw new UnAuthorizedException("You are not authorized to delete this task");
        }
        Optional<TaskModel> taskDtoOptional = taskRepository.findById(taskId);
        if(taskDtoOptional.isEmpty()) {
            LOGGER.error(TASK_NOT_FOUND);
            throw new NotFoundException(TASK_NOT_FOUND);
        }

        taskRepository.deleteById(taskId);
    }

    public void deleteAllUserTasks(String userId) {
        LOGGER.info("Delete all the tasks of the user : {} ", userId);

        User authenticatedUser = getAuthenticatedUser();
        if(!Objects.equals(authenticatedUser.getId(), userId)) {
            LOGGER.error("You are not authorized to delete the tasks");
            throw new UnAuthorizedException("You are not authorized to delete the tasks");
        }

        taskRepository.deleteByUserId(userId);
    }

    public List<User> getAllPaginatedUsers(int size, int page) {
        LOGGER.info("get All Paginated Users, page : {} and size {} ", page, size);
        List<User> userList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, size);
        userRepository.findAll(pageable).stream().forEach(userModel -> {
            User user = UserConverter.convertUserModelToUserDto(userModel);
            user.setPassword(null);
            userList.add(user);
                });
        return userList;
    }

    public ByteArrayOutputStream downloadUserTasksReportNormal() throws DocumentException {
        LOGGER.info("Download the task list of user");

        User authenticatedUser = getAuthenticatedUser();
        if(Objects.isNull(authenticatedUser)) {
            LOGGER.error("You are not authorized to download the reports");
            throw new UnAuthorizedException("You are not authorized to download the reports");
        }

        Document document = new Document();
        ByteArrayOutputStream pdfReportByteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, pdfReportByteArrayOutputStream);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 18, BaseColor.BLACK);
            Font font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
            Font userFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 14, BaseColor.BLUE);

            Chunk titleChunk = new Chunk("OSZ Developers: ", titleFont);

            Chunk chunk = new Chunk("List of tasks created by the user : ", font);
            Chunk chunk2 = new Chunk(authenticatedUser.getFirstName(), userFont);
            Chunk spaceChunk = new Chunk(" ", userFont);
            Chunk chunk3 = new Chunk(authenticatedUser.getLastName(), userFont);

            document.add(new Paragraph(titleChunk));

            document.add(chunk);
            document.add(chunk2);
            document.add(spaceChunk);
            document.add(chunk3);



            PdfPTable pdfTable = new PdfPTable(5);

            //Create Header Cells
            PdfPCell pdfHeaderCell1 = new PdfPCell(new Paragraph("Title"));
            pdfHeaderCell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pdfHeaderCell1.setBorderWidth(2);
            pdfHeaderCell1.setMinimumHeight(22);
            pdfHeaderCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfHeaderCell1.setVerticalAlignment(Element.ALIGN_CENTER);

            PdfPCell pdfHeaderCell2 = new PdfPCell(new Paragraph("Description"));
            pdfHeaderCell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pdfHeaderCell2.setBorderWidth(2);
            pdfHeaderCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfHeaderCell2.setVerticalAlignment(Element.ALIGN_CENTER);

            PdfPCell pdfHeaderCell3 = new PdfPCell(new Paragraph("Start Date"));
            pdfHeaderCell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pdfHeaderCell3.setBorderWidth(2);
            pdfHeaderCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfHeaderCell3.setVerticalAlignment(Element.ALIGN_CENTER);

            PdfPCell pdfHeaderCell4 = new PdfPCell(new Paragraph("End Date"));
            pdfHeaderCell4.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pdfHeaderCell4.setBorderWidth(2);
            pdfHeaderCell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfHeaderCell4.setVerticalAlignment(Element.ALIGN_CENTER);

            PdfPCell pdfHeaderCell5 = new PdfPCell(new Paragraph("Status"));
            pdfHeaderCell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pdfHeaderCell5.setBorderWidth(2);
            pdfHeaderCell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfHeaderCell5.setVerticalAlignment(Element.ALIGN_CENTER);

            //Add header cells to table
            pdfTable.addCell(pdfHeaderCell1);
            pdfTable.addCell(pdfHeaderCell2);
            pdfTable.addCell(pdfHeaderCell3);
            pdfTable.addCell(pdfHeaderCell4);
            pdfTable.addCell(pdfHeaderCell5);



            List<TaskModel> taskModelList = taskRepository.findByUserId(authenticatedUser.getId());
            if(!taskModelList.isEmpty()) {
                taskModelList.forEach(taskModel -> {

                    //Create cell
                    PdfPCell titleCell = new PdfPCell(new Paragraph(taskModel.getTitle()));
                    PdfPCell descriptionCell = new PdfPCell(new Paragraph(taskModel.getDescription()));
                    PdfPCell startDateCell = new PdfPCell(new Paragraph(taskModel.getStartDate().toString()));
                    PdfPCell endDateCell = new PdfPCell(new Paragraph(taskModel.getEndDate().toString()));
                    PdfPCell statusCell = new PdfPCell(new Paragraph(taskModel.isComplete() ? "Completed" : "Not Completed"));

                    //Add cell to table
                    pdfTable.addCell(titleCell);
                    pdfTable.addCell(descriptionCell);
                    pdfTable.addCell(startDateCell);
                    pdfTable.addCell(endDateCell);
                    pdfTable.addCell(statusCell);
                    });

            }

            document.add(pdfTable);
        }
        catch (DocumentException documentException) {
            throw new DocumentException("error while creating pdf file :", documentException);
        }
        finally {
            document.close();
        }

        return pdfReportByteArrayOutputStream;
    }

    public User findUserByEmail(String email) {
        LOGGER.info("Search user with email : {}", email);

        UserModel userModel = getUserByEmail(email);

        if(Objects.isNull(userModel)) {
            LOGGER.error("There is no user found with this email");
            throw new NotFoundException("There is no user found with this email");
        }

        User user = UserConverter.convertUserModelToUserDto(userModel);
        user.setPassword(null);
        return user;
    }
}
