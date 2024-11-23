package com.exampleoctober.octoberproj.ExceptionClass.Email;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class EmailFormatException extends Exception 
{
    private static final String emailformatting = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public EmailFormatException() 
    {
        super();
    }

    public EmailFormatException(String message) 
    {
        super(message);
    }

    public boolean validateEmail(String email) throws EmailFormatException 
    {
        if(email ==null || email.trim()=="")
        {
            throw new EmailFormatException("Email should not be Null !");
        }
        else if (email!=null && !Pattern.matches(emailformatting, email)) 
        {
            throw new EmailFormatException("Invalid Email Format");
        }
        else
        {
        return true;  
        }
    }
}

