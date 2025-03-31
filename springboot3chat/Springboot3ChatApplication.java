package com.example.springboot3chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class Springboot3ChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(Springboot3ChatApplication.class, args);
	}
}
