package com.example.TicTacToe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
    Button btnLogin;
    Button btnRegister;
    private InputValidator validator; // Use your validator
    private fbController auth; // Declare, don't initialize yet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call super.onCreate *first*
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        validator = new InputValidator(); // Initialize your validator
        auth = new fbController(this); // Initialize fbController *here*
        auth.setNavigationListener(new fbController.NavigationListener() {
            @Override
            public void navigateToMain() {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish loginActivity so the user can't go back
            }

            @Override
            public void navigateToLogin() {
                // You might not need to do anything here, as you're already on the login screen
                // But you could show a message, clear fields, etc.
                Toast.makeText(LoginActivity.this, "Already on login screen", Toast.LENGTH_SHORT).show();
            }
        });
        if (auth.isLoggedIn()) {
            auth.navigateToMainActivity();
        }

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim(); // Trim whitespace
            String password = etPassword.getText().toString();

            // Use your InputValidator! (Assuming you have validation methods)
            if (!validator.isValidEmail(email)) {
                etEmail.setError("Invalid email format");
                etEmail.requestFocus(); // Set focus to the email field
                return; // Stop processing
            }

            if (!validator.isValidPassword(password)) {
                etPassword.setError("Password must be at least 6 characters");
                etPassword.requestFocus();
                return;
            }

            auth.loginUser(email, password);
        });

        btnRegister.setOnClickListener(v -> {
            Intent regintent = new Intent(this, registerActivity.class); // Create intent here
            startActivity(regintent);
            // Don't call finish() here.  You want the user to be able to come back to login
        });
    }
}