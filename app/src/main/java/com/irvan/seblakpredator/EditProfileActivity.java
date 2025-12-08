package com.irvan.seblakpredator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.irvan.seblakpredator.apiclient.ApiClient;
import com.irvan.seblakpredator.apiclient.ApiService;
import com.irvan.seblakpredator.model.TokenManager;
import com.irvan.seblakpredator.model.ProfileResponse;
import com.irvan.seblakpredator.model.UpdateProfileRequest;
import com.irvan.seblakpredator.model.UpdateProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    TextInputEditText nameInput, usernameInput, emailInput, phoneInput;
    Button saveButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editprofile);

        // Layout fullscreen handler
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameInput = findViewById(R.id.nameInput);
        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
        });

        String token = "Bearer " + TokenManager.getToken(this);
        loadProfile(token);

        saveButton.setOnClickListener(v -> saveProfile(token));

        // Cek flag dari EditProfileActivity
        if (getIntent().getBooleanExtra("profile_update_success", false)) {
            showCustomNotification("Data berhasil disimpan", 7000);
        }
    }

    private void loadProfile(String token) {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<ProfileResponse> call = apiService.getProfile(token);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    showCustomNotification("Gagal update profil", 6000);
                    return;
                }

                ProfileResponse.Data user = response.body().getData();

                nameInput.setText(user.getName());
                usernameInput.setText(user.getUsername());
                emailInput.setText(user.getEmail());
                phoneInput.setText(user.getPhone_number());
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                String message = "Error" + t.getMessage();
                showErrorIncorrect(message);
            }
        });
    }

    private void saveProfile(String token) {
        String name = nameInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();

        // Validasi input dan error handling
        if (name.isEmpty()) {
            nameInput.setError("Nama harus di isi");
            return;
        }
        if (username.isEmpty()) {
            usernameInput.setError("Username harus di isi");
            return;
        }
        if (email.isEmpty()) {
            emailInput.setError("Email harus di isi");
            return;
        }
        if (phone.isEmpty()) {
            phoneInput.setError("Nomor Telepon harus di isi");
            return;
        }

        UpdateProfileRequest request = new UpdateProfileRequest(name, username, email, phone);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        Call<UpdateProfileResponse> call = apiService.updateProfile(token, request);

        call.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    showCustomNotification("Gagal update profil", 2000);
                    return;
                }

                // Kirim flag ke ProfileActivity
                SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                prefs.edit().putString("name", name).apply();

                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                intent.putExtra("profile_update_success", true); // flag untuk notifikasi
                startActivity(intent);
                finish(); // Hancurkan EditProfileActivity
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                showErrorIncorrect("Error: " + t.getMessage());
            }
        });
    }

    private void showErrorIncorrect(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.notification_incorrectdata, null);
        TextView messageView = view.findViewById(R.id.note1);
        messageView.setText(message);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.dialog_enter);
        view.startAnimation(anim);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnOk = view.findViewById(R.id.okbutton);
        btnOk.setOnClickListener(v -> dialog.dismiss());
    }

    private void showCustomNotification(String message, int durationMillis) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.notification_custom, null);

        TextView messageText = layout.findViewById(R.id.note);
        messageText.setText(message);

        // Ambil root ViewGroup
        ViewGroup root = findViewById(android.R.id.content);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER; // tampil di tengah
        layout.setLayoutParams(params);

        root.addView(layout);

        layout.setAlpha(0f);
        layout.animate().alpha(1f).setDuration(300).start();

        layout.postDelayed(() -> {
            layout.animate().alpha(0f).setDuration(300).withEndAction(() -> {
                root.removeView(layout);
            }).start();
        }, durationMillis);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof TextInputEditText) {
                int[] scrcoords = new int[2];
                v.getLocationOnScreen(scrcoords);

                float x = ev.getRawX() + v.getLeft() - scrcoords[0];
                float y = ev.getRawY() + v.getTop() - scrcoords[1];

                // Jika klik di luar area EditText → tutup keyboard
                if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                    hideKeyboard();
                    v.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
