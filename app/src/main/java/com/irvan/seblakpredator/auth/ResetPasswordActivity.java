package com.irvan.seblakpredator.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.irvan.seblakpredator.R;
import com.irvan.seblakpredator.apiclient.ApiClient;
import com.irvan.seblakpredator.apiclient.ApiService;
import com.irvan.seblakpredator.apiclient.ForgetPassRequest;
import com.irvan.seblakpredator.model.ForgetPassResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputLayout emailColumn;
    private TextInputEditText emailInput;
    private MaterialButton continueButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        initViews();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        continueButton.setOnClickListener(v -> sendOtp());

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        emailColumn = findViewById(R.id.emailColumn);
        emailInput = findViewById(R.id.emailInput);
        continueButton = findViewById(R.id.continueButton);

        emailInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailColumn.setError(null); // hapus error saat user mengetik
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private boolean validateEmail() {
        String email = emailInput.getText().toString().trim();

        if (email.isEmpty()) {
            emailColumn.setError("Email tidak boleh kosong");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailColumn.setError("Format email tidak valid");
            return false;
        }

        emailColumn.setError(null);
        return true;
    }

    private void sendOtp() {
        if (!validateEmail()) return;

        String email = emailInput.getText().toString().trim();

        ForgetPassRequest request = new ForgetPassRequest("send_otp", email);

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.forgetpassword(request).enqueue(new Callback<ForgetPassResponse>() {
            @Override
            public void onResponse(Call<ForgetPassResponse> call, Response<ForgetPassResponse> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this, "Error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                ForgetPassResponse res = response.body();
                if (res == null) {
                    Toast.makeText(ResetPasswordActivity.this, "Response kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!res.isSuccess()) {
                    emailColumn.setError(res.getMessage());
                    return;
                }

                Toast.makeText(ResetPasswordActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();

                // ➜ Pindah ke Activity Verifikasi OTP sambil membawa email
                Intent i = new Intent(ResetPasswordActivity.this, VerificationOTP.class);
                i.putExtra("email", email);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<ForgetPassResponse> call, Throwable t) {
                Toast.makeText(ResetPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
