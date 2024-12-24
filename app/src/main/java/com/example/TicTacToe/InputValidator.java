package com.example.TicTacToe;
import android.util.Patterns;

public class InputValidator {

    public boolean isValidEmail(String email)
    {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPassword(String password)
    {
        return password != null && password.length() >= 6 ;
    }

    public boolean isValidPassword2(String password, String password2)
    {
        return password.equals(password2);
    }

}
