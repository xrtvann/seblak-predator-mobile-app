package com.irvan.seblakpredator.auth;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        // Mengambil referensi dari EditText dan Button
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

        // Validasi input
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "New password and confirmation do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ambil user_id dan token dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);  // Ambil user_id dari SharedPreferences

        // Pastikan user_id tidak null
        if (userId == null) {
            Toast.makeText(this, "User ID is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Membuat request body untuk API
        ChangePasswordRequest request = new ChangePasswordRequest(userId, oldPassword, newPassword, confirmPassword);

        // Panggil API ganti password menggunakan Retrofit
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<ChangePasswordResponse> call = apiService.changePassword(request);

        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                if (response.isSuccessful()) {
                    ChangePasswordResponse changePasswordResponse = response.body();
                    if (changePasswordResponse != null && changePasswordResponse.isSuccess()) {
                        // Tampilkan pesan sukses
                        Toast.makeText(ChangePasswordActivity.this, "Password successfully changed!", Toast.LENGTH_SHORT).show();
                        finish();  // Tutup activity ini dan kembali ke halaman sebelumnya
                    } else {
                        // Tampilkan pesan error yang dikembalikan oleh API
                        Toast.makeText(ChangePasswordActivity.this, changePasswordResponse != null ? changePasswordResponse.getMessage() : "Failed to change password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Tangani error ketika response gagal (misalnya error jaringan atau server error)
                    Toast.makeText(ChangePasswordActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                // Tangani failure jika terjadi kesalahan pada koneksi (misalnya, jaringan tidak tersedia)
                Toast.makeText(ChangePasswordActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
