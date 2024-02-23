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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserProducerService {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(UserProducerService.class);

    @Value(value = "${kafka.general.topic.name}")
    private String topicName ;

    @Value(value = "${kafka.user.topic.name}")
    private String userTopicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaTemplate<String, User> userKafkaTemplate;

    public void sendMessage(String message) {
        LOGGER.info("Message sent : {} ", message);
        this.kafkaTemplate.send(topicName, message);
    }

    public void createUSer(User user) {
        LOGGER.info("user sent : {} ", user);
        this.userKafkaTemplate.send(userTopicName, user);
    }
}
