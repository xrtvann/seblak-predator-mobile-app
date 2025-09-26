package com.irvan.seblakpredator.ui.theme;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.irvan.seblakpredator.R;

public class LoginActivity extends AppCompatActivity {
    Button login, forgetPassword, register;
    EditText passwordText, usernameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameText = findViewById(R.id.usernameColumn);
        passwordText = findViewById(R.id.passwordColumn);
        login = findViewById(R.id.loginbutton);
        forgetPassword = findViewById(R.id.forgetbutton);
        register = findViewById(R.id.registerbutton);

        final boolean[] isPasswordVisible = {false};
        Typeface typeface = ResourcesCompat.getFont(this, R.font.arial);
        passwordText.setTypeface(typeface);

        passwordText.setOnTouchListener((v, event) ->{
            final int drawable_right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passwordText.getRight()
                        - passwordText.getCompoundDrawables()[drawable_right].getBounds().width())) {

                    if (isPasswordVisible[0]) {
                        // Sembunyikan password
                        passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        passwordText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eyehide, 0);
                        isPasswordVisible[0] = false;
                    } else {
                        // Tampilkan password
                        passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        passwordText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eyeshow, 0);
                        isPasswordVisible[0] = true;
                    }

                    // Biar cursor tetap di akhir teks
                    passwordText.setTypeface(ResourcesCompat.getFont(this, R.font.arial));
                    passwordText.setSelection(passwordText.getText().length());
                    return true;
                }
            }
            return false;
        } );
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}