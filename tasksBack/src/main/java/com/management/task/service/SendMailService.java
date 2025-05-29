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

import com.management.task.dto.EmailDetailsDto;
import com.management.task.dto.User;
import com.management.task.exceptions.BadRequestException;
import com.management.task.utils.FileReader;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Part;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Getter
@Setter
public class SendMailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMailService.class);


    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public SendMailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSampleMail(EmailDetailsDto details, User user) {

        LOGGER.debug("send a sample mail without attachment file");
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        String emailContent;

        try {

            mailMessage.setFrom(sender);
            mailMessage.setRecipients(Message.RecipientType.TO, user.getEmail());
            mailMessage.setSubject(details.getEmailSubject());

            LOGGER.debug("read the mail template file");
            emailContent = FileReader.loadFileContent("welcome-mail.html");

            emailContent = emailContent.replace("${firstName}", user.getFirstName());
            emailContent = emailContent.replace("${lastName}", user.getLastName().toUpperCase());

            LOGGER.debug("This mail has 2 part, the BODY and the embedded image");
            MimeMultipart multipart = new MimeMultipart("related");

            LOGGER.debug("The mail html part");
            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setContent(emailContent, "text/html");
            multipart.addBodyPart(messageBodyPart);

            Map<String, String> inlineImages = new HashMap<>();

            inlineImages.put("facebook-logo", Objects.requireNonNull(this.getClass().getClassLoader()
                    .getResource("templates/icons/facebook-logo-black.png")).getPath());
            inlineImages.put("instagram-logo", Objects.requireNonNull(this.getClass().getClassLoader()
                    .getResource("templates/icons/instagram-logo-black.png")).getPath());
            inlineImages.put("twitter-logo", Objects.requireNonNull(this.getClass().getClassLoader()
                    .getResource("templates/icons/twitter-logo-black.png")).getPath());
            inlineImages.put("youtube-logo", Objects.requireNonNull(this.getClass().getClassLoader()
                    .getResource("templates/icons/youtube-logo-black.png")).getPath());

            inlineImages.put("mail-logo", Objects.requireNonNull(this.getClass().getClassLoader()
                    .getResource("templates/icons/mail-logo.png")).getPath());
            inlineImages.put("url-logo", Objects.requireNonNull(this.getClass().getClassLoader()
                    .getResource("templates/icons/url-logo.png")).getPath());
            inlineImages.put("phone-logo", Objects.requireNonNull(this.getClass().getClassLoader()
                    .getResource("templates/icons/phone-logo.png")).getPath());
            inlineImages.put("localisation-logo", Objects.requireNonNull(this.getClass().getClassLoader()
                    .getResource("templates/icons/localisation-logo.png")).getPath());

            inlineImages.put("developer-logo", Objects.requireNonNull(this.getClass().getClassLoader()
                    .getResource("templates/icons/developer-logo.png")).getPath());

            inlineImages.put("discover-logo", Objects.requireNonNull(this.getClass().getClassLoader()
                    .getResource("templates/icons/discover-logo.png")).getPath());

            LOGGER.debug("This mail has 2 part, the BODY and the embedded images");
            inlineImages.keySet().forEach(contentId -> {
                MimeBodyPart imagePart = new MimeBodyPart();
                try {
                    imagePart.setHeader("Content-ID", "<" + contentId + ">");
                    imagePart.setDisposition(Part.INLINE);
                    String imageFilePath = inlineImages.get(contentId);
                    attachImageToMailBody(imagePart, imageFilePath);
                    multipart.addBodyPart(imagePart);
                } catch (MessagingException exception) {
                    LOGGER.error("error while attaching the image with id : {} to the mail body", contentId);
                    throw new BadRequestException("error while attaching the image to the email body : "
                            + exception.getMessage());
                }
            });

            mailMessage.setContent(multipart);
            javaMailSender.send(mailMessage);
        }
        catch (MailException | MessagingException | IOException exception) {
            LOGGER.error("error while sending the mail");
            throw new BadRequestException("error while sending the mail : "
                    + exception.getMessage());
        }
    }

    private void attachImageToMailBody(MimeBodyPart imagePart, String imageFilePath) {
        LOGGER.debug("attach the image to the email body");
        try {
            imagePart.attachFile(imageFilePath);
        } catch (IOException | MessagingException ex) {
            LOGGER.error("error while attaching the image to the mail body");
            throw new BadRequestException("error while attaching the image to the email body : "
                    + ex.getMessage());
        }
    }
}
