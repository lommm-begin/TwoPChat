package com.example.springboot3chat.controller;

import com.example.springboot3chat.pojo.InitalRequset;
import com.example.springboot3chat.pojo.MessageBody;
import com.example.springboot3chat.serivice.MessageContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Queue;

@RestController
public class ChatController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageContextService messageContextService;

    /**
     * 输入ip，端口信息
     * @param initalRequset
     */
    @PostMapping("/initialize")
    public String init(
            @RequestBody
            InitalRequset initalRequset) {
        try {
            log.info("初始化中...");
            messageContextService.setInitalRequset(initalRequset.getIp(), initalRequset.getTargetPort(), initalRequset.getListenPort(), initalRequset.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage() + "初始失败");
        }

        return "success";
    }

    @PostMapping("/send")
    public String send(
            @RequestBody
            String message) {
        if (message == null || message.isEmpty()) {
            return "消息不能为空";
        }
        try {
            messageContextService.sendMessage(message);
        } catch (IOException e) {
            log.error(e.getMessage() + "消息发送失败");
        }

        return "success";
    }

    @GetMapping("/messages")
    public Queue<MessageBody> getMessages() {
        try {
            System.out.println("监听消息");
            return messageContextService.getMessage();
        } catch (Exception e) {
            log.error(e.getMessage() + "接受消息失败");
        }
        return null;
    }

    /**
     * 开启监听
     * @return
     */
    @PostMapping("/start")
    public void start() {
        try {
            log.info("开始监听");
            messageContextService.listen();
        } catch (IOException e) {
            log.error("监听时发生异常");
        }
    }
}
