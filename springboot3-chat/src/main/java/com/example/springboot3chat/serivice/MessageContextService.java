package com.example.springboot3chat.serivice;

import com.example.springboot3chat.pojo.InitalRequset;
import com.example.springboot3chat.pojo.MessageBody;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


@Service
public class MessageContextService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Queue<MessageBody> queue = new ConcurrentLinkedQueue<>();
    private DatagramSocket receiverSocket;
    private DatagramPacket receiverPacket;
    private DatagramSocket sendSocket;
    private DatagramPacket sendPacket;

    private byte[] buffer = new byte[1024 * 60];

    private Boolean init = false;
    private volatile boolean stop = false;

    private InitalRequset initalRequset;

    @Autowired
    public MessageContextService(InitalRequset initalRequset) {
        this.initalRequset = initalRequset;
    }

    /**
     * 发送消息
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        if (!init) {
            log.info("未初始化，不能发送消息！");
            return;
        }
        sendPacket = new DatagramPacket(message.getBytes(StandardCharsets.UTF_8), message.getBytes().length, InetAddress.getByName(initalRequset.getIp()), initalRequset.getTargetPort());
        sendSocket.send(sendPacket);
        log.info("发送消息: {}", message);
    }

    /**
     * 监听
     * @return
     */
    @Async
    public void listen() throws IOException {
        if (!init) {
            log.info("已经启动监听！");
        }
        while (stop && !Thread.currentThread().isInterrupted()) {
            receiverSocket.receive(receiverPacket);
            MessageBody messageBody = new MessageBody();
            messageBody.setContent(new String(receiverPacket.getData(), 0, receiverPacket.getLength(), StandardCharsets.UTF_8));
            messageBody.setTimestamp(System.currentTimeMillis());
            queue.add(messageBody);

            log.info("接收到消息: {}", messageBody.getContent());
        }
    }

    /**
     * 消费消息
     * @return
     */
    public Queue<MessageBody> getMessage() {
        return new LinkedList<>(queue);
    }

    /**
     * 初始化发送者信息
     * @param ip
     * @param targetPort
     * @param listenPort
     * @param username
     * @throws Exception
     */
    public void setInitalRequset(String ip, int targetPort, int listenPort, String username) throws Exception {
        initalRequset.setIp(ip);
        initalRequset.setTargetPort(targetPort);
        initalRequset.setListenPort(listenPort);
        initalRequset.setUsername(username);
        // 初始化监听
        receiverSocket = new DatagramSocket(listenPort);
        receiverPacket = new DatagramPacket(buffer, buffer.length);
        sendSocket = new DatagramSocket();
        init = true;
        stop = true;

        log.info("初始化成功: {}:{}, 监听的端口是: {}", ip, targetPort, listenPort);
    }

    @PreDestroy
    public void shutdownAsyncExecutor() {
        stop = false;
        if (receiverSocket != null) {
            receiverSocket.close();
        }
        if (sendSocket != null) {
            sendSocket.close();
        }
    }
}
