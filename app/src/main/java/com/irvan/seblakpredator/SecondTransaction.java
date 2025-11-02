package com.irvan.seblakpredator;

import android.annotation.SuppressLint;
import android.content.Intent;
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
    Button btnSemua, btnKerupuk, btnSayuran, btnTopping, btnMie, btnAyam;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_transaction);

        KotakMenu = findViewById(R.id.KotakMenu);
        btnSemua = findViewById(R.id.btnsemua);
        btnKerupuk = findViewById(R.id.btnkerupuk);
        btnSayuran = findViewById(R.id.btnsayuran);
        btnTopping = findViewById(R.id.btntopping);
        btnMie = findViewById(R.id.btnmie);
        btnAyam = findViewById(R.id.btnayam);

        LayoutInflater inflater = getLayoutInflater();
        Button lanjut = findViewById(R.id.btnLanjut);

        lanjut.setOnClickListener(v -> {
            Intent intent = new Intent(SecondTransaction.this, ThirdTransaction.class);
            startActivity(intent);
        });

        ImageView kembali = findViewById(R.id.btnBack);

        kembali.setOnClickListener(v -> {
            Intent intent = new Intent(SecondTransaction.this, FirstTransaction.class);
            startActivity(intent);
        });

        // 🔹 Semua produk
        String[] namaProduk = {
                "Rafael Putih", "Rafael Oren", "Krupuk Tangga", "Krupuk Bulat Warna", "Kerupuk Bulat Putih",
                "Krupuk Bulat Oren", "Rafael Warna Kecil", "Rafael Warna Besar", "Pakcoy", "Sawi",
                "Jeruk Limau", "Enoki", "Enoki Jumbo", "Jamur Salju", "Jamur Kuping",
                "Sosis Kecil", "Sosis Merah Sapi", "Sosis Oren Ayam", "Beef Burger", "Fish Cake",
                "Pentol Daging", "Tempura", "Tahu Bakso", "Cocktail", "Pentol Jumbo",
                "Cireng", "Kornet", "Ekor Udang Sunfish", "Gyoza Ikan", "Money Bag Ikan",
                "Siomay Ayam", "Siomay Udang", "Udang Gulung", "Odeng Ori", "Odeng Spicy",
                "Pentol Ikan", "Otak-otak Ikan", "Bola Salmon", "Kue Ikan", "Fish Roll",
                "Character Ikan", "Tahu Bakso Seafood", "Otak-otak Singapore", "Ekor Udang Cedea", "Duo Twister",
                "Flower Twister", "Crabstick", "Scallop", "Kembang Cumi", "Cikua Mini",
                "Cikua Long", "Dumpling Ayam", "Dumpling Keju", "Dumpling Spicy", "Mie Kecil",
                "Mie Pipih", "Bihun", "Kwetiau", "Tulangan", "Ceker",
                "Sayap"
        };

        String[] hargaProduk = {
                "Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 1.000",
                "Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 2.000", "Rp 7.000", "Rp 1.000", "Rp 1.000",
                "Rp 2.000", "Rp 3.000", "Rp 3.000", "Rp 2.000", "Rp 2.000",
                "Rp 2.000", "Rp 2.000", "Rp 2.000", "Rp 3.000", "Rp 5.000",
                "Rp 2.000", "Rp 2.000", "Rp 2.000", "Rp 2.000", "Rp 2.000",
                "Rp 2.000", "Rp 2.000", "Rp 3.000", "Rp 2.000", "Rp 2.000",
                "Rp 2.000", "Rp 2.000", "Rp 2.000", "Rp 2.000", "Rp 3.000",
                "Rp 2.000", "Rp 2.000", "Rp 3.000", "Rp 2.000", "Rp 3.000",
                "Rp 3.000", "Rp 2.000", "Rp 2.000", "Rp 2.000", "Rp 2.000",
                "Rp 4.000", "Rp 2.000", "Rp 2.000", "Rp 2.000",
                "Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 2.000",
                "Rp 1.000", "Rp 2.000", "Rp 5.000"
        };

        int[] gambarProduk = {
                R.drawable.img_rafael_putih, R.drawable.img_rafael_oren, R.drawable.img_kerupuk_tangga, R.drawable.img_kerupuk_bulat_warna, R.drawable.img_kerupuk_bulat_putih,
                R.drawable.img_kerupuk_bulat_oren, R.drawable.img_rafael_warna, R.drawable.img_rafael_warna, R.drawable.img_pakcoy, R.drawable.img_sawi,
                R.drawable.img_jeruk_limau, R.drawable.img_enoki, R.drawable.img_enoki_jumbo, R.drawable.img_jamur_salju, R.drawable.img_jamur_kuping,
                R.drawable.img_sosis_kecil, R.drawable.img_sosis_merah_sapi, R.drawable.img_sosis_oren_ayam, R.drawable.img_beef_burger, R.drawable.img_fish_cake, R.drawable.img_pentol_daging, R.drawable.img_tempura,
                R.drawable.img_tahu_bakso, R.drawable.img_cocktail, R.drawable.img_pentol_jumbo, R.drawable.img_cireng, R.drawable.img_kornet,
                R.drawable.img_ekor_udang_sunfish, R.drawable.img_gyoza_ikan, R.drawable.img_money_bag_ikan, R.drawable.img_siomay_ayam, R.drawable.img_siomay_udang, R.drawable.img_udang_gulung,
                R.drawable.img_odeng_ori, R.drawable.img_odeng_spicy, R.drawable.img_pentol_ikan, R.drawable.img_otak_otak_ikan, R.drawable.img_bola_salmon,
                R.drawable.img_kue_ikan, R.drawable.img_fish_roll, R.drawable.img_cedea_karakter, R.drawable.img_tahu_bakso_seafood, R.drawable.img_otak_otak_singapore,
                R.drawable.img_ekor_udang_cedea, R.drawable.img_duotwister, R.drawable.img_flower_twister, R.drawable.img_crabstick, R.drawable.img_scallop,
                R.drawable.img_kembang_cumi, R.drawable.img_cikuwa_mini, R.drawable.img_cikuwa_long, R.drawable.img_dumpling_ayam, R.drawable.img_dumpling_keju,
                R.drawable.img_dumpling_spicy, R.drawable.img_mie_kecil, R.drawable.img_mie_pipih, R.drawable.img_bihun, R.drawable.img_kwetiau,
                R.drawable.img_tulangan, R.drawable.img_ceker, R.drawable.img_sayap
        };


        // 🔸 Kategori Kerupuk
        String[] namaKerupuk = {"Rafael Putih", "Rafael Oren", "Kerupuk Tangga", "Kerupuk Bulat Warna", "Kerupuk Bulat Putih", "Kerupuk Bulat Oren", "Rafael Warna Kecil", "Rafael Warna Besar"};
        String[] hargaKerupuk = {"Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 1.000"};
        int[] gambarKerupuk = {R.drawable.img_rafael_putih, R.drawable.img_rafael_oren, R.drawable.img_kerupuk_tangga, R.drawable.img_kerupuk_bulat_warna, R.drawable.img_kerupuk_bulat_putih, R.drawable.img_kerupuk_bulat_oren, R.drawable.img_rafael_warna, R.drawable.img_rafael_warna};

        // 🔸 Kategori Sayuran
        String[] namaSayuran = {"Pakcoy", "Sawi", "Jeruk Limau", "Enoki", "Enoki Jumbo", "Jamur Salju", "Jamur Kuping"};
        String[] hargaSayuran = {"Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 2.000", "Rp 7.000", "Rp 1.000", "Rp 1.000"};
        int[] gambarSayuran = {R.drawable.img_pakcoy, R.drawable.img_sawi, R.drawable.img_jeruk_limau, R.drawable.img_enoki, R.drawable.img_enoki_jumbo, R.drawable.img_jamur_salju, R.drawable.img_jamur_kuping};

        // 🔸 Kategori Topping
        String[] namaTopping = {
                "Sosis Kecil", "Sosis Merah Sapi", "Sosis Oren Ayam", "Beef Burger", "Fish Cake",
                "Pentol Daging", "Tempura", "Tahu Bakso", "Cocktail", "Pentol Jumbo",
                "Cireng", "Kornet", "Ekor Udang Sunfish", "Gyoza Ikan", "Money Bag Ikan",
                "Siomay Ayam", "Siomay Udang", "Udang Gulung", "Odeng Ori", "Odeng Spicy",
                "Pentol Ikan", "Otak-otak Ikan", "Bola Salmon", "Kue Ikan", "Fish Roll",
                "Character Ikan", "Tahu Bakso Seafood", "Otak-otak Singapore", "Ekor Udang Cedea", "Duo Twister",
                "Flower Twister", "Crabstick", "Scallop", "Kembang Cumi", "Cikua Mini",
                "Cikua Long", "Dumpling Ayam", "Dumpling Keju", "Dumpling Spicy"
        };

        String[] hargaTopping = {"Rp 2.000", "Rp 3.000", "Rp 3.000", "Rp 2.000", "Rp 2.000",
                "Rp 2.000", "Rp 2.000", "Rp 2.000", "Rp 3.000", "Rp 5.000",
                "Rp 2.000", "Rp 2.000", "Rp 2.000", "Rp 2.000", "Rp 2.000",
                "Rp 2.000", "Rp 2.000", "Rp 3.000", "Rp 2.000", "Rp 2.000",
                "Rp 2.000", "Rp 2.000", "Rp 2.000", "Rp 2.000", "Rp 3.000",
                "Rp 2.000", "Rp 2.000", "Rp 3.000", "Rp 2.000", "Rp 3.000",
                "Rp 3.000", "Rp 2.000", "Rp 2.000", "Rp 2.000", "Rp 2.000",
                "Rp 4.000", "Rp 2.000", "Rp 2.000", "Rp 2.000"
        };
        int[] gambarTopping = {
                R.drawable.img_sosis_kecil, R.drawable.img_sosis_merah_sapi, R.drawable.img_sosis_oren_ayam, R.drawable.img_beef_burger, R.drawable.img_fish_cake,
                R.drawable.img_pentol_daging, R.drawable.img_tempura, R.drawable.img_tahu_bakso, R.drawable.img_cocktail, R.drawable.img_pentol_jumbo,
                R.drawable.img_cireng, R.drawable.img_kornet, R.drawable.img_ekor_udang_sunfish, R.drawable.img_gyoza_ikan, R.drawable.img_money_bag_ikan,
                R.drawable.img_siomay_ayam, R.drawable.img_siomay_udang, R.drawable.img_udang_gulung, R.drawable.img_odeng_ori, R.drawable.img_odeng_spicy,
                R.drawable.img_pentol_ikan, R.drawable.img_otak_otak_ikan, R.drawable.img_bola_salmon, R.drawable.img_kue_ikan, R.drawable.img_fish_roll,
                R.drawable.img_cedea_karakter, R.drawable.img_tahu_bakso_seafood, R.drawable.img_otak_otak_singapore, R.drawable.img_ekor_udang_cedea, R.drawable.img_duotwister,
                R.drawable.img_flower_twister, R.drawable.img_crabstick, R.drawable.img_scallop, R.drawable.img_kembang_cumi, R.drawable.img_cikuwa_mini,
                R.drawable.img_cikuwa_long, R.drawable.img_dumpling_ayam, R.drawable.img_dumpling_keju, R.drawable.img_dumpling_spicy
        };
        // 🔸 Kategori Mie
        String[] namaMie = {"Mie Kecil", "Mie Pipih", "Bihun", "Kuetiau"};
        String[] hargaMie = {"Rp 1.000", "Rp 1.000", "Rp 1.000", "Rp 1.000"};
        int[] gambarMie = {R.drawable.img_mie_kecil, R.drawable.img_mie_pipih, R.drawable.img_bihun, R.drawable.img_kwetiau};

        // 🔸 Kategori Ayam
        String[] namaAyam = {"Tulangan", "Ceker", "Sayap"};
        String[] hargaAyam = {"Rp 1.000", "Rp 2.000", "Rp 5.000"};
        int[] gambarAyam = {R.drawable.img_tulangan, R.drawable.img_ceker, R.drawable.img_sayap};


        // =============== SEMUA PRODUK ===============
        btnSemua.setOnClickListener(v -> {
            KotakMenu.removeAllViews();
            for (int i = 0; i < namaProduk.length; i++) {
                View itemView = inflater.inflate(R.layout.activity_menu, KotakMenu, false);
                ((ImageView) itemView.findViewById(R.id.imgProduk2)).setImageResource(gambarProduk[i]);
                ((TextView) itemView.findViewById(R.id.tvNamaProduk2)).setText(namaProduk[i]);
                ((TextView) itemView.findViewById(R.id.tvHargaProduk2)).setText(hargaProduk[i]);

                setupQtyLogic(itemView);

                KotakMenu.addView(itemView);
            }
        });

        // =============== KERUPUK ===============
        btnKerupuk.setOnClickListener(v -> {
            KotakMenu.removeAllViews();
            for (int i = 0; i < namaKerupuk.length; i++) {
                View itemView = inflater.inflate(R.layout.activity_menu, KotakMenu, false);
                ((ImageView) itemView.findViewById(R.id.imgProduk2)).setImageResource(gambarKerupuk[i]);
                ((TextView) itemView.findViewById(R.id.tvNamaProduk2)).setText(namaKerupuk[i]);
                ((TextView) itemView.findViewById(R.id.tvHargaProduk2)).setText(hargaKerupuk[i]);

                setupQtyLogic(itemView);

                KotakMenu.addView(itemView);
            }
        });

        // =============== SAYURAN ===============
        btnSayuran.setOnClickListener(v -> {
            KotakMenu.removeAllViews();
            for (int i = 0; i < namaSayuran.length; i++) {
                View itemView = inflater.inflate(R.layout.activity_menu, KotakMenu, false);
                ((ImageView) itemView.findViewById(R.id.imgProduk2)).setImageResource(gambarSayuran[i]);
                ((TextView) itemView.findViewById(R.id.tvNamaProduk2)).setText(namaSayuran[i]);
                ((TextView) itemView.findViewById(R.id.tvHargaProduk2)).setText(hargaSayuran[i]);

                setupQtyLogic(itemView);

                KotakMenu.addView(itemView);
            }
        });

        // =============== TOPPING ===============
        btnTopping.setOnClickListener(v -> {
            KotakMenu.removeAllViews();
            for (int i = 0; i < namaTopping.length; i++) {
                View itemView = inflater.inflate(R.layout.activity_menu, KotakMenu, false);
                ((ImageView) itemView.findViewById(R.id.imgProduk2)).setImageResource(gambarTopping[i]);
                ((TextView) itemView.findViewById(R.id.tvNamaProduk2)).setText(namaTopping[i]);
                ((TextView) itemView.findViewById(R.id.tvHargaProduk2)).setText(hargaTopping[i]);

                setupQtyLogic(itemView);

                KotakMenu.addView(itemView);
            }
        });

        // =============== MIE ===============
        btnMie.setOnClickListener(v -> {
            KotakMenu.removeAllViews();
            for (int i = 0; i < namaMie.length; i++) {
                View itemView = inflater.inflate(R.layout.activity_menu, KotakMenu, false);
                ((ImageView) itemView.findViewById(R.id.imgProduk2)).setImageResource(gambarMie[i]);
                ((TextView) itemView.findViewById(R.id.tvNamaProduk2)).setText(namaMie[i]);
                ((TextView) itemView.findViewById(R.id.tvHargaProduk2)).setText(hargaMie[i]);

                setupQtyLogic(itemView);

                KotakMenu.addView(itemView);
            }
        });
        // =============== AYAM ===============
        btnAyam.setOnClickListener(v -> {
            KotakMenu.removeAllViews();
            for (int i = 0; i < namaAyam.length; i++) {
                View itemView = inflater.inflate(R.layout.activity_menu, KotakMenu, false);
                ((ImageView) itemView.findViewById(R.id.imgProduk2)).setImageResource(gambarAyam[i]);
                ((TextView) itemView.findViewById(R.id.tvNamaProduk2)).setText(namaAyam[i]);
                ((TextView) itemView.findViewById(R.id.tvHargaProduk2)).setText(hargaAyam[i]);

                setupQtyLogic(itemView);

                KotakMenu.addView(itemView);
            }
        });
    }

    private void setupQtyLogic(View itemView) {
        ImageView btnTambahAwal = itemView.findViewById(R.id.btnTambah2);
        LinearLayout layoutQty = itemView.findViewById(R.id.layoutQty);
        ImageView btnTambah = itemView.findViewById(R.id.btnTambah);
        ImageView btnKurang = itemView.findViewById(R.id.btnKurang);
        TextView txtQty = itemView.findViewById(R.id.txtQty);

        btnTambahAwal.setOnClickListener(v -> {
            btnTambahAwal.setVisibility(View.GONE);
            layoutQty.setVisibility(View.VISIBLE);
            txtQty.setText("1");
        });

        btnTambah.setOnClickListener(v -> {
            int jumlah = Integer.parseInt(txtQty.getText().toString());
            jumlah++;
            txtQty.setText(String.valueOf(jumlah));
        });

        btnKurang.setOnClickListener(v -> {
            int jumlah = Integer.parseInt(txtQty.getText().toString());
            if (jumlah > 1) {
                jumlah--;
                txtQty.setText(String.valueOf(jumlah));
            } else {
                layoutQty.setVisibility(View.GONE);
                btnTambahAwal.setVisibility(View.VISIBLE);
            }
        });
    }
}
