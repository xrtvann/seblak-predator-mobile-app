package com.irvan.seblakpredator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnTambah2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTambah2 = findViewById(R.id.btnTambah2); // pastikan ID-nya sesuai dengan XML

        btnTambah2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk pindah ke activity Buttontambah
                Intent intent = new Intent(MainActivity.this, Buttontambah.class);
                startActivity(intent);
            }
        });
    }
}