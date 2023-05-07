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

import com.management.task.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserConsumerService {

    private final Logger logger =
        LoggerFactory.getLogger(UserConsumerService.class);

    @KafkaListener(topics = "${kafka.general.topic.name}", groupId = "${kafka.general.topic.group.id}")
    public void consume(String message) {
        logger.info("Message received ff: {}", message);
    }

    @KafkaListener(topics = "${kafka.user.topic.name}", groupId = "${kafka.user.topic.group.id}",
        containerFactory = "userKafkaListenerContainerFactory")
    public void consume(User user) {
        logger.info("user email received : {}", user.getEmail());
        logger.info("user firstName received : {}", user.getFirstName());
        logger.info("user lastName received : {}", user.getLastName());
    }
}
