package com.mfc.infra.utils;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ArqEmailService {

    Logger logger = LoggerFactory.getLogger(ArqEmailService.class);
    @Autowired
    JavaMailSender javaMailSender;

    public String sendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("");
        message.setTo("");
        message.setSubject("Test Subject");
        message.setText("Test Body");

        javaMailSender.send(message);

        return "Mail sent successfully";
    }

    public String sendEmailwithAttachment() {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper messageHelper =
                    new MimeMessageHelper(message, true);

            messageHelper.setFrom("blackbaaza@gmail.com");
            messageHelper.setTo("blackbaaza@gmail.com");
            messageHelper.setSubject("Demo Subject");
            messageHelper.setText("Demo Body");

            File file = new File("Path To File");

            messageHelper.addAttachment(file.getName(), file);

            javaMailSender.send(message);

            return "Mail sent successfully";

        } catch (Exception e) {
            return "Mail sent failed";
        }
    }
}