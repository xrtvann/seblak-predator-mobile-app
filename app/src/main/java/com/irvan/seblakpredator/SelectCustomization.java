package com.irvan.seblakpredator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.irvan.seblakpredator.apiclient.ApiClient;
import com.irvan.seblakpredator.apiclient.ApiService;
import com.irvan.seblakpredator.model.CustomizationOption;
import com.irvan.seblakpredator.model.CustomizationResponse;
import com.irvan.seblakpredator.model.SelectedMenu;
import com.irvan.seblakpredator.model.SpiceLevel;
import com.irvan.seblakpredator.model.SpiceLevelResponse;
import com.irvan.seblakpredator.SecondTransaction.SelectedTopping;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectCustomization extends AppCompatActivity {

    private static final int REQUEST_SECOND_TRANSACTION = 200;

    Spinner spinnerLevel, spinnerKencur;
    RadioGroup tipeKuah, tipeTelur;
    Button btnLanjut, backButton, addToppingButton;
    ConstraintLayout bartopping;
    LinearLayout kotakMenu;

    private List<CustomizationOption> kuahList = new ArrayList<>();
    private List<CustomizationOption> telurList = new ArrayList<>();
    private List<CustomizationOption> kencurList = new ArrayList<>();
    private List<SpiceLevel> levelPedasList = new ArrayList<>();

    private ArrayList<SelectedTopping> selectedToppings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcustomization);

        // Setelan status bar transparan
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );

        // Inisialisasi UI
        spinnerLevel = findViewById(R.id.pilihanLevel);
        spinnerKencur = findViewById(R.id.pilihanKencur);
        tipeKuah = findViewById(R.id.tipeKuah);
        tipeTelur = findViewById(R.id.tipeTelur);
        btnLanjut = findViewById(R.id.btnLanjut);
        backButton = findViewById(R.id.backButton);
        addToppingButton = findViewById(R.id.btnAddTopping);
        kotakMenu = findViewById(R.id.KotakMenu);
        bartopping = findViewById(R.id.statusTopping);

        // Ambil data dari intent
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String nama = getIntent().getStringExtra("name");
        String orderType = getIntent().getStringExtra("order_type");
        String address = getIntent().getStringExtra("address");
        String userId = getIntent().getStringExtra("user_id");

        if (userId == null) {
            Toast.makeText(this, "User ID tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ArrayList<SelectedMenu> existingMenus = getIntent().getSerializableExtra("existingMenus") != null ?
                (ArrayList<SelectedMenu>) getIntent().getSerializableExtra("existingMenus") :
                new ArrayList<>();

        backButton.setOnClickListener(v -> finish());

        // Memuat pilihan kustomisasi
        loadCustomizationOptions();

        // Tombol tambah topping
        addToppingButton.setOnClickListener(v -> openSecondTransaction(nama, orderType, address, userId, existingMenus));

        // Tombol lanjut
        btnLanjut.setOnClickListener(v -> {
            String level = spinnerLevel.getSelectedItem() != null ? spinnerLevel.getSelectedItem().toString() : "";
            String kencur = spinnerKencur.getSelectedItem() != null ? spinnerKencur.getSelectedItem().toString() : "";

            int idKuah = tipeKuah.getCheckedRadioButtonId();
            int idTelur = tipeTelur.getCheckedRadioButtonId();

            if (idKuah == -1 || idTelur == -1 || level.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton pilihKuah = findViewById(idKuah);
            RadioButton pilihTelur = findViewById(idTelur);

            // Mendapatkan harga dari pilihan
            int hargaLevel = 0;
            for (SpiceLevel sl : levelPedasList) {
                if (sl.getName().equals(level)) { hargaLevel = (int) sl.getPrice(); break; }
            }

            int hargaKuah = 0;
            for (CustomizationOption kuah : kuahList) {
                if (kuah.getName().equals(pilihKuah.getText().toString())) { hargaKuah = (int) kuah.getPrice(); break; }
            }

            int hargaTelur = 0;
            for (CustomizationOption t : telurList) {
                if (t.getName().equals(pilihTelur.getText().toString())) { hargaTelur = (int) t.getPrice(); break; }
            }

            int hargaKencur = 0;
            for (CustomizationOption k : kencurList) {
                if (k.getName().equals(kencur)) { hargaKencur = (int) k.getPrice(); break; }
            }

            // Membuat objek SelectedMenu
            SelectedMenu currentMenu = new SelectedMenu(
                    nama,
                    level,
                    pilihKuah.getText().toString(),
                    pilihTelur.getText().toString(),
                    kencur,
                    hargaLevel,
                    hargaKuah,
                    hargaTelur,
                    hargaKencur,
                    new ArrayList<>(selectedToppings)
            );

            // Menambahkan menu yang sudah dipilih ke dalam existingMenus
            existingMenus.add(currentMenu);

            // Membuat Intent untuk mengarahkan ke TransaksiActivity
            Intent intent = new Intent(this, TransaksiActivity.class);
            intent.putExtra("user_id", userId);
            intent.putExtra("existingMenus", existingMenus);
            intent.putExtra("orderType", orderType);
            intent.putExtra("address", address);

            startActivity(intent);
        });
    }

    private void openSecondTransaction(String nama, String orderType, String address, String userId, ArrayList<SelectedMenu> existingMenus) {
        String level = spinnerLevel.getSelectedItem() != null ? spinnerLevel.getSelectedItem().toString() : "";
        String kencur = spinnerKencur.getSelectedItem() != null ? spinnerKencur.getSelectedItem().toString() : "";

        int idKuah = tipeKuah.getCheckedRadioButtonId();
        int idTelur = tipeTelur.getCheckedRadioButtonId();

        if (idKuah == -1 || idTelur == -1 || level.isEmpty()) {
            Toast.makeText(this, "Harap lengkapi semua data!", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton pilihKuah = findViewById(idKuah);
        RadioButton pilihTelur = findViewById(idTelur);

        int hargaLevel = 0;
        for (SpiceLevel sl : levelPedasList) {
            if (sl.getName().equals(level)) { hargaLevel = (int) sl.getPrice(); break; }
        }

        int hargaKuah = 0;
        for (CustomizationOption kuah : kuahList) {
            if (kuah.getName().equals(pilihKuah.getText().toString())) { hargaKuah = (int) kuah.getPrice(); break; }
        }

        int hargaTelur = 0;
        for (CustomizationOption t : telurList) {
            if (t.getName().equals(pilihTelur.getText().toString())) { hargaTelur = (int) t.getPrice(); break; }
        }

        int hargaKencur = 0;
        for (CustomizationOption k : kencurList) {
            if (k.getName().equals(kencur)) { hargaKencur = (int) k.getPrice(); break; }
        }

        SelectedMenu currentMenu = new SelectedMenu(
                nama,
                level,
                pilihKuah.getText().toString(),
                pilihTelur.getText().toString(),
                kencur,
                hargaLevel,
                hargaKuah,
                hargaTelur,
                hargaKencur,
                new ArrayList<>(selectedToppings)
        );

        Intent intent = new Intent(this, SecondTransaction.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("nama", nama);
        intent.putExtra("orderType", orderType);
        intent.putExtra("address", address);
        intent.putExtra("level", level);
        intent.putExtra("kuah", pilihKuah.getText().toString());
        intent.putExtra("telur", pilihTelur.getText().toString());
        intent.putExtra("kencur", kencur);
        intent.putExtra("hargaLevel", hargaLevel);
        intent.putExtra("hargaKuah", hargaKuah);
        intent.putExtra("hargaTelur", hargaTelur);
        intent.putExtra("hargaKencur", hargaKencur);
        intent.putExtra("existingMenus", existingMenus);
        intent.putExtra("existingToppings", selectedToppings);

        startActivityForResult(intent, REQUEST_SECOND_TRANSACTION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SECOND_TRANSACTION && resultCode == RESULT_OK && data != null) {
            ArrayList<SelectedTopping> updatedToppings =
                    (ArrayList<SelectedTopping>) data.getSerializableExtra("selectedToppings");
            if (updatedToppings != null) {
                selectedToppings.clear();
                selectedToppings.addAll(updatedToppings);
            }

            displaySelectedToppings(selectedToppings);

            // Update existingMenus
            ArrayList<SelectedMenu> updatedMenus = (ArrayList<SelectedMenu>) data.getSerializableExtra("existingMenus");
            if (updatedMenus != null) {
                // bisa diteruskan ke TransaksiActivity nanti
            }
        }
    }
    private void displaySelectedToppings(List<SelectedTopping> toppings) {
        kotakMenu.removeAllViews(); // Hapus tampilan sebelumnya
        LayoutInflater inflater = LayoutInflater.from(this);

        // Jika tidak ada topping yang dipilih, tampilkan bar topping
        if (toppings.isEmpty()) {
            bartopping.setVisibility(View.VISIBLE); // Tampilkan bar topping
        } else {
            bartopping.setVisibility(View.GONE); // Sembunyikan bar topping
        }

        for (SelectedTopping t : toppings) {
            View itemView = inflater.inflate(R.layout.activity_menu, kotakMenu, false);
            ImageView img = itemView.findViewById(R.id.imgProduk2);
            TextView nama = itemView.findViewById(R.id.tvNamaProduk2);
            TextView txtQty = itemView.findViewById(R.id.txtQty);
            LinearLayout layoutQty = itemView.findViewById(R.id.layoutQty);
            ImageView btnTambahAwal = itemView.findViewById(R.id.btnTambah2);
            ImageView btnTambah = itemView.findViewById(R.id.btnTambah);
            ImageView btnKurang = itemView.findViewById(R.id.btnKurang);

            nama.setText(t.getName());

            // Menampilkan quantity jika ada
            if (t.getQuantity() > 0) {
                btnTambahAwal.setVisibility(View.GONE);
                layoutQty.setVisibility(View.VISIBLE);
                txtQty.setText(String.valueOf(t.getQuantity()));
            } else {
                btnTambahAwal.setVisibility(View.VISIBLE);
                layoutQty.setVisibility(View.GONE);
            }

            // Ketika tombol tambah diklik
            btnTambahAwal.setOnClickListener(v -> {
                btnTambahAwal.setVisibility(View.GONE);
                layoutQty.setVisibility(View.VISIBLE);
                txtQty.setTextColor(Color.BLACK);
                txtQty.setText("1");
                t.setQuantity(1);
                if (!selectedToppings.contains(t)) selectedToppings.add(t);
                updateNoteTopping(); // Memperbarui status bartopping
            });

            // Ketika tombol tambah lagi diklik
            btnTambah.setOnClickListener(v -> {
                int qty = Integer.parseInt(txtQty.getText().toString());
                qty++;
                txtQty.setText(String.valueOf(qty));
                t.setQuantity(qty);
                updateNoteTopping(); // Memperbarui status bartopping
            });

            // Ketika tombol kurang diklik
            btnKurang.setOnClickListener(v -> {
                int qty = Integer.parseInt(txtQty.getText().toString());
                if (qty > 1) {
                    qty--;
                    txtQty.setText(String.valueOf(qty));
                    t.setQuantity(qty);
                } else {
                    selectedToppings.removeIf(s -> s.getId().equals(t.getId())); // Menghapus topping
                    displaySelectedToppings(selectedToppings); // Memperbarui tampilan topping
                    updateNoteTopping(); // Memperbarui status bartopping
                }
            });

            kotakMenu.addView(itemView); // Menambahkan item topping ke layout
        }
    }


    private void loadCustomizationOptions() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiService.getCustomizationOptions().enqueue(new Callback<CustomizationResponse>() {
            @Override
            public void onResponse(Call<CustomizationResponse> call, Response<CustomizationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CustomizationOption> allOptions = response.body().getData();

                    kuahList.clear();
                    telurList.clear();
                    kencurList.clear();

                    for (CustomizationOption option : allOptions) {
                        switch (option.getCategoryId()) {
                            case "cat_690b004902b3c": kuahList.add(option); break;
                            case "cat_690b0054b1092": kencurList.add(option); break;
                            case "cat_690b003db21f4": telurList.add(option); break;
                        }
                    }

                    populateKuahRadioGroup();
                    populateTelurRadioGroup();
                    populateKencurSpinner();
                    loadLevelPedasFromAPI();
                } else {
                    Toast.makeText(SelectCustomization.this, "Gagal memuat data customization", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustomizationResponse> call, Throwable t) {
                showErrorDialog("Error memuat data customization: " + t.getMessage());
            }
        });
    }

    private void loadLevelPedasFromAPI() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiService.getAllSpiceLevels().enqueue(new Callback<SpiceLevelResponse>() {
            @Override
            public void onResponse(Call<SpiceLevelResponse> call, Response<SpiceLevelResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    levelPedasList = response.body().getData();
                    List<String> levels = new ArrayList<>();
                    for (SpiceLevel level : levelPedasList) levels.add(level.getName());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectCustomization.this,
                            android.R.layout.simple_spinner_item, levels) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView text = view.findViewById(android.R.id.text1);
                            text.setTextColor(getResources().getColor(android.R.color.black));
                            return view;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView text = view.findViewById(android.R.id.text1);
                            text.setTextColor(getResources().getColor(android.R.color.black));
                            view.setBackgroundResource(R.drawable.spinner_item_selector);
                            return view;
                        }
                    };
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLevel.setAdapter(adapter);
                } else {
                    Toast.makeText(SelectCustomization.this, "Gagal memuat data level pedas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SpiceLevelResponse> call, Throwable t) {
                showErrorDialog("Error memuat level pedas: " + t.getMessage());
            }
        });
    }

    private void populateKuahRadioGroup() {
        tipeKuah.removeAllViews();
        int color = ContextCompat.getColor(this, R.color.primary);
        for (CustomizationOption kuah : kuahList) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(View.generateViewId());
            radioButton.setText(kuah.getName());
            radioButton.setButtonTintList(ColorStateList.valueOf(color));
            radioButton.setTextColor(getResources().getColor(android.R.color.black));
            tipeKuah.addView(radioButton);
        }
    }

    private void populateTelurRadioGroup() {
        tipeTelur.removeAllViews();
        int color = ContextCompat.getColor(this, R.color.primary);
        for (CustomizationOption telur : telurList) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(View.generateViewId());
            radioButton.setText(telur.getName());
            radioButton.setButtonTintList(ColorStateList.valueOf(color));
            radioButton.setTextColor(getResources().getColor(android.R.color.black));
            tipeTelur.addView(radioButton);
        }
    }

    private void populateKencurSpinner() {
        List<String> names = new ArrayList<>();
        for (CustomizationOption kencur : kencurList) names.add(kencur.getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(android.R.color.black));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(android.R.color.black));
                view.setBackgroundResource(R.drawable.spinner_item_selector);
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKencur.setAdapter(adapter);
    }

    private void showErrorDialog(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.notification_errorsystem, null);
        TextView errorText = view.findViewById(R.id.errornote);
        errorText.setText(message);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.dialog_enter);
        view.startAnimation(anim);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnOk = view.findViewById(R.id.okbutton);
        btnOk.setOnClickListener(v -> dialog.dismiss());
    }

    private void updateNoteTopping() {
        if (selectedToppings.isEmpty()) {
            bartopping.setVisibility(View.VISIBLE);
        } else {
            bartopping.setVisibility(View.GONE);
        }
    }
}
