package com.irvan.seblakpredator.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.irvan.seblakpredator.MainActivity;
import com.irvan.seblakpredator.ProfileActivity;
import com.irvan.seblakpredator.R;

import com.irvan.seblakpredator.apiclient.ApiClient;
import com.irvan.seblakpredator.apiclient.ApiService;
import com.irvan.seblakpredator.fragment.DashboardFragment;
import com.irvan.seblakpredator.model.LoginRequest;
import com.irvan.seblakpredator.model.TokenManager;
import com.irvan.seblakpredator.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout usernameColumn, passwordColumn;
    private TextInputEditText usernameInput, passwordInput;
    private MaterialButton loginButton;

    private TextView registerText, resetPasswordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        initViews();
        setupListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                    new OnBackInvokedCallback() {
                        @Override
                        public void onBackInvoked() {
                            Log.d("BackPressed", "onBackInvoked called");
                            showExitConfirmationDialog();
                        }
                    }
            );
        } else {
            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    Log.d("BackPressed", "handleOnBackPressed called");
                    showExitConfirmationDialog();
                }
            });
        }

    }

    private void initViews() {
        usernameColumn = findViewById(R.id.usernameColumn);
        passwordColumn = findViewById(R.id.passwordColumn);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerText = findViewById(R.id.linkRegister);
        resetPasswordText = findViewById(R.id.linkResetPassword);
    }

    private void setupListener() {
        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        resetPasswordText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });
        usernameInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usernameColumn.setError(null); // hapus error
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        passwordInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordColumn.setError(null); // hapus error
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty()) {
                usernameColumn.setError("Username harus di isi");
                return;
            }

            if (password.isEmpty()) {
                passwordColumn.setError("Password harus di isi");
                return;
            }

            // MULAI REQUEST API
            ApiService api = ApiClient.getClient(this).create(ApiService.class);
            LoginRequest request = new LoginRequest(username, password);

            api.login(request).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse res = response.body();

                        // Jika login berhasil
                        if (res.isSuccess()) {
                            // Menyimpan token di SharedPreferences
                            String accessToken = res.getAccessToken();

                            TokenManager.saveToken(LoginActivity.this, accessToken);

                            // Simpan nama user ke SharedPreferences
                            getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                                    .edit()
                                    .putString("name", res.getUser().getName())
                                    .apply();

                            Toast.makeText(LoginActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Log.d("LoginResponse", "Login Failed: " + res.getMessage());
                            showErrorIncorrect();  // Tampilkan notifikasi kesalahan
                        }
                    } else {
                        // Tangani error yang terjadi ketika server mengembalikan status error (misalnya, HTTP 401)
                        if (response.code() == 401) {
                            // Pesan khusus untuk error login (username/password salah)
                            Log.d("LoginResponse", "Login failed with 401 - Unauthorized.");
                            showErrorIncorrect();  // Tampilkan notifikasi "Username atau Password Salah"
                        } else {
                            // Tampilkan pesan error umum jika bukan 401
                            try {
                                String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                                String message = "Login gagal!\nHTTP Code: " + response.code() + "\n" + errorBody;
                                showErrorDialog(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    // Tangani error jaringan atau server
                    String message = "Error: " + t.getMessage();
                    showErrorDialog(message);
                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }


            });
        });

    }
    private void showErrorIncorrect() {
        // Inflate custom layout
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.notification_incorrectdata, null);

        // Buat dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.dialog_enter);
        view.startAnimation(anim);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Tombol OK
        Button btnOk = view.findViewById(R.id.okbutton);
        btnOk.setOnClickListener(v -> dialog.dismiss());
    }
    private void showErrorDialog(String message) {
        // Inflate custom layout
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.notification_errorsystem, null);

        // Set message di TextView (pastikan layout punya TextView dengan id misal errorMessage)
        TextView errorText = view.findViewById(R.id.errornote);
        errorText.setText(message);

        // Buat dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.dialog_enter);
        view.startAnimation(anim);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Tombol OK
        Button btnOk = view.findViewById(R.id.okbutton);
        btnOk.setOnClickListener(v -> dialog.dismiss());
    }

    public void showExitConfirmationDialog() {
        Log.d("BackPressed", "showExitConfirmationDialog called");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.notification_closeapp, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnOk = view.findViewById(R.id.okbutton);
        btnOk.setOnClickListener(v -> {
            Log.d("BackPressed", "Exit confirmed, finishing app");
            dialog.dismiss();
            finishAffinity();
        });

        Button btnBatal = view.findViewById(R.id.batalbutton);
        btnBatal.setOnClickListener(v -> {
            Log.d("BackPressed", "Exit cancelled");
            dialog.dismiss();
        });
    }


}