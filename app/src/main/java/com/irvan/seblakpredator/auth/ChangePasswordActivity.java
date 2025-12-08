package com.irvan.seblakpredator.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.irvan.seblakpredator.R;
import com.irvan.seblakpredator.apiclient.ApiClient;
import com.irvan.seblakpredator.apiclient.ApiService;
import com.irvan.seblakpredator.model.ChangePasswordRequest;
import com.irvan.seblakpredator.model.ChangePasswordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPasswordInput, newPasswordInput, confirmPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPasswordInput = findViewById(R.id.oldPasswordInput);
        newPasswordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.passwordConfirmInput);

        findViewById(R.id.changePasswordButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String oldPassword = oldPasswordInput.getText().toString().trim();
        String newPassword = newPasswordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showCustomNotification("Kolom wajib diisi", 6000);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showCustomNotification("Password tidak sama", 6000);
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);

        if (userId == null) {
            Toast.makeText(this, "User ID is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        ChangePasswordRequest request = new ChangePasswordRequest(userId, oldPassword, newPassword, confirmPassword);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<ChangePasswordResponse> call = apiService.changePassword(request);

        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                if (response.isSuccessful()) {
                    ChangePasswordResponse changePasswordResponse = response.body();

                    if (changePasswordResponse != null && changePasswordResponse.isSuccess()) {

                        // ✅ KIRIM HASIL SUKSES KE ACTIVITY SEBELUMNYA
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("profile_update_success", true);
                        setResult(RESULT_OK, resultIntent);
                        finish(); // ✅ Langsung tutup activity

                    } else {
                        String apiMessage = changePasswordResponse != null
                                ? changePasswordResponse.getMessage()
                                : "Gagal mengubah password";

                        showErrorIncorrect(apiMessage);
                    }

                } else {
                    if (response.code() == 400 || response.code() == 401) {
                        showErrorIncorrect("Password lama anda salah");
                    } else {
                        showErrorIncorrect("Terjadi kesalahan pada server");
                    }
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        params.gravity = Gravity.CENTER;
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
