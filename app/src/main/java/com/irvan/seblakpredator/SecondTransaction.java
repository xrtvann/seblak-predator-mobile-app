package com.irvan.seblakpredator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondTransaction extends AppCompatActivity {

    LinearLayout KotakMenu;
    Button btnsemua, btnkerupuk, btnsayuran, btnolahandaging, btnmie;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_transaction);

        // 🔹 Ambil container & tombol dari XML
        KotakMenu = findViewById(R.id.KotakMenu);
        btnsemua = findViewById(R.id.btnsemua);
        btnkerupuk = findViewById(R.id.btnkrupuk);
        btnsayuran = findViewById(R.id.btnsayuran);
        btnolahandaging = findViewById(R.id.btnolahandaging);
        btnmie = findViewById(R.id.btnmie);

        LayoutInflater inflater = getLayoutInflater();

        // 🔹 Data produk utama
        String[] namaProduk = {
                "Rafael Putih", "Krupuk Tangga", "Krupuk Warna", "Krupuk oren",
                "Rafael Warna","Macaroni Spiral","Cuanki Lidah","Pilus Cikur"
        };
        String[] hargaProduk = {
                "Rp 1.000","Rp 1.000","Rp 1.000","Rp 1.000",
                "Rp 1.000","Rp 1.000","Rp 1.500","Rp 1.000"
        };
        int[] gambarProduk = {
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background
        };

        // 🔹 Data kategori kerupuk
        String[] namaKerupuk = {"Rafael Putih", "Krupuk Tangga", "Krupuk Warna", "Krupuk oren"};
        String[] hargaKerupuk = {"Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 1.000"};
        int[] gambarkrupuk = {
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background
        };

        // 🔹 Tampilkan semua produk
        btnsemua.setOnClickListener(v -> {
            KotakMenu.removeAllViews();
            for (int i = 0; i < namaProduk.length; i++) {
                View itemView = inflater.inflate(R.layout.activity_menu, KotakMenu, false);

                ImageView imgProduk = itemView.findViewById(R.id.imgProduk2);
                TextView tvNamaProduk = itemView.findViewById(R.id.tvNamaProduk2);
                TextView tvHargaProduk = itemView.findViewById(R.id.tvHargaProduk2);
                ImageView btnTambah = itemView.findViewById(R.id.btnTambah2);

                imgProduk.setImageResource(gambarProduk[i]);
                tvNamaProduk.setText(namaProduk[i]);
                tvHargaProduk.setText(hargaProduk[i]);

                KotakMenu.addView(itemView);
            }
        });

        // 🔹 Tampilkan hanya produk kategori kerupuk
        btnkerupuk.setOnClickListener(v -> {
            KotakMenu.removeAllViews();
            for (int i = 0; i < namaKerupuk.length; i++) {
                View itemView = inflater.inflate(R.layout.activity_menu, KotakMenu, false);

                ImageView imgProduk = itemView.findViewById(R.id.imgProduk2);
                TextView tvNamaProduk = itemView.findViewById(R.id.tvNamaProduk2);
                TextView tvHargaProduk = itemView.findViewById(R.id.tvHargaProduk2);
                ImageView btnTambah = itemView.findViewById(R.id.btnTambah2);

                imgProduk.setImageResource(gambarkrupuk[i]);
                tvNamaProduk.setText(namaKerupuk[i]);
                tvHargaProduk.setText(hargaKerupuk[i]);

                KotakMenu.addView(itemView);
            }
        });
    }
}
