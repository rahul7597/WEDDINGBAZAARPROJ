package com.exampleoctober.octoberproj.Registration.RegisterEntity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "october_table")
public class RegisterEntity 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;
   private String name;
   private String email;  
   private String number;
   private String password;
//    private String ConfirmPassword;
   private LocalDateTime otpExpiryTime=LocalDateTime.now().plusMinutes(2);;
   private String role;

   public String getRole() {
    return role;
}
public void setRole(String role) {
    this.role = role;
}
public LocalDateTime getOtpExpiryTime() {
    return otpExpiryTime;
}
public LocalDateTime setOtpExpiryTime(LocalDateTime otpExpiryTime) {
    this.otpExpiryTime = otpExpiryTime;
    System.out.println("OTP Expiry Time = "+otpExpiryTime);
    return otpExpiryTime;
}
// public String getConfirmPassword() {
//     return ConfirmPassword;
// }
// public void setConfirmPassword(String confirmPassword) {
//     this.ConfirmPassword = password;
// }
private String otp;
   public String getOtp() {
    return otp;
}
public String getNumber() {
    return number;
}
public void setNumber(String number) {
    this.number = number;
}

public int getId() {
    return id;
}
public void setId(int id) {
    this.id = id;
}
public String getName() {
    return name;
}
public void setName(String name) {
    this.name = name;
}
public String getEmail() {
    return email;
}
public void setEmail(String email) {
    this.email = email;
}

public String getPassword() {
    return password;
}
public void setPassword(String password) {
    this.password = password;
}
public void setOtp(String otp) {
    this.otp=otp;
}

}
