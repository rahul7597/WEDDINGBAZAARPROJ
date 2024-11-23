package com.exampleoctober.octoberproj.ExceptionClass.Name;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class NameFormatException extends Exception 
{

    private static final String nameformatting = "^[a-zA-Z ]{2,50}$";

    public NameFormatException(String message) {
        super(message);
    }
    NameFormatException()
    {

    }

    public boolean validateName(String name) throws NameFormatException {
        // Email format validation
        if (!Pattern.matches(nameformatting, name)) {
            throw new NameFormatException("Invalid Name Format");
        }
        else
        {
        return true;
        }
    }
}


