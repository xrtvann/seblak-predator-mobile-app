package com.irvan.seblakpredator.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.irvan.seblakpredator.MainActivity;
import com.irvan.seblakpredator.R;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout usernameColumn, passwordColumn;
    private TextInputEditText usernameInput, passwordInput;
    private MaterialButton loginButton;

    private TextView registerText, resetPasswordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        initViews();
        setupListener();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        usernameColumn = findViewById(R.id.usernameColumn);
        passwordColumn = findViewById(R.id.passwordColumn);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerText = findViewById(R.id.linkRegister);
        resetPasswordText = findViewById(R.id.linkResetPassword);
    }

    private void setupListener() {
        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        resetPasswordText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()){
                usernameColumn.setError("Username harus di isi");
                passwordColumn.setError("Password harus di isi");
                return;
            }

            if (username.equals("irvan") && password.equals("irvan123")) {
                Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show();

                // Pindah ke halaman dashboard (contoh)
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Username atau password salah!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}