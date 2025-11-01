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

        KotakMenu = findViewById(R.id.KotakMenu);
        btnsemua = findViewById(R.id.btnsemua);
        btnkerupuk = findViewById(R.id.btnkerupuk);
        btnsayuran = findViewById(R.id.btnsayuran);
        btnolahandaging = findViewById(R.id.btnolahandaging);
        btnmie = findViewById(R.id.btnmie);

        LayoutInflater inflater = getLayoutInflater();

        // 🔹 Semua produk
        String[] namaProduk = {
                "Rafael Putih", "Krupuk Tangga", "Krupuk Warna", "Krupuk oren", "Rafael Warna",
                "Macaroni Spiral","cuanki lidah","pilus cikur","siomay kering","mie kecil",
                "mie pipih","bihun","sosis kecil","kornet","sosis merah sapi","scallop",
                "fish roll","duo twister","odeng ori spicy","tahu bakso seafood","bakso ikan",
                "kembang cumi","siomay ayam","siomay udang","kue ikan bulat","pentol keju",
                "otak-otak singapure","crapstick","pentol jamur","beef burger","ekor udang chedea",
                "ekor udan sunfish","pakcoy","sawi","jeruk limau","enoki","sosis oren ayam",
                "enoki jumbo","bola salmon","dumpling ayam","dumpling keju","dumpling spicy",
                "cikua","cikua long","udang guling","tahu bakso","makaroni","otak-otak ikan",
                "sosis salju","kue ikan goreng","tempura","pentol jumbo","money bag","telor",
                "sayap","tulangan","ceker","kuetiau","tahu geprek","jemur kuping","cirawang ori",
                "cirawang pedas","gyoza ikan","jamur salju","cedea karakter","pentol udang","cireng"
        };

        String[] hargaProduk = {
                "Rp 1.000","Rp 1.000","Rp 1.000","Rp 1.000","Rp 1.000","Rp 1.000","Rp 1.500",
                "Rp 1.000","Rp 1.000","Rp 1.000","Rp 1.000","Rp 1.000","Rp 2.000","Rp 2.000",
                "Rp 3.000","Rp 2.000","Rp 3.000","Rp 3.000","Rp 2.000","Rp 2.000","Rp 2.000",
                "Rp 2.000","Rp 2.000","Rp 2.000","Rp 3.000","Rp 5.000","Rp 3.000","Rp 2.000",
                "Rp 2.000","Rp 2.000","Rp 2.000","Rp 2.000","Rp 1.000","Rp 1.000","Rp 1.000",
                "Rp 2.000","Rp 3.000","Rp 7.000","Rp 2.000","Rp 2.000","Rp 2.000","Rp 2.000",
                "Rp 2.000","Rp 4.000","Rp 3.000","Rp 2.000","Rp 1.000","Rp 2.000","Rp 2.000",
                "Rp 2.000","Rp 2.000","Rp 5.000","Rp 2.000","Rp 3.000","Rp 5.000","Rp 1.000",
                "Rp 2.000","Rp 1.000","Rp 1.500","Rp 1.000","Rp 1.000","Rp 1.000","Rp 2.000",
                "Rp 1.000","Rp 2.000","Rp 2.000","Rp 2.000"
        };

        int[] gambarProduk = new int[namaProduk.length];
        for (int i = 0; i < gambarProduk.length; i++) {
            gambarProduk[i] = R.drawable.ic_launcher_background; // default sementara
        }

        // 🔸 Kategori Kerupuk
        String[] namaKerupuk = {"Rafael Putih", "Krupuk Tangga", "Krupuk Warna", "Krupuk oren", "Rafael Warna"};
        String[] hargaKerupuk = {"Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 1.000"};

        // 🔸 Kategori Sayuran
        String[] namaSayuran = {"pakcoy","sawi","jeruk limau","enoki","enoki jumbo","jamur salju"};
        String[] hargaSayuran = {"Rp 1.000","Rp 1.000","Rp 1.000","Rp 2.000","Rp 3.000","Rp 2.000"};

        // 🔸 Kategori Olahan Daging
        String[] namaOlahanDaging = {
                "sosis kecil","kornet","sosis merah sapi","tahu bakso seafood",
                "beef burger","sosis oren ayam","tahu bakso","sosis salju",
                "sayap","tulangan","ceker"
        };
        String[] hargaOlahanDaging = {
                "Rp 2.000","Rp 2.000","Rp 3.000","Rp 2.000","Rp 2.000","Rp 3.000",
                "Rp 2.000","Rp 2.000","Rp 3.000","Rp 5.000","Rp 5.000"
        };

        // 🔸 Kategori Mie
        String[] namaMie = {"mie kecil","mie pipih","bihun","kuetiau"};
        String[] hargaMie = {"Rp 1.000","Rp 1.000","Rp 1.000","Rp 2.000"};

        // =============== SEMUA PRODUK ===============
        btnsemua.setOnClickListener(v -> {
            KotakMenu.removeAllViews();
            for (int i = 0; i < namaProduk.length; i++) {
                View itemView = inflater.inflate(R.layout.activity_menu, KotakMenu, false);
                ((ImageView) itemView.findViewById(R.id.imgProduk2)).setImageResource(gambarProduk[i]);
                ((TextView) itemView.findViewById(R.id.tvNamaProduk2)).setText(namaProduk[i]);
                ((TextView) itemView.findViewById(R.id.tvHargaProduk2)).setText(hargaProduk[i]);
                KotakMenu.addView(itemView);
            }
        });

        // =============== KERUPUK ===============
        btnkerupuk.setOnClickListener(v -> {
            KotakMenu.removeAllViews();
            for (int i = 0; i < namaKerupuk.length; i++) {
                View itemView = inflater.inflate(R.layout.activity_menu, KotakMenu, false);
                ((ImageView) itemView.findViewById(R.id.imgProduk2)).setImageResource(R.drawable.ic_launcher_background);
                ((TextView) itemView.findViewById(R.id.tvNamaProduk2)).setText(namaKerupuk[i]);
                ((TextView) itemView.findViewById(R.id.tvHargaProduk2)).setText(hargaKerupuk[i]);
                KotakMenu.addView(itemView);
            }
        });

        // =============== SAYURAN ===============
        btnsayuran.setOnClickListener(v -> {
            KotakMenu.removeAllViews();
            for (int i = 0; i < namaSayuran.length; i++) {
                View itemView = inflater.inflate(R.layout.activity_menu, KotakMenu, false);
                ((ImageView) itemView.findViewById(R.id.imgProduk2)).setImageResource(R.drawable.ic_launcher_background);
                ((TextView) itemView.findViewById(R.id.tvNamaProduk2)).setText(namaSayuran[i]);
                ((TextView) itemView.findViewById(R.id.tvHargaProduk2)).setText(hargaSayuran[i]);
                KotakMenu.addView(itemView);
            }
        });

        // =============== OLAHAN DAGING ===============
        btnolahandaging.setOnClickListener(v -> {
            KotakMenu.removeAllViews();
            for (int i = 0; i < namaOlahanDaging.length; i++) {
                View itemView = inflater.inflate(R.layout.activity_menu, KotakMenu, false);
                ((ImageView) itemView.findViewById(R.id.imgProduk2)).setImageResource(R.drawable.ic_launcher_background);
                ((TextView) itemView.findViewById(R.id.tvNamaProduk2)).setText(namaOlahanDaging[i]);
                ((TextView) itemView.findViewById(R.id.tvHargaProduk2)).setText(hargaOlahanDaging[i]);
                KotakMenu.addView(itemView);
            }
        });

        // =============== MIE ===============
        btnmie.setOnClickListener(v -> {
            KotakMenu.removeAllViews();
            for (int i = 0; i < namaMie.length; i++) {
                View itemView = inflater.inflate(R.layout.activity_menu, KotakMenu, false);
                ((ImageView) itemView.findViewById(R.id.imgProduk2)).setImageResource(R.drawable.ic_launcher_background);
                ((TextView) itemView.findViewById(R.id.tvNamaProduk2)).setText(namaMie[i]);
                ((TextView) itemView.findViewById(R.id.tvHargaProduk2)).setText(hargaMie[i]);
                KotakMenu.addView(itemView);
            }
        });
    }
}
