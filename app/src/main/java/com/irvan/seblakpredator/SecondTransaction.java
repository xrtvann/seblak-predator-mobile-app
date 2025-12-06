package com.irvan.seblakpredator;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
    TextInputEditText searchTextInput;
    TextInputLayout searchTextColumn;
    ConstraintLayout noResultsMessage;

    private List<Topping> toppingList = new ArrayList<>();

    private ArrayList<SelectedTopping> previousToppings = new ArrayList<>(); // topping dari menu sebelumnya
    private ArrayList<SelectedTopping> selectedToppings = new ArrayList<>(); // topping untuk menu ini

    private ArrayList<SelectedMenu> existingMenus = new ArrayList<>();

    private static final int REQUEST_CART = 100;

    private int hargaLevel, hargaKuah, hargaTelur, hargaKencur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_transaction);

        existingMenus = getIntent().getSerializableExtra("existingMenus") != null ?
                (ArrayList<SelectedMenu>) getIntent().getSerializableExtra("existingMenus") :
                new ArrayList<>();

        // Status bar transparan
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );

        // Ambil topping lama
        ArrayList<SelectedTopping> oldToppings =
                (ArrayList<SelectedTopping>) getIntent().getSerializableExtra("existingToppings");

        if (oldToppings != null) {
            previousToppings.addAll(oldToppings); // simpan untuk transaksi
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
        searchTextInput = findViewById(R.id.searchTextInput);
        searchTextColumn = findViewById(R.id.searchTextColumn);
        noResultsMessage = findViewById(R.id.noResultsMessage);

        Button lanjut = findViewById(R.id.nextButton);
        Button kembali = findViewById(R.id.backButton);

        String nama = getIntent().getStringExtra("nama");
        String level = getIntent().getStringExtra("level");
        String kuah = getIntent().getStringExtra("kuah");
        String telur = getIntent().getStringExtra("telur");
        String kencur = getIntent().getStringExtra("kencur");

        hargaLevel = getIntent().getIntExtra("hargaLevel", 0);
        hargaKuah = getIntent().getIntExtra("hargaKuah", 0);
        hargaTelur = getIntent().getIntExtra("hargaTelur", 0);
        hargaKencur = getIntent().getIntExtra("hargaKencur", 0);

        // Lanjut → kirim menu + topping baru + topping lama
        lanjut.setOnClickListener(v -> {
            lanjut.setEnabled(false);

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
                    new ArrayList<>(selectedToppings) // hanya topping baru
            );

            existingMenus.add(currentMenu);

            String orderType = getIntent().getStringExtra("order_type");
            String address = getIntent().getStringExtra("address");

            ArrayList<SelectedTopping> allToppings = new ArrayList<>();
            allToppings.addAll(previousToppings);
            allToppings.addAll(selectedToppings);

            Intent intentNext = new Intent();
            intentNext.putExtra("existingMenus", existingMenus);
            intentNext.putExtra("selectedToppings", allToppings);
            intentNext.putExtra("order_type", orderType);
            intentNext.putExtra("user_id", userId);
            intentNext.putExtra("address", address);
            setResult(RESULT_OK, intentNext);
            finish();
        });

        // Kembali → kirim data tapi tidak tampilkan topping lama
        kembali.setOnClickListener(v -> {
            String orderType = getIntent().getStringExtra("orderType");
            String address = getIntent().getStringExtra("address");

            Intent result = new Intent();
            result.putExtra("existingMenus", existingMenus);
            result.putExtra("existingToppings", previousToppings);
            result.putExtra("orderType", orderType);
            result.putExtra("user_id", userId);
            result.putExtra("address", address);

            setResult(RESULT_OK, result);
            finish();
        });

        ConstraintLayout btnKeranjang = findViewById(R.id.btnKeranjang);
        btnKeranjang.setOnClickListener(v -> {
            if (selectedToppings.isEmpty() && previousToppings.isEmpty()) {
                Toast.makeText(this, "Keranjang masih kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intentKeranjang = new Intent(this, KeranjangActivity.class);
            String orderType = getIntent().getStringExtra("orderType");
            String address = getIntent().getStringExtra("address");

            ArrayList<SelectedTopping> allToppings = new ArrayList<>();
            allToppings.addAll(previousToppings);
            allToppings.addAll(selectedToppings);

            intentKeranjang.putExtra("selectedToppings", allToppings);
            intentKeranjang.putExtra("existingMenus", existingMenus);
            intentKeranjang.putExtra("orderType", orderType);
            intentKeranjang.putExtra("user_id", userId);
            intentKeranjang.putExtra("address", address);

            startActivityForResult(intentKeranjang, REQUEST_CART);
        });

        // Load topping API
        loadToppingsFromApi();
        changeButtonColor(btnSemua);
        // Set listener on the search box to filter toppings
        searchTextInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String query = charSequence.toString().trim();
                if (!query.isEmpty()) {
                    List<Topping> filteredList = new ArrayList<>();
                    for (Topping topping : toppingList) {
                        if (topping.getName().toLowerCase().contains(query)) {
                            filteredList.add(topping);
                        }
                    }
                    if (filteredList.isEmpty()) {
                        noResultsMessage.setVisibility(View.VISIBLE);  // Show the "no results" message
                    } else {
                        noResultsMessage.setVisibility(View.GONE);  // Hide the "no results" message
                    }
                    showToppings(filteredList);
                    // Keep "Semua" button white and text orange when searching
                    changeButtonColor(btnSemua);
                } else {
                    // If the search box is empty, show all toppings
                    showToppings(toppingList);
                    noResultsMessage.setVisibility(View.GONE); // Hide the message when search is empty
                    changeButtonColor(btnSemua); // Keep "Semua" button as it is
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });
        searchTextInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Menyembunyikan keyboard ketika tombol DONE ditekan
                hideKeyboard();
                return true; // Menandakan bahwa aksi sudah ditangani
            }
            return false;
        });


        // Filter kategori
        btnSemua.setOnClickListener(v -> {
            showToppings(toppingList); // Menampilkan semua topping
            changeButtonColor(btnSemua); // Mengubah warna tombol btnSemua
        });

        btnFrozenFood.setOnClickListener(v -> {
            showByCategory("cat_690b022fee4e9"); // Filter berdasarkan kategori Frozen Food
            changeButtonColor(btnFrozenFood); // Mengubah warna tombol btnFrozenFood
        });

        btnPelengkap.setOnClickListener(v -> {
            showByCategory("cat_69153987dc69c"); // Filter berdasarkan kategori Pelengkap
            changeButtonColor(btnPelengkap); // Mengubah warna tombol btnPelengkap
        });

        btnSayuran.setOnClickListener(v -> {
            showByCategory("cat_690b0353ec0df"); // Filter berdasarkan kategori Sayuran
            changeButtonColor(btnSayuran); // Mengubah warna tombol btnSayuran
        });

        btnJamur.setOnClickListener(v -> {
            showByCategory("cat_691539dc7b28f"); // Filter berdasarkan kategori Jamur
            changeButtonColor(btnJamur); // Mengubah warna tombol btnJamur
        });
    }
    private void changeButtonColor(Button selectedButton) {
        // Mengembalikan semua tombol ke warna default
        resetButtonColors();

        // Mengubah warna tombol yang dipilih
        selectedButton.setBackgroundColor(Color.WHITE); // Ubah background menjadi putih
        selectedButton.setTextColor(Color.parseColor("#E64A19")); // Ubah teks menjadi orange
    }

    private void resetButtonColors() {
        // Reset semua tombol ke warna default
        btnSemua.setBackgroundColor(Color.parseColor("#E64A19")); // Warna default orange
        btnSemua.setTextColor(Color.WHITE); // Warna teks default putih

        btnFrozenFood.setBackgroundColor(Color.parseColor("#E64A19"));
        btnFrozenFood.setTextColor(Color.WHITE);

        btnPelengkap.setBackgroundColor(Color.parseColor("#E64A19"));
        btnPelengkap.setTextColor(Color.WHITE);

        btnSayuran.setBackgroundColor(Color.parseColor("#E64A19"));
        btnSayuran.setTextColor(Color.WHITE);

        btnJamur.setBackgroundColor(Color.parseColor("#E64A19"));
        btnJamur.setTextColor(Color.WHITE);
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

    // =========================
    // LOGIC QTY → handle topping lama & baru
    // =========================
    private void setupQtyLogic(View itemView, Topping topping) {
        ImageView btnTambahAwal = itemView.findViewById(R.id.btnTambah2);
        LinearLayout layoutQty = itemView.findViewById(R.id.layoutQty);
        ImageView btnTambah = itemView.findViewById(R.id.btnTambah);
        ImageView btnKurang = itemView.findViewById(R.id.btnKurang);
        TextView txtQty = itemView.findViewById(R.id.txtQty);

        // Cek qty dari topping lama atau baru
        int initialQty = 0;
        boolean isPrevious = false;

        for (SelectedTopping s : selectedToppings) {
            if (s.getId().equals(topping.getId())) {
                initialQty = s.getQuantity();
                break;
            }
        }
        if (initialQty == 0) {
            for (SelectedTopping s : previousToppings) {
                if (s.getId().equals(topping.getId())) {
                    initialQty = s.getQuantity();
                    isPrevious = true;
                    break;
                }
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
            txtQty.setTextColor(Color.BLACK);

            boolean foundInPrevious = false;
            for (SelectedTopping s : previousToppings) {
                if (s.getId().equals(topping.getId())) {
                    s.setQuantity(1);
                    foundInPrevious = true;
                    break;
                }
            }
            if (!foundInPrevious) {
                selectedToppings.add(new SelectedTopping(
                        topping.getId(),
                        topping.getName(),
                        1,
                        topping.getPrice()
                ));
            }
        });

        btnTambah.setOnClickListener(v -> {
            int jumlah = Integer.parseInt(txtQty.getText().toString());
            jumlah++;
            txtQty.setText(String.valueOf(jumlah));

            boolean updated = false;
            for (SelectedTopping s : selectedToppings) {
                if (s.getId().equals(topping.getId())) {
                    s.setQuantity(jumlah);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                for (SelectedTopping s : previousToppings) {
                    if (s.getId().equals(topping.getId())) {
                        s.setQuantity(jumlah);
                        break;
                    }
                }
            }
        });

        btnKurang.setOnClickListener(v -> {
            int jumlah = Integer.parseInt(txtQty.getText().toString());

            if (jumlah > 1) {
                jumlah--;
                txtQty.setText(String.valueOf(jumlah));

                boolean updated = false;
                for (SelectedTopping s : selectedToppings) {
                    if (s.getId().equals(topping.getId())) {
                        s.setQuantity(jumlah);
                        updated = true;
                        break;
                    }
                }
                if (!updated) {
                    for (SelectedTopping s : previousToppings) {
                        if (s.getId().equals(topping.getId())) {
                            s.setQuantity(jumlah);
                            break;
                        }
                    }
                }
            } else {
                layoutQty.setVisibility(View.GONE);
                btnTambahAwal.setVisibility(View.VISIBLE);

                selectedToppings.removeIf(s -> s.getId().equals(topping.getId()));
                previousToppings.removeIf(s -> s.getId().equals(topping.getId()));
            }
        });
    }

    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        String query = charSequence.toString().toLowerCase();
        Log.d("Search", "Query: " + query);  // Log untuk melihat query yang dimasukkan
        if (!query.isEmpty()) {
            List<Topping> filteredList = new ArrayList<>();
            for (Topping topping : toppingList) {
                if (topping.getName().toLowerCase().contains(query)) {
                    filteredList.add(topping);
                }
            }
            Log.d("Search", "Filtered list size: " + filteredList.size());  // Log untuk ukuran hasil pencarian

            if (filteredList.isEmpty()) {
                noResultsMessage.setVisibility(View.VISIBLE);  // Show the "no results" message
                KotakMenu.setVisibility(View.GONE);  // Menyembunyikan daftar topping
            } else {
                noResultsMessage.setVisibility(View.GONE);  // Hide the "no results" message
                KotakMenu.setVisibility(View.VISIBLE);  // Menampilkan daftar topping yang ada
                showToppings(filteredList);  // Menampilkan topping yang sudah difilter
            }
            changeButtonColor(btnSemua);  // Memastikan warna tombol "Semua" tetap sesuai
        } else {
            // Jika pencarian kosong, tampilkan semua topping
            showToppings(toppingList);
            noResultsMessage.setVisibility(View.GONE);  // Sembunyikan pesan tidak ada hasil
            KotakMenu.setVisibility(View.VISIBLE);  // Tampilkan semua topping
            changeButtonColor(btnSemua);  // Memastikan warna tombol "Semua" tetap sesuai
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof TextInputEditText) {
                int[] scrcoords = new int[2];
                v.getLocationOnScreen(scrcoords);

                float x = ev.getRawX() + v.getLeft() - scrcoords[0];
                float y = ev.getRawY() + v.getTop() - scrcoords[1];

                // Jika klik di luar area EditText → tutup keyboard
                if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                    hideKeyboard();
                    v.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CART && resultCode == RESULT_OK) {
            ArrayList<SelectedTopping> updatedToppings =
                    (ArrayList<SelectedTopping>) data.getSerializableExtra("updatedToppings");

            selectedToppings.clear();

            if (updatedToppings != null) {
                selectedToppings.addAll(updatedToppings);
            }

            showToppings(toppingList);
        }
    }

    // ========================
    // MODEL TOPPING
    // ========================
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
