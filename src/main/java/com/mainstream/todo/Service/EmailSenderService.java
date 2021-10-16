package com.mainstream.todo.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    private final JavaMailSender mailSender;

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String toMail, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("myonlinetodo@gmail.com");
        message.setTo(toMail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

}
