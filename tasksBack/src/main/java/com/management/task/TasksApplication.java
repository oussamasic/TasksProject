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

package com.management.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class TasksApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(TasksApplication.class);

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(TasksApplication.class, args);

	}

	@Override
	public void run(String... args) {
		final String applicationName = env.getProperty("spring.application.name") ;
		LOGGER.debug("User Tasks SpringBoot Application started:");
		LOGGER.debug("Spring Application Name: {}", applicationName);
	}

}
