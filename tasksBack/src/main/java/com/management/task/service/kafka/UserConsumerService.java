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

package com.management.task.service.kafka;

import com.management.task.dto.EmailDetailsDto;
import com.management.task.dto.User;
import com.management.task.service.SendMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserConsumerService {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(UserConsumerService.class);

    private static final String CREATED_USE_WELCOME_MAIL = "Hello with us in OSZ plateforme";

    @Autowired
    private final SendMailService sendMailService;

    public UserConsumerService(SendMailService sendMailService) {
        this.sendMailService = sendMailService;
    }

    @KafkaListener(topics = "${kafka.general.topic.name}", groupId = "${kafka.general.topic.group.id}")
    public void consume(String message) {
        LOGGER.info("Message received ff: {}", message);
    }

    @KafkaListener(topics = "${kafka.user.topic.name}", groupId = "${kafka.user.topic.group.id}",
        containerFactory = "userKafkaListenerContainerFactory")
    public void consume(User user) {
        LOGGER.info("user email received : {}", user.getEmail());
        LOGGER.info("user firstName, lastName received : {} {}",
                user.getFirstName(), user.getLastName());
        EmailDetailsDto emailDetailsDto = new EmailDetailsDto();
        emailDetailsDto.setEmailSubject(CREATED_USE_WELCOME_MAIL);
        sendMailService.sendSampleMail(emailDetailsDto, user);
    }
}
