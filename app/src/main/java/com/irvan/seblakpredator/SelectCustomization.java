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
import androidx.cardview.widget.CardView;
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

    Spinner spinnerKencur;
    RadioGroup tipeKuah, tipeTelur;
    Button btnLanjut, backButton, addToppingButton;
    ConstraintLayout bartopping;
    LinearLayout kotakMenu, spiceContainer;

    private List<CustomizationOption> kuahList = new ArrayList<>();
    private List<CustomizationOption> telurList = new ArrayList<>();
    private List<CustomizationOption> kencurList = new ArrayList<>();
    private List<SpiceLevel> levelPedasList = new ArrayList<>();

    private String selectedLevelPedas = "";
    private CardView selectedCardView = null;

    private ArrayList<SelectedTopping> selectedToppings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcustomization);

        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );

        spinnerKencur = findViewById(R.id.pilihanKencur);
        tipeKuah = findViewById(R.id.tipeKuah);
        tipeTelur = findViewById(R.id.tipeTelur);
        btnLanjut = findViewById(R.id.btnLanjut);
        backButton = findViewById(R.id.backButton);
        addToppingButton = findViewById(R.id.btnAddTopping);
        kotakMenu = findViewById(R.id.KotakMenu);
        bartopping = findViewById(R.id.statusTopping);

        spiceContainer = findViewById(R.id.toppingContainer);

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
        loadCustomizationOptions();

        addToppingButton.setOnClickListener(v ->
                openSecondTransaction(nama, orderType, address, userId, existingMenus)
        );

        btnLanjut.setOnClickListener(v -> {

            if (selectedLevelPedas.isEmpty()) {
                Toast.makeText(this, "Harap pilih level pedas!", Toast.LENGTH_SHORT).show();
                return;
            }

            String kencur = spinnerKencur.getSelectedItem() != null ?
                    spinnerKencur.getSelectedItem().toString() : "";

            int idKuah = tipeKuah.getCheckedRadioButtonId();
            int idTelur = tipeTelur.getCheckedRadioButtonId();

            if (idKuah == -1 || idTelur == -1) {
                Toast.makeText(this, "Harap lengkapi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton pilihKuah = findViewById(idKuah);
            RadioButton pilihTelur = findViewById(idTelur);

            int hargaLevel = 0;
            for (SpiceLevel sl : levelPedasList)
                if (sl.getName().equals(selectedLevelPedas)) hargaLevel = (int) sl.getPrice();

            int hargaKuah = 0;
            for (CustomizationOption kuah : kuahList)
                if (kuah.getName().equals(pilihKuah.getText().toString()))
                    hargaKuah = (int) kuah.getPrice();

            int hargaTelur = 0;
            for (CustomizationOption t : telurList)
                if (t.getName().equals(pilihTelur.getText().toString()))
                    hargaTelur = (int) t.getPrice();

            int hargaKencur = 0;
            for (CustomizationOption k : kencurList)
                if (k.getName().equals(kencur)) hargaKencur = (int) k.getPrice();

            SelectedMenu currentMenu = new SelectedMenu(
                    nama,
                    selectedLevelPedas,
                    pilihKuah.getText().toString(),
                    pilihTelur.getText().toString(),
                    kencur,
                    hargaLevel,
                    hargaKuah,
                    hargaTelur,
                    hargaKencur,
                    new ArrayList<>(selectedToppings)
            );

            existingMenus.add(currentMenu);

            Intent intent = new Intent(this, TransaksiActivity.class);
            intent.putExtra("user_id", userId);
            intent.putExtra("existingMenus", existingMenus);
            intent.putExtra("order_type", orderType);
            intent.putExtra("address", address);
            startActivity(intent);
        });
    }

    // ----------------- LEVEL PEDAS CARDVIEW ------------------

    private void loadLevelPedasFromAPI() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        apiService.getAllSpiceLevels().enqueue(new Callback<SpiceLevelResponse>() {
            @Override
            public void onResponse(Call<SpiceLevelResponse> call, Response<SpiceLevelResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {

                    levelPedasList = response.body().getData();
                    spiceContainer.removeAllViews();

                    LayoutInflater inflater = LayoutInflater.from(SelectCustomization.this);

                    for (SpiceLevel spice : levelPedasList) {

                        View card = inflater.inflate(R.layout.cardview_item, spiceContainer, false);

                        CardView detailSpice = card.findViewById(R.id.detailTopping);
                        TextView levelSpice = card.findViewById(R.id.NameTopping);
                        TextView priceSpice = card.findViewById(R.id.priceTopping);

                        levelSpice.setText(spice.getName());
                        priceSpice.setText("Rp" + spice.getPrice());

                        detailSpice.setOnClickListener(v ->
                                handleSpiceSelection(detailSpice, spice.getName())
                        );

                        spiceContainer.addView(card);
                    }

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

    private void handleSpiceSelection(CardView clickedCard, String levelName) {

        if (selectedCardView == null) {
            selectCard(clickedCard, levelName);
            return;
        }

        if (selectedCardView == clickedCard) {
            unselectCard();
            return;
        }

        unselectCard();
        selectCard(clickedCard, levelName);
    }
    private void animateCardSelect(CardView card) {
        card.animate().scaleX(1.05f).scaleY(1.05f).setDuration(150).start();
        card.setCardElevation(12f); // shadow lebih besar
        card.setCardBackgroundColor(Color.parseColor("#FFE082")); // highlight
    }

    private void animateCardUnselect(CardView card) {
        card.animate().scaleX(1f).scaleY(1f).setDuration(150).start();
        card.setCardElevation(2f); // kembali kecil
        card.setCardBackgroundColor(Color.WHITE); // warna normal
    }


    private void selectCard(CardView card, String levelName) {
        animateCardSelect(card);
        selectedCardView = card;
        selectedLevelPedas = levelName;
    }

    private void unselectCard() {
        if (selectedCardView != null) {
            animateCardUnselect(selectedCardView);
        }
        selectedCardView = null;
        selectedLevelPedas = "";
    }

    // ------------------ LOAD CUSTOMIZATION ---------------------

    private void loadCustomizationOptions() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiService.getCustomizationOptions().enqueue(new Callback<CustomizationResponse>() {
            @Override
            public void onResponse(Call<CustomizationResponse> call, Response<CustomizationResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(SelectCustomization.this, "Gagal memuat customization", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<CustomizationOption> allOptions = response.body().getData();

                kuahList.clear();
                telurList.clear();
                kencurList.clear();

                for (CustomizationOption option : allOptions) {
                    switch (option.getCategoryId()) {
                        case "cat_690b004902b3c":
                            kuahList.add(option);
                            break;
                        case "cat_690b0054b1092":
                            kencurList.add(option);
                            break;
                        case "cat_690b003db21f4":
                            telurList.add(option);
                            break;
                    }
                }

                populateKuahRadioGroup();
                populateTelurRadioGroup();
                populateKencurSpinner();
                loadLevelPedasFromAPI();
                displaySelectedToppings(selectedToppings); // tampilkan topping
            }

            @Override
            public void onFailure(Call<CustomizationResponse> call, Throwable t) {
                showErrorDialog("Gagal memuat customization: " + t.getMessage());
            }
        });
    }

    // ---------------- RADIO & SPINNER -------------------

    private void populateKuahRadioGroup() {
        tipeKuah.removeAllViews();
        int color = ContextCompat.getColor(this, R.color.primary);

        for (CustomizationOption kuah : kuahList) {
            RadioButton rd = new RadioButton(this);
            rd.setId(View.generateViewId());
            rd.setText(kuah.getName());
            rd.setButtonTintList(ColorStateList.valueOf(color));
            rd.setTextColor(Color.BLACK);
            tipeKuah.addView(rd);
        }
    }

    private void populateTelurRadioGroup() {
        tipeTelur.removeAllViews();
        int color = ContextCompat.getColor(this, R.color.primary);

        for (CustomizationOption telur : telurList) {
            RadioButton rd = new RadioButton(this);
            rd.setId(View.generateViewId());
            rd.setText(telur.getName());
            rd.setButtonTintList(ColorStateList.valueOf(color));
            rd.setTextColor(Color.BLACK);
            tipeTelur.addView(rd);
        }
    }

    private void populateKencurSpinner() {
        List<String> names = new ArrayList<>();
        for (CustomizationOption k : kencurList) names.add(k.getName());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, names){
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

    // ----------------- OTHER FUNCTIONS ------------------

    private void openSecondTransaction(String nama, String orderType, String address,
                                       String userId, ArrayList<SelectedMenu> existingMenus) {

        if (selectedLevelPedas.isEmpty()) {
            Toast.makeText(this, "Harap pilih level pedas!", Toast.LENGTH_SHORT).show();
            return;
        }

        int idKuah = tipeKuah.getCheckedRadioButtonId();
        int idTelur = tipeTelur.getCheckedRadioButtonId();

        if (idKuah == -1 || idTelur == -1) {
            Toast.makeText(this, "Harap lengkapi Kuah & Telur!", Toast.LENGTH_SHORT).show();
            return;
        }

        String kencur = spinnerKencur.getSelectedItem() != null ?
                spinnerKencur.getSelectedItem().toString() : "";

        RadioButton pilihKuah = findViewById(idKuah);
        RadioButton pilihTelur = findViewById(idTelur);

        int hargaLevel = 0;
        for (SpiceLevel sl : levelPedasList)
            if (sl.getName().equals(selectedLevelPedas)) hargaLevel = (int) sl.getPrice();

        int hargaKuah = 0;
        for (CustomizationOption kuah : kuahList)
            if (kuah.getName().equals(pilihKuah.getText().toString()))
                hargaKuah = (int) kuah.getPrice();

        int hargaTelur = 0;
        for (CustomizationOption telur : telurList)
            if (telur.getName().equals(pilihTelur.getText().toString()))
                hargaTelur = (int) telur.getPrice();

        int hargaKencur = 0;
        for (CustomizationOption k : kencurList)
            if (k.getName().equals(kencur)) hargaKencur = (int) k.getPrice();

        SelectedMenu currentMenu = new SelectedMenu(
                nama,
                selectedLevelPedas,
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
        intent.putExtra("order_type", orderType);
        intent.putExtra("address", address);
        intent.putExtra("level", selectedLevelPedas);
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

    // ----------------- TOPPING FUNCTIONS ------------------

    private void updateNoteTopping() {
        if (selectedToppings.isEmpty()) {
            bartopping.setVisibility(View.VISIBLE);
        } else {
            bartopping.setVisibility(View.GONE);
        }
    }

    private void displaySelectedToppings(List<SelectedTopping> toppings) {
        LinearLayout toppingContainer = findViewById(R.id.toppingItemSelectContainer);
        toppingContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        if (toppings.isEmpty()) {
            bartopping.setVisibility(View.VISIBLE);
            return;
        } else {
            bartopping.setVisibility(View.GONE);
        }

        for (SelectedTopping t : toppings) {
            // Inflate CardView layout
            View cardView = inflater.inflate(R.layout.item_toppingmenu, toppingContainer, false);

            TextView name = cardView.findViewById(R.id.NameTopping);
            TextView price = cardView.findViewById(R.id.priceTopping);
            LinearLayout layoutQty = cardView.findViewById(R.id.layoutQty);
            ImageView btnTambahAwal = cardView.findViewById(R.id.btnTambah2);
            ImageView btnTambah = cardView.findViewById(R.id.btnTambah);
            ImageView btnKurang = cardView.findViewById(R.id.btnKurang);
            ImageView deleteButton = cardView.findViewById(R.id.deleteButtonItemSelected);

            // Set data
            name.setText(t.getName());
            price.setText("Rp" + t.getPrice());

            if (t.getQuantity() > 0) {
                btnTambahAwal.setVisibility(View.GONE);
                layoutQty.setVisibility(View.VISIBLE);
                ((TextView) layoutQty.findViewById(R.id.txtQty)).setText(String.valueOf(t.getQuantity()));
            } else {
                btnTambahAwal.setVisibility(View.VISIBLE);
                layoutQty.setVisibility(View.GONE);
            }

            // Tombol tambah awal (awal belum ada qty)
            btnTambahAwal.setOnClickListener(v -> {
                btnTambahAwal.setVisibility(View.GONE);
                layoutQty.setVisibility(View.VISIBLE);
                ((TextView) layoutQty.findViewById(R.id.txtQty)).setText("1");
                t.setQuantity(1);

                if (!selectedToppings.contains(t)) selectedToppings.add(t);
                updateNoteTopping();
            });

            // Tombol tambah qty
            btnTambah.setOnClickListener(v -> {
                TextView txtQty = layoutQty.findViewById(R.id.txtQty);
                int qty = Integer.parseInt(txtQty.getText().toString());
                qty++;
                txtQty.setText(String.valueOf(qty));
                t.setQuantity(qty);
                updateNoteTopping();
            });

            // Tombol kurang qty
            btnKurang.setOnClickListener(v -> {
                TextView txtQty = layoutQty.findViewById(R.id.txtQty);
                int qty = Integer.parseInt(txtQty.getText().toString());
                if (qty > 1) {
                    qty--;
                    txtQty.setText(String.valueOf(qty));
                    t.setQuantity(qty);
                } else {
                    selectedToppings.removeIf(s -> s.getId().equals(t.getId()));
                    displaySelectedToppings(selectedToppings);
                    updateNoteTopping();
                }
            });

            // Tombol delete
            deleteButton.setOnClickListener(v -> {
                selectedToppings.remove(t);
                displaySelectedToppings(selectedToppings); // refresh preview
                updateNoteTopping();
            });

            // Tambahkan CardView ke container
            toppingContainer.addView(cardView);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SECOND_TRANSACTION && resultCode == RESULT_OK && data != null) {
            ArrayList<SelectedTopping> returnedToppings = (ArrayList<SelectedTopping>) data.getSerializableExtra("selectedToppings");
            if (returnedToppings != null) {
                selectedToppings.clear();
                selectedToppings.addAll(returnedToppings);
                displaySelectedToppings(selectedToppings);
            }
        }
    }
}