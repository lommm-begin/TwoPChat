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
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


@Service
public class MessageContextService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Queue<MessageBody> queue = new ConcurrentLinkedQueue<>();
    private ServerSocket serverSocket;
    private Socket socket;

    private Boolean init = false;
    private volatile boolean stop = false;
    private byte[] buffer = new byte[1024 * 60];

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
        OutputStream outputStream = socket.getOutputStream();
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            bufferedOutputStream.write(message.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error(e.getMessage() + "发送消息失败");
        }
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
            Socket accept = serverSocket.accept();

            InputStream inputStream = accept.getInputStream();

            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
                int readCount = 0;
                while ((readCount = bufferedInputStream.read(buffer)) != -1) {
                    queue.add(new MessageBody(new String(buffer, 0, readCount, StandardCharsets.UTF_8), System.currentTimeMillis()));
                }
            } catch (Exception e) {
                log.error(e.getMessage() + "接受消息时发生异常");
            }

            log.info("接收到消息: {}", queue.toArray());
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
        this.serverSocket = new ServerSocket(listenPort);
        this.socket = new Socket(ip, targetPort);
        init = true;
        stop = true;

        log.info("初始化成功: {}:{}, 监听的端口是: {}", ip, targetPort, listenPort);
    }

    @PreDestroy
    public void shutdownAsyncExecutor() {
        stop = false;
        try {
            if (socket != null) {
                socket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage() + "关闭时发生异常，已捕获");
        }
    }
}
