package com.irvan.seblakpredator;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.irvan.seblakpredator.adapter.CartAdapter;
import com.irvan.seblakpredator.model.CartItem;
import com.irvan.seblakpredator.model.SelectedMenu;
import com.irvan.seblakpredator.model.Topping;

import java.util.ArrayList;
import java.util.List;

public class KeranjangActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private CheckBox checkAll;
    private TextView btnDelete;
    private Button btnCheckout, backBtn;
    private List<CartItem> itemList;
    private ArrayList<SecondTransaction.SelectedTopping> selectedToppings;
    private int hargaLevel, hargaKuah, hargaTelur, hargaKencur;
    private List<Topping> toppingList = new ArrayList<>();
    private ArrayList<SelectedMenu> existingMenus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        // Transparent status bar
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );

        // Init views
        recyclerView = findViewById(R.id.recyclerKeranjang);
        checkAll = findViewById(R.id.checkAll);
        btnDelete = findViewById(R.id.btnHapus);
        btnCheckout = findViewById(R.id.btnCheckout);
        backBtn = findViewById(R.id.backButton);

        // Ambil data selectedToppings dari intent
        selectedToppings = (ArrayList<SecondTransaction.SelectedTopping>) getIntent()
                .getSerializableExtra("selectedToppings");
        String userId = getIntent().getStringExtra("user_id");
        if (userId == null) {
            Toast.makeText(this, "User ID tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Ambil topping lama jika ada
        ArrayList<SecondTransaction.SelectedTopping> oldToppings = (ArrayList<SecondTransaction.SelectedTopping>) getIntent()
                .getSerializableExtra("existingToppings");
        if (oldToppings != null && !oldToppings.isEmpty()) {
            selectedToppings.addAll(oldToppings);
        }
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

        // Konversi ke CartItem (gabung semua topping menjadi 1 item)
        itemList = new ArrayList<>();
        if (selectedToppings != null && !selectedToppings.isEmpty()) {
            StringBuilder products = new StringBuilder();
            int totalPrice = 0;
            for (SecondTransaction.SelectedTopping s : selectedToppings) {
                if (products.length() > 0) products.append(", ");
                products.append(s.getName());
                totalPrice += s.getPrice() * s.getQuantity();
            }
            itemList.add(new CartItem(
                    products.toString(),
                    "Rp " + totalPrice,
                    "", // tidak tampil qty di cart item gabungan
                    false,
                    new ArrayList<>(selectedToppings)
            ));
        }

        // Setup adapter dan RecyclerView
        adapter = new CartAdapter(itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Listener tombol back
        backBtn.setOnClickListener(v -> {
            String address = getIntent().getStringExtra("address");
            String orderType = getIntent().getStringExtra("orderType");
            Intent resultIntent = new Intent();
            resultIntent.putExtra("existingMenus", existingMenus);
            resultIntent.putExtra("orderType", orderType);  // <- tambahan
            resultIntent.putExtra("user_id", userId);
            resultIntent.putExtra("address", address);
            setResult(RESULT_OK, resultIntent);
            finish();
                });

        // Listener check all
        checkAll.setOnCheckedChangeListener((buttonView, isChecked) -> adapter.selectAll(isChecked));

        // Listener tombol delete
        btnDelete.setOnClickListener(v -> {
            if (adapter.getItemCount() == 0) {
                Toast.makeText(this, "Tidak ada item untuk dihapus", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah kamu yakin ingin menghapus item yang dipilih?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        boolean hadToppingItem = !itemList.isEmpty(); // Asumsi item topping gabungan ada
                        adapter.removeSelected();

                        // Jika item topping dihapus, kosongkan selectedToppings juga supaya sync
                        if (hadToppingItem) {
                            selectedToppings.clear();
                        }

                        Toast.makeText(this, "Item berhasil dihapus", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });

        btnCheckout.setOnClickListener(v -> {
            if (selectedToppings.isEmpty()) {
                Toast.makeText(this, "Keranjang kosong!", Toast.LENGTH_SHORT).show();
                return;
            }
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
            Intent intentNext = new Intent(KeranjangActivity.this, TransaksiActivity.class);
            intentNext.putExtra("selectedToppings", (ArrayList<SecondTransaction.SelectedTopping>) selectedToppings);
            intentNext.putExtra("existingMenus", existingMenus);
            intentNext.putExtra("orderType", orderType);  // <- tambahan
            intentNext.putExtra("user_id", userId);
            intentNext.putExtra("address", address);
            startActivity(intentNext);
        });

    }

    @Override
    public void finish() {
        // Kirim kembali selectedToppings yang sudah diupdate ke SecondTransaction
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedToppings", selectedToppings);
        setResult(RESULT_OK, resultIntent);
        super.finish();
    }
}
