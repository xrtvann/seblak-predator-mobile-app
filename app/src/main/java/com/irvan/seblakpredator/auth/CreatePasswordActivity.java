package com.irvan.seblakpredator.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.irvan.seblakpredator.R;
import com.irvan.seblakpredator.apiclient.ApiClient;
import com.irvan.seblakpredator.apiclient.ApiService;
import com.irvan.seblakpredator.model.ForgetPassRequest;
import com.irvan.seblakpredator.model.ForgetPassResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePasswordActivity extends AppCompatActivity {

    private TextInputLayout passwordColumn, passwordConfirmColumn;
    private TextInputEditText passwordInput, passwordConfirmInput;
    private Button continueButton;

    private String email, otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_password);

        email = getIntent().getStringExtra("email");
        otp = getIntent().getStringExtra("otp"); // bisa dikirim dari VerificationOTP

        initViews();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        continueButton.setOnClickListener(v -> resetPassword());
    }

    private void initViews() {
        passwordColumn = findViewById(R.id.passwordColumn);
        passwordConfirmColumn = findViewById(R.id.passwordConfirmColumn);
        passwordInput = findViewById(R.id.passwordInput);
        passwordConfirmInput = findViewById(R.id.passwordConfirmInput);
        continueButton = findViewById(R.id.createPasswordButton);

        passwordInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordColumn.setError(null); // hapus error saat user mengetik
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        passwordConfirmInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordConfirmColumn.setError(null); // hapus error saat user mengetik
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private boolean validatePassword() {
        String password = passwordInput.getText().toString().trim();
        String confirm = passwordConfirmInput.getText().toString().trim();

        if (password.isEmpty()) {
            passwordColumn.setError("Password tidak boleh kosong");
            return false;
        }
        if (password.length() < 6) {
            passwordColumn.setError("Password minimal 6 karakter");
            return false;
        }
        passwordColumn.setError(null);

        if (!password.equals(confirm)) {
            passwordConfirmInput.setError("Password tidak sama");
            return false;
        }
        passwordConfirmInput.setError(null);
        return true;
    }

    private void resetPassword() {
        if (!validatePassword()) return;

        String password = passwordInput.getText().toString().trim();
        String confirm = passwordConfirmInput.getText().toString().trim();

        ForgetPassRequest req = new ForgetPassRequest("reset_password", email, otp, password, confirm);
        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        api.forgetpassword(req).enqueue(new Callback<ForgetPassResponse>() {
            @Override
            public void onResponse(Call<ForgetPassResponse> call, Response<ForgetPassResponse> response) {
                if (!response.isSuccessful()) {
                    String message = "Error " + response.code();
                    showErrorDialog(message);
                    Toast.makeText(CreatePasswordActivity.this, "Error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                ForgetPassResponse res = response.body();
                if (res == null || !res.isSuccess()) {
                    Toast.makeText(CreatePasswordActivity.this, res != null ? res.getMessage() : "Gagal mengganti password", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(CreatePasswordActivity.this, "Password berhasil diubah", Toast.LENGTH_SHORT).show();
                // Tambahkan intent ke LoginActivity sebelum finish()
                Intent intent = new Intent(CreatePasswordActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // bisa kembali ke login
            }

            @Override
            public void onFailure(Call<ForgetPassResponse> call, Throwable t) {
                Toast.makeText(CreatePasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showErrorDialog(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.notification_errorsystem, null);

        TextView errorText = view.findViewById(R.id.errornote);
        errorText.setText(message);

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
