package com.irvan.seblakpredator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class buttontambah extends AppCompatActivity {

    private TextView btnMinus, btnPlus, txtJumlah;
    private int jumlah = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttontambah); // pastikan sesuai nama layout kamu

        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        txtJumlah = findViewById(R.id.txtJumlah);

        // Tombol tambah
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumlah++;
                txtJumlah.setText(String.valueOf(jumlah));
            }
        });

        // Tombol kurang
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jumlah > 0) {
                    jumlah--;
                    txtJumlah.setText(String.valueOf(jumlah));
                }
            }
        });
    }
}