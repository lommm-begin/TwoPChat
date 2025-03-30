package com.example.springboot3chat;

import com.example.springboot3chat.serivice.MessageContextService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Springboot3ChatApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	MessageContextService messageContextService;

	@Test
	public void test() throws Exception {
		messageContextService.setInitalRequset("10.211.121.137", 8888, 8888,"lyx");

		messageContextService.sendMessage("nhao");

		messageContextService.listen();

		System.out.println(messageContextService.getMessage().toString());
	}
}
