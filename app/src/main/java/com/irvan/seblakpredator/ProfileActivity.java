package com.irvan.seblakpredator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.irvan.seblakpredator.apiclient.ApiClient;
import com.irvan.seblakpredator.apiclient.ApiService;
import com.irvan.seblakpredator.model.TokenManager;
import com.irvan.seblakpredator.auth.LoginActivity;
import com.irvan.seblakpredator.model.ProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    TextView namaCostumer, emailCostumer, usernameCostumer, phoneCostumer;
    Button editprofileButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inisialisasi tampilan
        namaCostumer = findViewById(R.id.nameCostumer);
        emailCostumer = findViewById(R.id.emailCostumer);
        usernameCostumer = findViewById(R.id.usernameCostumer);
        phoneCostumer = findViewById(R.id.phoneCostumer);
        editprofileButton = findViewById(R.id.editprofilebutton);
        backButton = findViewById(R.id.backButton);

        // Ambil token dari SharedPreferences
        String rawToken = TokenManager.getToken(this).trim();
        String token = "Bearer " + rawToken;
        // Cek flag dari EditProfileActivity
        if (getIntent().getBooleanExtra("profile_update_success", false)) {
            showCustomNotification("Data berhasil disimpan", 4000);
        }

        Log.d("ProfileActivity", "Token retrieved: " + token);

        if (rawToken.isEmpty()) {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        } else {
            getProfile(token);
        }

        editprofileButton.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class)));

        backButton.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, MainActivity.class)));
    }

    private void getProfile(String token) {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<ProfileResponse> call = apiService.getProfile(token);

        Log.d("API_CALL", "=== GET PROFILE CALLED ===");
        Log.d("API_CALL", "Token: " + token);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                Log.d("API_RESPONSE", "Response Code: " + response.code());
                Log.d("API_RESPONSE", "Message: " + response.message());

                if (!response.isSuccessful()) {
                    try {
                        String errorBody = response.errorBody() != null
                                ? response.errorBody().string()
                                : "null";
                        Log.e("API_ERROR_BODY", errorBody);
                    } catch (Exception e) {
                        Log.e("API_ERROR_PARSE", "Failed to parse error body", e);
                    }
                    showErrorIncorrect("Error code: " + response.code() + " - " + response.message());
                    return;
                }

                if (response.body() == null) {
                    Log.e("API_NULL", "Response body is NULL");
                    showErrorIncorrect("Data kosong dari server.");
                    return;
                }

                ProfileResponse profileResponse = response.body();

                // === FIX : cek data (tidak perlu cek getUser() lagi) ===
                if (profileResponse.getData() == null) {
                    Log.e("API_NULL", "Data kosong: " + new Gson().toJson(profileResponse));
                    showErrorIncorrect("Data user kosong atau token salah!");
                    return;
                }

                // Ambil data (langsung dari data, bukan data.user)
                ProfileResponse.Data user = profileResponse.getData();

                namaCostumer.setText(user.getName() != null ? user.getName() : "-");
                emailCostumer.setText(user.getEmail() != null ? user.getEmail() : "-");
                usernameCostumer.setText(user.getUsername() != null ? user.getUsername() : "-");
                phoneCostumer.setText(user.getPhone_number() != null ? user.getPhone_number() : "-");

                Log.d("API_SUCCESS_JSON", new Gson().toJson(profileResponse));
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.e("API_FAILURE", "Request failed: " + t.getMessage());
                showErrorIncorrect("Gagal menghubungi server: " + t.getMessage());
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
}
