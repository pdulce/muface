package com.mfc.infra.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.StringUtils;

import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "mail")
public class ArqJavaMailSender {

    @Value("${arch.mail.password.encoded}")
    private String encodedPasswd;

    @Bean
    public JavaMailSender javaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com"); // Configura tu host SMTP aquí
        mailSender.setPort(587); // Configura el puerto SMTP aquí
        mailSender.setUsername("pedro.dulce@gmail.com"); // Configura tu usuario SMTP aquí
        mailSender.setPassword(encodedPasswd); // Configura tu contraseña SMTP aquí

        //  mailSender.setPassword(decodePass(encodedPasswd)); // Configura tu contraseña SMTP aquí

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); // Habilita el modo de depuración para ver los logs

        return mailSender;

    }

    private String decodePass(String passwd) {
        return StringUtils.hasText(passwd) ? new String(java.util.Base64.getDecoder().decode(passwd)) : null;
    }


}
