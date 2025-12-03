package com.irvan.seblakpredator;

import android.content.Intent;
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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.irvan.seblakpredator.apiclient.ApiClient;
import com.irvan.seblakpredator.apiclient.ApiService;
import com.irvan.seblakpredator.model.SecondMenuResponse;
import com.irvan.seblakpredator.model.SelectedMenu;
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
    private ArrayList<SelectedMenu> existingMenus = new ArrayList<>();

    private static final int REQUEST_CART = 100;

    // Harga menu utama dari server
    private int hargaLevel, hargaKuah, hargaTelur, hargaKencur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_transaction);

        // Ambil existingMenus dari intent jika ada
        existingMenus = getIntent().getSerializableExtra("existingMenus") != null ?
                (ArrayList<SelectedMenu>) getIntent().getSerializableExtra("existingMenus") :
                new ArrayList<>();

        // Transparent status bar
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );

        // Ambil topping lama jika ada
        ArrayList<SelectedTopping> oldToppings = (ArrayList<SelectedTopping>) getIntent()
                .getSerializableExtra("existingToppings");
        if (oldToppings != null && !oldToppings.isEmpty()) {
            selectedToppings.addAll(oldToppings);
        }
        String userId = getIntent().getStringExtra("user_id");
        if (userId == null) {
            Toast.makeText(this, "User ID tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        KotakMenu = findViewById(R.id.KotakMenu);
        btnSemua = findViewById(R.id.allButton);
        btnFrozenFood = findViewById(R.id.frozenfoodButton);
        btnPelengkap = findViewById(R.id.pelengkapButton);
        btnSayuran = findViewById(R.id.sayuranButton);
        btnJamur = findViewById(R.id.jamurButton);

        Button lanjut = findViewById(R.id.nextButton);
        Button kembali = findViewById(R.id.backButton);
        ImageView profil = findViewById(R.id.profilepage);

        // Ambil data menu utama dari FirstTransaction
        String nama = getIntent().getStringExtra("nama");
        String level = getIntent().getStringExtra("level");
        String kuah = getIntent().getStringExtra("kuah");
        String telur = getIntent().getStringExtra("telur");
        String kencur = getIntent().getStringExtra("kencur");

        // Ambil harga dari server (dikirim dari FirstTransaction)
        hargaLevel = getIntent().getIntExtra("hargaLevel", 0);
        hargaKuah = getIntent().getIntExtra("hargaKuah", 0);
        hargaTelur = getIntent().getIntExtra("hargaTelur", 0);
        hargaKencur = getIntent().getIntExtra("hargaKencur", 0);

        // Tombol lanjut ke TransaksiActivity
        lanjut.setOnClickListener(v -> {
            lanjut.setEnabled(false);
            if (selectedToppings == null) selectedToppings = new ArrayList<>();

            // Buat SelectedMenu baru untuk menu yang dipilih sekarang
            SelectedMenu currentMenu = new SelectedMenu(
                    nama != null ? nama : "",
                    level != null ? level : "",
                    kuah != null ? kuah : "",
                    telur != null ? telur : "",
                    kencur != null ? kencur : "",
                    hargaLevel,
                    hargaKuah,
                    hargaTelur,
                    hargaKencur,
                    new ArrayList<>(selectedToppings) // salin topping saat ini
            );

            String address = getIntent().getStringExtra("address");

            // Tambahkan menu baru ke existingMenus
            existingMenus.add(currentMenu);
            String orderType = getIntent().getStringExtra("orderType");

            // Kirim existingMenus ke TransaksiActivity
            Intent intentNext = new Intent(SecondTransaction.this, TransaksiActivity.class);
            intentNext.putExtra("existingMenus", existingMenus);
            intentNext.putExtra("orderType", orderType);  // <- tambahan
            intentNext.putExtra("user_id", userId);
            intentNext.putExtra("address", address);
            startActivity(intentNext);
        });

        // Tombol kembali ke FirstTransaction
        kembali.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("existingMenus", existingMenus);
            setResult(RESULT_OK, resultIntent);
            finish();

        });

        profil.setOnClickListener(v -> {
            // Buka profil (jika ada)
        });

        ConstraintLayout btnKeranjang = findViewById(R.id.btnKeranjang);
        btnKeranjang.setOnClickListener(v -> {
            if (selectedToppings.isEmpty()) {
                Toast.makeText(SecondTransaction.this, "Keranjang masih kosong!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intentKeranjang = new Intent(SecondTransaction.this, KeranjangActivity.class);
            intentKeranjang.putExtra("selectedToppings", (ArrayList<SelectedTopping>) selectedToppings);
            startActivityForResult(intentKeranjang, REQUEST_CART);
        });

        // Load topping dari API
        loadToppingsFromApi();

        // Filter tombol kategori
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
                    Toast.makeText(SecondTransaction.this, "Gagal mengambil topping", Toast.LENGTH_SHORT).show();
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

        int initialQty = 0;
        for (SelectedTopping s : selectedToppings) {
            if (s.getId().equals(topping.getId())) {
                initialQty = s.getQuantity();
                break;
            }
        }

        if (initialQty > 0) {
            btnTambahAwal.setVisibility(View.GONE);
            layoutQty.setVisibility(View.VISIBLE);
            txtQty.setText(String.valueOf(initialQty));
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CART && resultCode == RESULT_OK) {
            ArrayList<SelectedTopping> updatedToppings =
                    (ArrayList<SelectedTopping>) data.getSerializableExtra("updatedToppings");
            selectedToppings.clear();
            if (updatedToppings != null && !updatedToppings.isEmpty()) {
                selectedToppings.addAll(updatedToppings);
            }
            showToppings(toppingList);
        }
    }

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
