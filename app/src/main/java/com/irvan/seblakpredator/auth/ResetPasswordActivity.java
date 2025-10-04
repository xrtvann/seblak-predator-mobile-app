package com.irvan.seblakpredator.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
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
import com.irvan.seblakpredator.R;

public class ResetPasswordActivity extends AppCompatActivity {


    private LinearLayout step1Container, step2Container, step3Container;
    private TextInputLayout emailColumn, otpColumn, newPasswordColumn, confirmPasswordColumn;
    private TextInputEditText emailInput, otpInput, newPasswordInput, confirmPasswordInput;
    private MaterialButton continueButton, verifyButton, resetButton;
    private TextView titleText, subtitleText;
    private int currentStep = 1;
    private String enteredEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        initViews();
        setupListeners();
        updateUIForStep();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        step1Container = findViewById(R.id.step1Container);
        step2Container = findViewById(R.id.step2Container);
        step3Container = findViewById(R.id.step3Container);
        emailColumn = findViewById(R.id.emailColumn);
        otpColumn = findViewById(R.id.otpColumn);
        newPasswordColumn = findViewById(R.id.newPasswordColumn);
        confirmPasswordColumn = findViewById(R.id.confirmPasswordColumn);
        emailInput = findViewById(R.id.emailInput);
        otpInput = findViewById(R.id.otpInput);
        newPasswordInput = findViewById(R.id.newPasswordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        continueButton = findViewById(R.id.continueButton);
        verifyButton = findViewById(R.id.verifyButton);
        resetButton = findViewById(R.id.resetButton);
        titleText = findViewById(R.id.titleText);
        subtitleText = findViewById(R.id.subtitleText);
    }

    private void setupListeners() {


        // Real-time validation untuk OTP (hanya angka, max 6)
        otpInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String otp = s.toString();
                if (otp.length() > 6) {
                    otpInput.setText(otp.substring(0, 6));
                }
                if (!otp.matches("\\d*")) {
                    otpInput.setError("OTP harus berupa angka");
                } else {
                    otpColumn.setError(null);
                }
            }
        });

        // Real-time validation untuk password match
        TextWatcher passwordWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePasswordMatch();
            }
        };
        newPasswordInput.addTextChangedListener(passwordWatcher);
        confirmPasswordInput.addTextChangedListener(passwordWatcher);
        // Button listeners
        continueButton.setOnClickListener(v -> proceedToStep(2));
        verifyButton.setOnClickListener(v -> proceedToStep(3));
        resetButton.setOnClickListener(v -> resetPassword());
    }

    private void updateUIForStep() {
        step1Container.setVisibility(currentStep == 1 ? View.VISIBLE : View.GONE);
        step2Container.setVisibility(currentStep == 2 ? View.VISIBLE : View.GONE);
        step3Container.setVisibility(currentStep == 3 ? View.VISIBLE : View.GONE);
        // Update subtitle berdasarkan step
        switch (currentStep) {
            case 1:
                subtitleText.setText("Masukkan email untuk reset password");
                break;
            case 2:
                subtitleText.setText("Kode OTP telah dikirim ke email Anda");
                break;
            case 3:
                subtitleText.setText("Buat password baru yang aman");
                break;
        }
    }

    private void proceedToStep(int nextStep) {
        boolean isValid = false;
        switch (currentStep) {
            case 1:
                isValid = validateEmail();
                if (isValid) {
                    enteredEmail = emailInput.getText().toString().trim();
                    // Simulasi kirim OTP (ganti dengan API call)
                    Toast.makeText(this, "OTP dikirim ke " + enteredEmail, Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                isValid = validateOTP();
                break;
            case 3:
                isValid = validatePassword();
                break;
        }
        if (isValid) {
            currentStep = nextStep;
            updateUIForStep();
            scrollToTop();  // Scroll ke atas untuk step baru
        }
    }

    private boolean validateEmail() {
        String email = emailInput.getText().toString().trim();
        if (email.isEmpty()) {
            emailColumn.setError("Email tidak boleh kosong");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailColumn.setError("Format email tidak valid");
            return false;
        }
        emailColumn.setError(null);
        return true;
    }

    private boolean validateOTP() {
        String otp = otpInput.getText().toString().trim();
        if (otp.isEmpty()) {
            otpColumn.setError("OTP tidak boleh kosong");
            return false;
        } else if (otp.length() != 6 || !otp.matches("\\d{6}")) {
            otpColumn.setError("OTP harus 6 digit angka");
            return false;
        }
        otpColumn.setError(null);
        // Simulasi verifikasi OTP (ganti dengan API call, misal OTP benar = "123456")
        if (!otp.equals("123456")) {  // Ganti dengan logic verifikasi nyata
            otpColumn.setError("Kode OTP salah");
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        String newPass = newPasswordInput.getText().toString().trim();
        String confirmPass = confirmPasswordInput.getText().toString().trim();
        if (newPass.isEmpty()) {
            newPasswordColumn.setError("Password baru tidak boleh kosong");
            return false;
        } else if (newPass.length() < 6) {
            newPasswordColumn.setError("Password minimal 6 karakter");
            return false;
        }
        newPasswordColumn.setError(null);
        if (confirmPass.isEmpty()) {
            confirmPasswordColumn.setError("Konfirmasi password tidak boleh kosong");
            return false;
        } else if (!newPass.equals(confirmPass)) {
            confirmPasswordColumn.setError("Password tidak cocok");
            return false;
        }
        confirmPasswordColumn.setError(null);
        return true;
    }

    private void validatePasswordMatch() {
        String newPass = newPasswordInput.getText().toString().trim();
        String confirmPass = confirmPasswordInput.getText().toString().trim();
        if (!newPass.isEmpty() && !confirmPass.isEmpty() && !newPass.equals(confirmPass)) {
            confirmPasswordColumn.setError("Password tidak cocok");
        } else {
            confirmPasswordColumn.setError(null);
        }
    }

    private void resetPassword() {
        if (validatePassword()) {
            // Simulasi reset password (ganti dengan API call)
            Toast.makeText(this, "Password berhasil direset! Silakan login ulang.", Toast.LENGTH_LONG).show();
            // Kembali ke LoginActivity
            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void scrollToTop() {
        // Scroll ke atas saat ganti step
        findViewById(R.id.scrollView).scrollTo(0, 0);
    }
}