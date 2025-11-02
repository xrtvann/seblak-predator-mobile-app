package com.irvan.seblakpredator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.irvan.seblakpredator.fragment.DashboardFragment;

public class ProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // 💡 Tambahan: Biar layout nutup sampai status bar
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0,0, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });
        // 🧩 Isi data dummy ke form
        TextInputEditText nameInput = findViewById(R.id.nameInput);
        TextInputEditText usernameInput = findViewById(R.id.usernameInput);
        TextInputEditText emailInput = findViewById(R.id.emailInput);
        TextInputEditText phoneInput = findViewById(R.id.phoneInput);

        nameInput.setText("Muhammad Fitrah");
        usernameInput.setText("mfitrah15");
        emailInput.setText("mfitrah15ofcc@gmail.com");
        phoneInput.setText("089528019977");
    }
}