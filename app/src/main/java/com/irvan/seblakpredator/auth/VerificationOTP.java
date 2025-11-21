package com.irvan.seblakpredator.auth;

import android.app.Dialog;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chaos.view.PinView;
import com.google.android.material.button.MaterialButton;
import com.irvan.seblakpredator.R;
import com.irvan.seblakpredator.apiclient.ApiClient;
import com.irvan.seblakpredator.apiclient.ApiService;
import com.irvan.seblakpredator.apiclient.ForgetPassRequest;
import com.irvan.seblakpredator.model.ForgetPassResponse;
import com.irvan.seblakpredator.splash.SplashActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationOTP extends AppCompatActivity {

    private PinView pinView;
    private TextView timerText, btnResendOTP;
    private Button verifyButton, backButton;
    private String email;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verification_otp);

        // Ambil email dari intent
        email = getIntent().getStringExtra("email");

        initViews();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        pinView = findViewById(R.id.codepinOTP);
        timerText = findViewById(R.id.timerText);
        btnResendOTP = findViewById(R.id.btnResendOTP);
        verifyButton = findViewById(R.id.btnVerified); // tombol verify
        backButton = findViewById(R.id.backButton);

        // Fokus & keyboard
        pinView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(pinView, InputMethodManager.SHOW_IMPLICIT);

        startTimer();

        btnResendOTP.setOnClickListener(v -> resendOtp());

        verifyButton.setOnClickListener(v -> verifyOtp());

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(VerificationOTP.this,ResetPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void startTimer() {
        btnResendOTP.setEnabled(false);
        btnResendOTP.setVisibility(TextView.GONE);

        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                long detik = millisUntilFinished / 1000;
                timerText.setText("Kirim ulang kode dalam " + detik + " detik");
            }

            public void onFinish() {
                timerText.setText("Belum menerima kode?");
                btnResendOTP.setEnabled(true);
                btnResendOTP.setVisibility(TextView.VISIBLE);
            }
        }.start();
    }

    private void resendOtp() {
        Toast.makeText(this, "Mengirim ulang kode OTP...", Toast.LENGTH_SHORT).show();
        ForgetPassRequest req = new ForgetPassRequest("send_otp", email);
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.forgetpassword(req).enqueue(new Callback<ForgetPassResponse>() {
            @Override
            public void onResponse(Call<ForgetPassResponse> call, Response<ForgetPassResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(VerificationOTP.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startTimer();
                }
            }

            @Override
            public void onFailure(Call<ForgetPassResponse> call, Throwable t) {
                Toast.makeText(VerificationOTP.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyOtp() {
        String otp = pinView.getText().toString().trim();
        if (otp.isEmpty()) {
            Toast.makeText(this, "OTP tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        ForgetPassRequest req = new ForgetPassRequest("verify_otp", email, otp);
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.forgetpassword(req).enqueue(new Callback<ForgetPassResponse>() {
            @Override
            public void onResponse(Call<ForgetPassResponse> call, Response<ForgetPassResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(VerificationOTP.this, "Error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                ForgetPassResponse res = response.body();
                if (res == null || !res.isSuccess()) {
                    Toast.makeText(VerificationOTP.this, res != null ? res.getMessage() : "OTP tidak valid", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(VerificationOTP.this, "OTP berhasil diverifikasi", Toast.LENGTH_SHORT).show();

                // Lanjut ke ganti password
                Intent i = new Intent(VerificationOTP.this, CreatePasswordActivity.class);
                i.putExtra("email", email);
                i.putExtra("otp", otp);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<ForgetPassResponse> call, Throwable t) {
                Toast.makeText(VerificationOTP.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
