package com.exampleoctober.octoberproj.Registration.RegisterService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.exampleoctober.octoberproj.OTP;
import com.exampleoctober.octoberproj.ExceptionClass.Email.EmailFormatException;
import com.exampleoctober.octoberproj.ExceptionClass.Name.NameFormatException;
import com.exampleoctober.octoberproj.ExceptionClass.Number.ALLNumberFormatException;
import com.exampleoctober.octoberproj.ExceptionClass.Password.PasswordFormatException;
import com.exampleoctober.octoberproj.Registration.RegisterEntity.RegisterEntity;
import com.exampleoctober.octoberproj.Registration.RegisterRepo.RegisterRepo;

@Service
public class RegisterService {

    private NameFormatException nameexp;
    private EmailFormatException emailexp;
    private ALLNumberFormatException numexp;
    PasswordFormatException passexp;
    private RegisterRepo repo;


        @Autowired
    private JavaMailSender javaMailSender;
    
    @Value("${spring.mail.username}")
    private String sender;
    RegisterService(NameFormatException nameexp,EmailFormatException emailexp, ALLNumberFormatException numexp, PasswordFormatException passexp,RegisterRepo repo)
    {
        this.nameexp=nameexp;
        this.emailexp=emailexp;
        this.numexp=numexp;
        this.passexp=passexp;
        this.repo=repo;
    }


    public ResponseEntity<Map<String, String>> saveValidUser(RegisterEntity e) {
        try {
            // Name, Email, and Number validation
            if (!nameexp.validateName(e.getName())) {
                throw new NameFormatException("Invalid Name!");
            } else if (!emailexp.validateEmail(e.getEmail())) {
                throw new EmailFormatException("Invalid Email!");
            } else if (!numexp.validateNumber(e.getNumber())) {
                throw new ALLNumberFormatException("Invalid Number!");
            } 
            // Check if Email or Number already exists
            RegisterEntity existingUser = repo.findByEmailOrNumber(e.getEmail(), e.getNumber());
            
            if (existingUser != null) {
                // Generate OTP and send to existing user
                OTP otp = new OTP(javaMailSender, sender);
        OTP.OTPResponse otpResponse = otp.generateOTP(6, existingUser.getEmail());

                // OTP.OTPResponse otpResponse = OTP.generateOTP(6,existingUser.getEmail());
                existingUser.setOtp(otpResponse.otp);
                existingUser.setOtpExpiryTime(otpResponse.expiryTime);
                repo.save(existingUser);
                System.out.println("OTP is = " + otpResponse.otp);
                return ResponseEntity.ok(Collections.singletonMap("message", "OTP sent to your email, Now Set Your Password"));
            } else {
                // Check if password is provided
                if (e.getPassword() != null) {
                    throw new PasswordFormatException("Password should not be provided during registration!");
                }
                
                // Generate OTP and send to new user
                OTP otp = new OTP(javaMailSender, sender);
                OTP.OTPResponse otpResponse = otp.generateOTP(6, e.getEmail());
                // OTP.OTPResponse otpResponse = OTP.generateOTP(6,e.getEmail());
                e.setOtp(otpResponse.otp);
                e.setOtpExpiryTime(otpResponse.expiryTime);
                repo.save(e);
                System.out.println("OTP is = " + otpResponse.otp);
                return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully,  OTP sent to your email, Now Set Your Password"));
            }
        
        } catch (NameFormatException e1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Invalid Name: " + e1.getMessage()));
        } catch (EmailFormatException e1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Invalid Email: " + e1.getMessage()));
        } catch (ALLNumberFormatException e1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Invalid Number: " + e1.getMessage()));
        } catch (PasswordFormatException e1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("error", e1.getMessage()));
        } 
    }


    
    public List<RegisterEntity> GetAllInfo() {
        return repo.findAll();
    }

    public RegisterEntity getUserByEmail(String extractedEmail) {
        return repo.findByEmail(extractedEmail);
    }
}