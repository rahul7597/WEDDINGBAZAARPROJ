package com.exampleoctober.octoberproj.ExceptionClass.Password;

import java.util.regex.Pattern;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// import com.spotrackagain.refurbish.ExceptionClass.Number.ALLNumberFormatException;
// import com.spotrackagain.refurbish.signUP.SignUpRepo;

@Component
public class PasswordFormatException extends Exception 
{

    private static final String passwordformatting = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";

    public PasswordFormatException(String message) {
        super(message);
    }
    PasswordFormatException()
    {

    }

    public boolean validatePassword(String password) throws PasswordFormatException 
    {
       
        if(password==null || password.trim()=="")
        {
            throw new PasswordFormatException("Password Should not be Null !");
        }
       else if (!Pattern.matches(passwordformatting,password)) 
        {
            throw new PasswordFormatException("Invalid Password Format. Please enter minimum 8 characters, 1 uppercase, 1 lowercase, 1 digit and 1 special character.");
        }
        else 
        {
            return true;
        }
    }

}