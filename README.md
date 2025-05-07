# TwoPChat
好像是双人无防火墙的热点聊天
* 后端是springboot使用java的UDP，两个人互相聊天
* 自测是同一个手机热点下可以互相聊天，但前提是把公网的那个防火墙要关掉
* 不知道为什么自己测试的时候自己给自己发消息还有给人发消息，收到是正常的文本
* 但舍友的电脑无论是给我发送还是自己给自己发，发送中文内容，都会出现json格式不完整，最后少个分号和括号
* 试过了校园网，但是不能互发
* 不知道为什么编译的时候lombok的注解没起作用，被idea提示（没在idea开启注解处理）
* 发送端口和监听端口建议改成相同的
* 前端代码是AI生成的
* 前后端分离，json格式传输数据

![image](https://github.com/user-attachments/assets/65f2a4fc-392e-4b02-8081-c54a8d1c99df)
![image](https://github.com/user-attachments/assets/d6c45e80-e151-4110-8109-5ab7a40d6599)

