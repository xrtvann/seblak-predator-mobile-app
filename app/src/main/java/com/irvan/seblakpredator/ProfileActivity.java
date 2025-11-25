package com.irvan.seblakpredator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {

    TextView namaCostumer, emailCostumer, usernameCostumer, phoneCostumer;
    Button editprofileButton, backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Aktifkan edge-to-edge agar layout memanfaatkan seluruh layar
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_profile);

        // Sesuaikan padding untuk status bar dan navigation bar
        View header = findViewById(R.id.header);
        ViewCompat.setOnApplyWindowInsetsListener(header, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        namaCostumer = findViewById(R.id.nameCostumer);
        emailCostumer = findViewById(R.id.emailCostumer);
        usernameCostumer = findViewById(R.id.emailCostumer);
        phoneCostumer = findViewById(R.id.phoneCostumer);
        editprofileButton = findViewById(R.id.editprofilebutton);
        backButton = findViewById(R.id.backButton);

        editprofileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
            startActivity(intent);
        });
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
            startActivity(intent);
        });

    }

}