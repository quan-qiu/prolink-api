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
public class VtycEmailController {

    @RequestMapping("/vtycemail")
    @ResponseBody
    public String home(){
        try {
            sendEmail();
            return "Email sent!";
        }catch (Exception ex){
            return "Error in sending email: " + ex;
        }
    }

    public static void sendEmail() throws Exception
    {
        String fromEmail = "quan_qiu@varroctyc.com";
        String toEmail = "quan_qiu@varroctyc.com";
        String emailName = "quan_qiu@varroctyc.com" ;
        String emailPassword = "Vtyc10427" ;
        String title = "test";
        String centent = "this is testing";

        Properties prop=new Properties();
        prop.put("mail.host","smtp.mxhichina.com" );
        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.debug", "true"); // 设置是否显示debug信息 true 会在控制台显示相关信息
        Session session=Session.getInstance(prop);
        session.setDebug(true);
        Transport ts=session.getTransport();
        ts.connect(emailName, emailPassword);
        Message message=new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject(title);
        message.setContent(centent, "text/html;charset=utf-8");
        ts.sendMessage(message, message.getAllRecipients());
    }
}
