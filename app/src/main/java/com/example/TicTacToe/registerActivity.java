package com.example.TicTacToe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class registerActivity extends AppCompatActivity {

    Button btnLoginA;
    Button btnRegisterM;
    private InputValidator validator;
    private FirebaseAuth auth;
    private fbController CreateUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent logintent = new Intent(registerActivity.this, LoginActivity.class);

        validator = new InputValidator();
        auth = FirebaseAuth.getInstance();


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLoginA = findViewById(R.id.btnLoginA);
        btnRegisterM = findViewById(R.id.btnRegisterM);

        btnLoginA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                registerActivity.this.startActivity(logintent);

            }
        });

        EditText etEmail = findViewById(R.id.etEmailR);
        EditText etPassword = findViewById(R.id.etPasswordR);
        EditText etPassword2 = findViewById(R.id.etPasswordR2);
        CreateUser = new fbController(registerActivity.this);


        btnRegisterM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String password2= etPassword2.getText().toString();

                if (!validator.isValidEmail(email)) {
                    etEmail.setError("This email is not valid");
                }
                else if (!validator.isValidPassword(password)) {
                    etPassword.setError("Password must contain at least 6 letters");
                }
                else if (!validator.isValidPassword2(password,password2)) {
                    etPassword2.setError("Passwords must be identical");
                }
                else {
                    User user = new User(email);
                    CreateUser.createUser(email, password,user);
                }
            }
        });

    }
}