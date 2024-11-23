package com.exampleoctober.octoberproj.ExceptionClass.Number;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
@Component
public class ALLNumberFormatException extends Exception {

    private static final String numberformatting = "^[6-9][0-9]{9}$";

    public ALLNumberFormatException(String message) {
        super(message);
    }
    public ALLNumberFormatException() {
        super();
    }

    public boolean validateNumber(String number) throws ALLNumberFormatException 
    {
    //    String numbers=Long.toString(number);
        if(number==null || number.trim()=="")
        {
            throw new ALLNumberFormatException("Number Should not be Null !");
        }
        else if (!Pattern.matches(numberformatting,number)) 
        {
            throw new ALLNumberFormatException("Invalid Number Format. Please enter 10 digit number.");
        }
        else
        {
        return true;
        }
        
    }
}