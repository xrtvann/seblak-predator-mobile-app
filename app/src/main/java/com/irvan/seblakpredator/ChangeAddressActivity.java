package com.irvan.seblakpredator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class ChangeAddressActivity extends AppCompatActivity {

    private Button save, cancel;
    private TextInputEditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_changeaddress);

        // Pindahkan findViewById setelah setContentView
        save = findViewById(R.id.savebutton);
        cancel = findViewById(R.id.backbutton);
        input = findViewById(R.id.addressInput);

        save.setOnClickListener(v -> {
            String newAddress = input.getText().toString();

            Intent result = new Intent();
            result.putExtra("address", newAddress);
            setResult(RESULT_OK, result);
            finish();
        });

        cancel.setOnClickListener(v -> {
            // Jangan start activity baru, cukup finish
            finish();
        });
    }
}
