package com.management.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class TasksApplication implements CommandLineRunner {

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(TasksApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("VITAMUI SpringBoot Application started:");
		System.out.println("spring.application.name: " + env.getProperty("spring.application.name"));

	}

}
