package com.irvan.seblakpredator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.irvan.seblakpredator.apiclient.ApiClient;
import com.irvan.seblakpredator.apiclient.ApiService;
import com.irvan.seblakpredator.model.SecondMenuResponse;
import com.irvan.seblakpredator.model.Topping;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondTransaction extends AppCompatActivity {

    LinearLayout KotakMenu;
    Button btnSemua, btnFrozenFood, btnPelengkap, btnSayuran, btnJamur;

    private List<Topping> toppingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_transaction);

        // Ambil data dari intent yang dikirim dari FirstTransaction
        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String level = intent.getStringExtra("level");
        String kencur = intent.getStringExtra("kencur");
        String kuah = intent.getStringExtra("kuah");
        String telur = intent.getStringExtra("telur");
        String orderType = intent.getStringExtra("orderType");
        String waktuAmbil = intent.getStringExtra("waktuAmbil");

        // Menyimpan data ke SharedPreferences untuk digunakan kembali saat kembali ke FirstTransaction
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nama", nama);
        editor.putString("level", level);
        editor.putString("kencur", kencur);
        editor.putString("kuah", kuah);
        editor.putString("telur", telur);
        editor.putString("orderType", orderType);
        editor.putString("waktuAmbil", waktuAmbil);
        editor.apply();

        KotakMenu = findViewById(R.id.KotakMenu);
        btnSemua = findViewById(R.id.allButton);
        btnFrozenFood = findViewById(R.id.frozenfoodButton);
        btnPelengkap = findViewById(R.id.pelengkapButton);
        btnSayuran = findViewById(R.id.sayuranButton);
        btnJamur = findViewById(R.id.jamurButton);

        Button lanjut = findViewById(R.id.nextButton);
        Button kembali = findViewById(R.id.backButton);
        ImageView profil = findViewById(R.id.profilepage);

        lanjut.setOnClickListener(v -> {
            Intent intentNext = new Intent(SecondTransaction.this, TransaksiActivity.class);
            startActivity(intentNext);
        });

        kembali.setOnClickListener(v -> {
            // Kembali ke FirstTransaction dan membawa data yang sudah disimpan
            Intent intentBack = new Intent(SecondTransaction.this, FirstTransaction.class);
            startActivity(intentBack);
        });

        profil.setOnClickListener(v -> {
            Intent intentProfile = new Intent(SecondTransaction.this, ProfileActivity.class);
            startActivity(intentProfile);
        });

        loadToppingsFromApi();

        // Tampilkan semua data saat tombol semua ditekan
        btnSemua.setOnClickListener(v -> showToppings(toppingList));

        // Tampilkan data berdasarkan category_id saat tombol kategori ditekan
        btnFrozenFood.setOnClickListener(v -> showByCategory("cat_690b022fee4e9"));
        btnPelengkap.setOnClickListener(v -> showByCategory("cat_69153987dc69c"));
        btnSayuran.setOnClickListener(v -> showByCategory("cat_690b0353ec0df"));
        btnJamur.setOnClickListener(v -> showByCategory("cat_691539dc7b28f"));
    }

    private void loadToppingsFromApi() {
        ApiService api = ApiClient.getClient(this).create(ApiService.class);
        api.getToppings().enqueue(new Callback<SecondMenuResponse>() {
            @Override
            public void onResponse(Call<SecondMenuResponse> call, Response<SecondMenuResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    toppingList = response.body().getData();
                    // Otomatis tampilkan semua data saat halaman dibuka
                    showToppings(toppingList);
                } else {
                    Toast.makeText(SecondTransaction.this, "Response gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SecondMenuResponse> call, Throwable t) {
                Toast.makeText(SecondTransaction.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showByCategory(String categoryId) {
        List<Topping> filtered = new ArrayList<>();
        for (Topping t : toppingList) {
            if (t.getCategoryId().equals(categoryId)) {
                filtered.add(t);
            }
        }
        showToppings(filtered);
    }

    private void showToppings(List<Topping> toppings) {
        KotakMenu.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        for (Topping t : toppings) {
            View itemView = inflater.inflate(R.layout.activity_menu, KotakMenu, false);
            ImageView img = itemView.findViewById(R.id.imgProduk2);
            TextView nama = itemView.findViewById(R.id.tvNamaProduk2);
            TextView harga = itemView.findViewById(R.id.tvHargaProduk2);

            nama.setText(t.getName());
            harga.setText("Rp " + t.getPrice());

            Glide.with(this)
                    .load(t.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(img);

            setupQtyLogic(itemView);
            KotakMenu.addView(itemView);
        }
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
