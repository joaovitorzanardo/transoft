package br.com.transoft.backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmailWithUserPassword(String to, String password) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("noreply@transoft.com");
        email.setTo(to);
        email.setSubject("Bem vindo a Transoft!");
        email.setText("Sua senha é: " + password);
        javaMailSender.send(email);
    }

}
