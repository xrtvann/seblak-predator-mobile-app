package com.irvan.seblakpredator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondTransaction extends AppCompatActivity {

    LinearLayout KotakMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_transaction);

        // Ambil container dari XML
        KotakMenu = findViewById(R.id.KotakMenu);

        // Data produk (contoh data statis)
        String[] namaProduk = {"Rafael Putih", "Jordan Hitam", "Nike Air", "Converse Classic"};
        String[] hargaProduk = {"Rp 1.000", "Rp 2.000", "Rp 3.000", "Rp 4.000"};
        int[] gambarProduk = {
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background
        };

        LayoutInflater inflater = getLayoutInflater();

        // 🔁 Loop untuk menambahkan item ke layout
        for (int i = 0; i < namaProduk.length; i++) {
            // Inflate layout item produk
            View itemView = inflater.inflate(R.layout.activity_menu, KotakMenu, false);

            // Ambil elemen-elemen dari item
            ImageView imgProduk = itemView.findViewById(R.id.imgProduk2);
            TextView tvNamaProduk = itemView.findViewById(R.id.tvNamaProduk2);
            TextView tvHargaProduk = itemView.findViewById(R.id.tvHargaProduk2);
            ImageView btnTambah = itemView.findViewById(R.id.btnTambah2);

            // Set data produk sesuai index
            imgProduk.setImageResource(gambarProduk[i]);
            tvNamaProduk.setText(namaProduk[i]);
            tvHargaProduk.setText(hargaProduk[i]);

            // Tambahkan view ke container utama
            KotakMenu.addView(itemView);
        }
    }
}

