/*
 *
 *  <OZ TASKS>
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
 *
 */

package com.management.task.service;

import com.management.task.dto.ContactMailDto;
import com.management.task.exceptions.BadRequestException;
import com.management.task.utils.FileReader;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class ContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactService.class);

    private static final String CONTACT_MAIL = "making contact";

    @Value("${spring.mail.username}")
    private String recipient;

    private final JavaMailSender javaMailSender;

    @Autowired
    public ContactService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendContactEmail(ContactMailDto contactMailDto) {
        LOGGER.info("Sending email request");
        if(Objects.isNull(contactMailDto)) {
            LOGGER.error("You can not send the email");
            throw new BadRequestException("You can not send the email");
        }
        LOGGER.info("Sending contact email from : {} {}",
            contactMailDto.getFirstName(), contactMailDto.getLastName());

        String emailContent;

        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        try {

            mailMessage.setRecipients(Message.RecipientType.TO, recipient);
            mailMessage.setSubject(CONTACT_MAIL);

            LOGGER.info("read the mail template file");
            emailContent = FileReader.loadFileContent("contact-us.html");

            emailContent = emailContent.replace("${lastName}", contactMailDto.getLastName().toUpperCase());
            emailContent = emailContent.replace("${firstName}", contactMailDto.getFirstName().toUpperCase());
            emailContent = emailContent.replace("${sender}", contactMailDto.getSender());
            emailContent = emailContent.replace("${phoneNumber}",
                Objects.nonNull(contactMailDto.getPhoneNumber()) ? contactMailDto.getPhoneNumber() : "No phone number provided");

            emailContent = emailContent.replace("${mailBody}", contactMailDto.getMailBody());

            MimeMultipart multipart = new MimeMultipart("related");

            LOGGER.info("The mail html part");
            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setContent(emailContent, "text/html");
            multipart.addBodyPart(messageBodyPart);
            mailMessage.setContent(multipart);

            // Sending the mail
            javaMailSender.send(mailMessage);

        }    catch (MailException | MessagingException | IOException exception) {
            LOGGER.error("error while sending the mail");
            throw new BadRequestException("error while sending the mail : "
                + exception.getMessage());
        }

    }
}
