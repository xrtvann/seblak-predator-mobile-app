package com.irvan.seblakpredator.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.irvan.seblakpredator.R;
import com.irvan.seblakpredator.apiclient.ApiClient;
import com.irvan.seblakpredator.apiclient.ApiService;
import com.irvan.seblakpredator.apiclient.RegisterRequest;
import com.irvan.seblakpredator.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout nameColumn, emailColumn, usernameColumn, passwordColumn;
    private TextInputEditText nameInput, emailInput, usernameInput, passwordInput;
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
        usernameColumn = findViewById(R.id.usernameColumn);
        passwordColumn = findViewById(R.id.passwordColumn);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);

        registerButton = findViewById(R.id.registerButton);
        loginText = findViewById(R.id.linkLogin);
    }

    private void setupListener() {

        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        nameInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameColumn.setError(null); // hapus error
            }
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
        emailInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailColumn.setError(null); // hapus error
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
        usernameInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usernameColumn.setError(null); // hapus error
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
        passwordInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordColumn.setError(null); // hapus error
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {

        String name = nameInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (name.isEmpty()) { nameColumn.setError("Nama harus diisi"); return; }
        if (username.isEmpty()) { usernameColumn.setError("Username harus diisi"); return; }
        if (email.isEmpty()) { emailColumn.setError("Email harus diisi"); return; }
        if (password.isEmpty()) { passwordColumn.setError("Password harus diisi"); return; }

        RegisterRequest request = new RegisterRequest(
                name, username, email, password
        );

        Gson gson = new Gson();
        Log.d("REGISTER_DEBUG", "BODY: " + gson.toJson(request));

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<RegisterResponse> call = apiService.register(request);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                Log.d("REGISTER_DEBUG", "HTTP CODE: " + response.code());

                if (!response.isSuccessful()) {

                    try {
                        Log.e("REGISTER_DEBUG", "RAW ERROR: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("REGISTER_DEBUG", "ERROR PARSING", e);
                    }

                    showErrorDialog("Error Server " + response.code());
                    return;
                }

                RegisterResponse res = response.body();
                Log.d("REGISTER_DEBUG", "RESPONSE: " + gson.toJson(res));

                if (res == null) {
                    showErrorDialog("Response kosong dari server");
                    return;
                }

                if (res.isSuccess()) {

                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    showErrorDialog(res.getMessage());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("REGISTER_DEBUG", "FAIL: " + t.getMessage(), t);
                showErrorDialog("Gagal terhubung ke server:\n" + t.getMessage());
            }
        });
    }

    private void showErrorDialog(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.notification_errorsystem, null);

        TextView errorText = view.findViewById(R.id.errornote);
        errorText.setText(message);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.dialog_enter);
        view.startAnimation(anim);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnOk = view.findViewById(R.id.okbutton);
        btnOk.setOnClickListener(v -> dialog.dismiss());
    }
}
