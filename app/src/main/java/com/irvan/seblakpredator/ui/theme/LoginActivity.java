package com.irvan.seblakpredator.ui.theme;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.irvan.seblakpredator.R;

public class LoginActivity extends AppCompatActivity {

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

        EditText passwordText = findViewById(R.id.passwordColumn);
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
    }
}