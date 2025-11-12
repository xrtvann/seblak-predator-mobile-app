package com.irvan.seblakpredator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ThirdTransaction extends AppCompatActivity {

        LinearLayout KotakMenu2;
        Button allbutton, snackbutton, drinkbutton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_third_transaction);

            // Ambil container dari XML

            KotakMenu2 = findViewById(R.id.KotakMenu);
            allbutton = findViewById(R.id.btnSemua);
            snackbutton = findViewById(R.id.btnSnack);
            drinkbutton = findViewById(R.id.btnMinuman);

            // Data produk (contoh data statis)
            String[] namaProduk = {"Cheers", "Nutrisari", "Pop Ice", "Teh (hangat/es)", "Jeruk (hangat/es)", "Milo", "Hilo", "Chocolatos", "Kopi", "Molreng", "Twistko", "Kerupuk Ikan", "Suki"};
            String[] hargaProduk = {"Rp 3.000", "Rp 3.000", "Rp 5.000", "Rp 3.000", "Rp 4.000", "Rp 5.000", "Rp 5.000", "Rp 5.000", "Rp 3.500", "Rp 2.500", "Rp 1.000", "Rp 2.500", "Rp 1.000"};
            String[] namaSnack = {"Molreng", "Twistko", "Kerupuk Ikan", "Suki"};
            String[] hargaSnack = {"Rp 2.500", "Rp 1.000", "Rp 2.500", "Rp 1.000"};
            String[] namaMinuman = {"Cheers", "Nutrisari", "Pop Ice", "Teh (hangat/es)", "Jeruk (hangat/es)", "Milo", "Hilo", "Chocolatos", "Kopi"};
            String[] hargaMinuman = {"Rp 3.000", "Rp 3.000", "Rp 5.000", "Rp 3.000", "Rp 4.000", "Rp 5.000", "Rp 5.000", "Rp 5.000", "Rp 3.500"};
            int[] gambarProduk = {
                    R.drawable.img_cheers,
                    R.drawable.img_nutrisari,
                    R.drawable.img_popice,
                    R.drawable.img_teh,
                    R.drawable.img_jeruk,
                    R.drawable.img_milo,
                    R.drawable.img_hilo,
                    R.drawable.img_chocolatos,
                    R.drawable.img_kopi,
                    R.drawable.img_molreng,
                    R.drawable.img_twistko,
                    R.drawable.img_kerupukikan,
                    R.drawable.img_suki
            };
            int[] gambarSnack = {
                    R.drawable.img_molreng,
                    R.drawable.img_twistko,
                    R.drawable.img_kerupukikan,
                    R.drawable.img_suki
            };
            int[] gambarMinuman = {
                    R.drawable.img_cheers,
                    R.drawable.img_nutrisari,
                    R.drawable.img_popice,
                    R.drawable.img_teh,
                    R.drawable.img_jeruk,
                    R.drawable.img_milo,
                    R.drawable.img_hilo,
                    R.drawable.img_chocolatos,
                    R.drawable.img_kopi
            };

            LayoutInflater inflater = getLayoutInflater();
            Button tambahPesanan = findViewById(R.id.btnTambahPesanan);

            tambahPesanan.setOnClickListener(v -> {
                Intent intent = new Intent(ThirdTransaction.this, FirstTransaction.class);
                startActivity(intent);
            });
            ImageView profil = findViewById(R.id.btnProfile);

            profil.setOnClickListener(v -> {
                Intent intent = new Intent(ThirdTransaction.this, ProfileActivity.class);
                startActivity(intent);
            });
            ImageView keranjang = findViewById(R.id.btnCart);

            keranjang.setOnClickListener(v -> {
                Intent intent = new Intent(ThirdTransaction.this, KeranjangActivity.class);
                startActivity(intent);
            });

            // 🔁 Loop untuk menambahkan item ke layout
            for (int i = 0; i < namaProduk.length; i++) {
                // Inflate layout item produk
                View itemView = inflater.inflate(R.layout.activity_menu_kedua, KotakMenu2, false);

                // Ambil elemen-elemen dari item
                ImageView imgProduk = itemView.findViewById(R.id.imgProduk2);
                TextView tvNamaProduk = itemView.findViewById(R.id.tvNamaProduk2);
                TextView tvHargaProduk = itemView.findViewById(R.id.tvHargaProduk2);
                ImageView btnTambah = itemView.findViewById(R.id.btnTambah2);

                // Set data produk sesuai index
                imgProduk.setImageResource(gambarProduk[i]);
                tvNamaProduk.setText(namaProduk[i]);
                tvHargaProduk.setText(hargaProduk[i]);

                setupQtyLogic(itemView);


                // Tambahkan view ke container utama
                KotakMenu2.addView(itemView);
            }
            allbutton.setOnClickListener(v -> {
                KotakMenu2.removeAllViews();
                for (int i = 0; i < namaProduk.length; i++) {
                    // Inflate layout item produk
                    View itemView = inflater.inflate(R.layout.activity_menu_kedua, KotakMenu2, false);

                    // Ambil elemen-elemen dari item
                    ImageView imgProduk = itemView.findViewById(R.id.imgProduk2);
                    TextView tvNamaProduk = itemView.findViewById(R.id.tvNamaProduk2);
                    TextView tvHargaProduk = itemView.findViewById(R.id.tvHargaProduk2);
                    ImageView btnTambah = itemView.findViewById(R.id.btnTambah2);

                    // Set data produk sesuai index
                    imgProduk.setImageResource(gambarProduk[i]);
                    tvNamaProduk.setText(namaProduk[i]);
                    tvHargaProduk.setText(hargaProduk[i]);

                    setupQtyLogic(itemView);


                    // Tambahkan view ke container utama
                    KotakMenu2.addView(itemView);
                }
            });
            snackbutton.setOnClickListener(v -> {
                KotakMenu2.removeAllViews();
                for (int i = 0; i < namaSnack.length; i++) {
                    // Inflate layout item produk
                    View itemView = inflater.inflate(R.layout.activity_menu_kedua, KotakMenu2, false);

                    // Ambil elemen-elemen dari item
                    ImageView imgProduk = itemView.findViewById(R.id.imgProduk2);
                    TextView tvNamaProduk = itemView.findViewById(R.id.tvNamaProduk2);
                    TextView tvHargaProduk = itemView.findViewById(R.id.tvHargaProduk2);
                    ImageView btnTambah = itemView.findViewById(R.id.btnTambah2);

                    // Set data produk sesuai index
                    imgProduk.setImageResource(gambarSnack[i]);
                    tvNamaProduk.setText(namaSnack[i]);
                    tvHargaProduk.setText(hargaSnack[i]);

                    setupQtyLogic(itemView);


                    // Tambahkan view ke container utama
                    KotakMenu2.addView(itemView);
                }
            });
            drinkbutton.setOnClickListener(v -> {
                KotakMenu2.removeAllViews();
                for (int i = 0; i < namaMinuman.length; i++) {
                    // Inflate layout item produk
                    View itemView = inflater.inflate(R.layout.activity_menu_kedua, KotakMenu2, false);

                    // Ambil elemen-elemen dari item
                    ImageView imgProduk = itemView.findViewById(R.id.imgProduk2);
                    TextView tvNamaProduk = itemView.findViewById(R.id.tvNamaProduk2);
                    TextView tvHargaProduk = itemView.findViewById(R.id.tvHargaProduk2);
                    ImageView btnTambah = itemView.findViewById(R.id.btnTambah2);

                    // Set data produk sesuai index
                    imgProduk.setImageResource(gambarMinuman[i]);
                    tvNamaProduk.setText(namaMinuman[i]);
                    tvHargaProduk.setText(hargaMinuman[i]);

                    setupQtyLogic(itemView);


                    // Tambahkan view ke container utama
                    KotakMenu2.addView(itemView);
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


