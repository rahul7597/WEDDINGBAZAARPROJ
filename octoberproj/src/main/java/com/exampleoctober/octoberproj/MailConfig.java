package com.exampleoctober.octoberproj;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
 
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        
        // mail sender settings
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("busiiness4uu@gmail.com");
        mailSender.setPassword("dcynufsbidaenaho");
        
        // TLS aur SSL settings
        mailSender.setJavaMailProperties(System.getProperties());
        System.getProperties().put("mail.smtp.auth", true);
        System.getProperties().put("mail.smtp.starttls.enable", true);
        System.getProperties().put("mail.smtp.ssl.trust", "smtp.gmail.com");
        
        return mailSender;
    }

}
