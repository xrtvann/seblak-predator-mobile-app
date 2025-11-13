package com.irvan.seblakpredator.auth;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chaos.view.PinView;
import com.irvan.seblakpredator.R;

public class VerificationOTP extends AppCompatActivity {

    private PinView pinView;
    private TextView timerText, btnResendOTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verification_otp);
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

        // ✅ Pastikan PinView bisa fokus & tampil keyboard
        pinView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(pinView, InputMethodManager.SHOW_IMPLICIT);

        // ✅ Timer awal
        startTimer();

        // ✅ Aksi saat tombol "Kirim Ulang OTP" diklik
        btnResendOTP.setOnClickListener(v -> {
            Toast.makeText(this, "Mengirim ulang kode OTP...", Toast.LENGTH_SHORT).show();
            startTimer(); // mulai ulang timer
        });
    }

    // Timer hitung mundur 30 detik
    private void startTimer() {
        btnResendOTP.setEnabled(false); // disable tombol saat timer berjalan

        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                long detik = millisUntilFinished / 1000;
                timerText.setText("Kirim ulang kode dalam " + detik + " detik");
            }

            public void onFinish() {
                timerText.setText("Kamu bisa kirim ulang kode sekarang");
                btnResendOTP.setEnabled(true); // aktifkan kembali
            }
        }.start();
    }
}