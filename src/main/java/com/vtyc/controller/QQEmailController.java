package com.vtyc.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Controller
public class QQEmailController {

    @RequestMapping("/simpleemail")
    @ResponseBody
    public String home(){
        try {
            sendMail();
            return "Email sent!";
        }catch (Exception ex){
            return "Error in sending email: " + ex;
        }
    }

    public static void sendMail() throws AddressException, MessagingException {

        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp"); // 连接协议
        properties.put("mail.smtp.host", "smtp.qq.com"); // 主机名
        properties.put("mail.smtp.port", 465);  // 端口号
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");  // 设置是否使用ssl安全连接 (一般都使用)
        properties.put("mail.debug", "true"); // 设置是否显示debug信息 true 会在控制台显示相关信息
        // 得到回话对象
        Session session = Session.getInstance(properties);
        // 获取邮件对象
        Message message = new MimeMessage(session);
        // 设置发件人邮箱地址
        try {
            message.setFrom(new InternetAddress("38154591@qq.com"));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        // 设置收件人地址
        message.setRecipients( MimeMessage.RecipientType.TO, new InternetAddress[] {
                new InternetAddress("38154591@qq.com") });
        // 设置邮件标题
        message.setSubject("由JavaMail发出的测试邮件");
        // 设置邮件内容
        message.setContent("<h1>请点击以下链接址激活注册:</br><h3>（若无法点击，请复制到浏览器中打开）</h3></h1>",
                "text/html;Charset=UTF-8");
        // 得到邮差对象
        Transport transport = session.getTransport();
        // 连接自己的邮箱账户
        transport.connect("38154591@qq.com", "mkqejgvaixyrbjej"); //密码为刚才得到的授权码
        // 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
    }

}
