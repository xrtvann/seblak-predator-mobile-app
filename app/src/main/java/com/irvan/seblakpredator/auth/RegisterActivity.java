package com.irvan.seblakpredator.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.irvan.seblakpredator.R;
import com.irvan.seblakpredator.Users;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {


    private TextInputLayout nameColumn, emailColumn, phoneColumn, usernameColumn, passwordColumn;
    private TextInputEditText nameInput, emailInput, phoneInput, usernameInput, passwordInput;

    private MaterialButton registerButton;

    private TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        initViews();
        setupListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        nameColumn = findViewById(R.id.nameColumn);
        emailColumn = findViewById(R.id.emailColumn);
        phoneColumn = findViewById(R.id.phoneColumn);
        usernameColumn = findViewById(R.id.usernameColumn);
        passwordColumn = findViewById(R.id.passwordColumn);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);

        registerButton = findViewById(R.id.registerButton); // pastikan ID-nya benar
        loginText = findViewById(R.id.linkLogin);
    }

    private void setupListener() {
        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        registerButton.setOnClickListener(v -> registerUser());
    }
    private void registerUser(){
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

            }

}