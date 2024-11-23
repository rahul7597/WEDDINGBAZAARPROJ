package com.exampleoctober.octoberproj;

import java.time.LocalDateTime;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OTP {
    private static final Logger logger = LoggerFactory.getLogger(OTP.class);
    private final JavaMailSender javaMailSender;
    private final String sender;

    public OTP(JavaMailSender javaMailSender, @Value("${spring.mail.username}") String sender) {
        this.javaMailSender = javaMailSender;
        this.sender = sender;
    }

    public OTPResponse generateOTP(int length, String apiEmail) {
        try {
            logger.info("Generating OTP of length {} for {}", length, apiEmail);
            String numbers = "0123456789";
            StringBuilder otp = new StringBuilder(length);
            Random random = new Random();
            for (int i = 0; i < length; i++) {
                otp.append(numbers.charAt(random.nextInt(numbers.length())));
            }
            logger.debug("Generated OTP: {}", otp);
            String generatedOtp = otp.toString();
            LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(2);
            logger.info("OTP will expire in 2 minutes");

            // Send OTP via email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(apiEmail);
            mailMessage.setSubject("OTP for API Access");
            mailMessage.setText("Your OTP is: " + generatedOtp + "\nExpires in 2 minutes");
            javaMailSender.send(mailMessage);
            logger.info("OTP sent to {}", apiEmail);
            return new OTPResponse(generatedOtp, expiryTime);
        } catch (MailException e) {
            logger.error("Error sending mail: {}", e.getMessage());
            throw new RuntimeException("Error sending mail", e);
        } catch (Exception e) {
            logger.error("Error generating OTP: {}", e.getMessage());
            throw new RuntimeException("Error generating OTP", e);
        }
    }

    public static OTPResponse generateStaticOTP(int length, String apiEmail, OTP otp) {
        return otp.generateOTP(length, apiEmail);
    }

    public static class OTPResponse {
        public String otp;
        public LocalDateTime expiryTime;

        public OTPResponse(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }

        public boolean isExpired() {
            if (expiryTime == null) {
                logger.error("Expiry time is null");
                throw new NullPointerException("Expiry time is null");
            }
            boolean isExpired = LocalDateTime.now().isAfter(expiryTime);
            logger.debug("OTP expired: {}", isExpired);
            return isExpired;
        }
    }
}

