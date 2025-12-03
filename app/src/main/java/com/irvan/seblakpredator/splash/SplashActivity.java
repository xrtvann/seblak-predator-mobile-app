package com.irvan.seblakpredator.splash;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.irvan.seblakpredator.R;
import com.irvan.seblakpredator.SecondTransaction;
import com.irvan.seblakpredator.auth.LoginActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        ActionBar actionBar = getActionBar();

        setContentView(R.layout.activity_splash);

        // Pertama kali: cek koneksi!
        if (!isConnected()) {
            showNoConnectionPopup();
        } else {
            goToLogin();
        }
    }

    // ✔ Fungsi untuk cek apakah ada internet
    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    // ✔ Fungsi untuk menampilkan popup "Tidak Ada Koneksi"
    private void showNoConnectionPopup() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_disconnect_notification); // pastikan nama XML sama
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button retry = dialog.findViewById(R.id.reconnectbutton);
        Button cancel = dialog.findViewById(R.id.batalbutton);
        retry.setOnClickListener(v -> {
            dialog.dismiss();

            if (isConnected()) {
                goToLogin();
            } else {
                showNoConnectionPopup(); // muncul lagi kalau masih offline
            }
        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
            finishAffinity();
        });
        dialog.show();
    }

    // ✔ Fungsi masuk ke LoginActivity jika terkoneksi
    private void goToLogin() {
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}
