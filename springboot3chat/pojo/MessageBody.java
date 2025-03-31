package com.example.springboot3chat.pojo;

public class MessageBody {
    String content;
    long timestamp;

    public MessageBody() {
    }

    public MessageBody(String content, long timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "MessageBody{" +
                "content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
