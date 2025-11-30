package com.irvan.seblakpredator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondTransaction extends AppCompatActivity {

    LinearLayout KotakMenu;
    Button btnSemua, btnFrozenFood, btnPelengkap, btnSayuran, btnJamur;
    private List<Topping> toppingList = new ArrayList<>();
    private List<SelectedTopping> selectedToppings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_transaction);

        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );




        // Ambil data dari intent
        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String level = intent.getStringExtra("level");
        String kencur = intent.getStringExtra("kencur");
        String kuah = intent.getStringExtra("kuah");
        String telur = intent.getStringExtra("telur");
        String orderType = intent.getStringExtra("orderType");
        String waktuAmbil = intent.getStringExtra("waktuAmbil");

        // Simpan data ke SharedPreferences
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

        // Tombol lanjut ke TransaksiActivity
        lanjut.setOnClickListener(v -> {
            if(selectedToppings.isEmpty()){
                Toast.makeText(SecondTransaction.this, "Silakan pilih minimal 1 produk!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intentNext = new Intent(SecondTransaction.this, TransaksiActivity.class);
            intentNext.putExtra("selectedToppings", (Serializable) selectedToppings);
            startActivity(intentNext);
        });

        // Tombol kembali ke FirstTransaction
        kembali.setOnClickListener(v -> {
            finish(); // tidak reset data di FirstTransaction
        });

        profil.setOnClickListener(v -> {
            Intent intentProfile = new Intent(SecondTransaction.this, ProfileActivity.class);
            startActivity(intentProfile);
        });

        // Load topping dari API
        loadToppingsFromApi();

        btnSemua.setOnClickListener(v -> showToppings(toppingList));
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
            if (t.getCategoryId().equals(categoryId)) filtered.add(t);
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

            setupQtyLogic(itemView, t);
            KotakMenu.addView(itemView);
        }
    }

    private void setupQtyLogic(View itemView, Topping topping) {
        ImageView btnTambahAwal = itemView.findViewById(R.id.btnTambah2);
        LinearLayout layoutQty = itemView.findViewById(R.id.layoutQty);
        ImageView btnTambah = itemView.findViewById(R.id.btnTambah);
        ImageView btnKurang = itemView.findViewById(R.id.btnKurang);
        TextView txtQty = itemView.findViewById(R.id.txtQty);

        btnTambahAwal.setOnClickListener(v -> {
            btnTambahAwal.setVisibility(View.GONE);
            layoutQty.setVisibility(View.VISIBLE);
            txtQty.setText("1");
            selectedToppings.add(new SelectedTopping(
                    topping.getId(),
                    topping.getName(),
                    1,
                    topping.getPrice()
            ));
        });

        btnTambah.setOnClickListener(v -> {
            int jumlah = Integer.parseInt(txtQty.getText().toString());
            jumlah++;
            txtQty.setText(String.valueOf(jumlah));
            for (SelectedTopping s : selectedToppings) {
                if (s.getId().equals(topping.getId())) {
                    s.setQuantity(jumlah);
                    break;
                }
            }
        });

        btnKurang.setOnClickListener(v -> {
            int jumlah = Integer.parseInt(txtQty.getText().toString());
            if (jumlah > 1) {
                jumlah--;
                txtQty.setText(String.valueOf(jumlah));
                for (SelectedTopping s : selectedToppings) {
                    if (s.getId().equals(topping.getId())) {
                        s.setQuantity(jumlah);
                        break;
                    }
                }
            } else {
                layoutQty.setVisibility(View.GONE);
                btnTambahAwal.setVisibility(View.VISIBLE);
                selectedToppings.removeIf(s -> s.getId().equals(topping.getId()));
            }
        });
    }

    // Model untuk dikirim ke TransaksiActivity
    public static class SelectedTopping implements Serializable {
        private String id;
        private String name;
        private int quantity;
        private int price;

        public SelectedTopping(String id, String name, int quantity, int price) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public int getQuantity() { return quantity; }
        public int getPrice() { return price; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}
