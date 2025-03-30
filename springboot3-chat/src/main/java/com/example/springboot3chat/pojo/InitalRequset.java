package com.example.springboot3chat.pojo;

import org.springframework.stereotype.Component;

@Component
public class InitalRequset {
    String ip;
    int targetPort;
    int listenPort;
    String username;

    public InitalRequset() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
