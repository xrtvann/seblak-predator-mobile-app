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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.irvan.seblakpredator.apiclient.ApiClient;
import com.irvan.seblakpredator.apiclient.ApiService;
import com.irvan.seblakpredator.model.CustomizationOption;
import com.irvan.seblakpredator.model.CustomizationResponse;
import com.irvan.seblakpredator.model.SelectedMenu;
import com.irvan.seblakpredator.model.SpiceLevel;
import com.irvan.seblakpredator.model.SpiceLevelResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectCustomization extends AppCompatActivity {

    private static final String TAG = "SelectCustomization";
    private static final int REQUEST_SECOND_TRANSACTION = 200;

    Spinner spinnerLevel, spinnerKencur;
    RadioGroup tipeKuah, tipeTelur;
    Button btnLanjut, backButton;

    private List<CustomizationOption> kuahList = new ArrayList<>();
    private List<CustomizationOption> telurList = new ArrayList<>();
    private List<CustomizationOption> kencurList = new ArrayList<>();
    private List<SpiceLevel> levelPedasList = new ArrayList<>();

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

        spinnerLevel = findViewById(R.id.pilihanLevel);
        spinnerKencur = findViewById(R.id.pilihanKencur);
        tipeKuah = findViewById(R.id.tipeKuah);
        tipeTelur = findViewById(R.id.tipeTelur);
        btnLanjut = findViewById(R.id.btnLanjut);
        backButton = findViewById(R.id.backButton);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String nama = getIntent().getStringExtra("nama");
        String orderType = getIntent().getStringExtra("orderType");

        ArrayList<SelectedMenu> existingMenus = getIntent().getSerializableExtra("existingMenus") != null ?
                (ArrayList<SelectedMenu>) getIntent().getSerializableExtra("existingMenus") :
                new ArrayList<>();

        boolean restoreData = prefs.getBoolean("restoreData", false);
        if (restoreData) {
            restorePreviousData(prefs);
            prefs.edit().putBoolean("restoreData", false).apply();
        }

        backButton.setOnClickListener(v -> finish());

        loadCustomizationOptions();

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

            // Buat menu baru
            SelectedMenu newMenu = new SelectedMenu(
                    nama,
                    level,
                    pilihKuah.getText().toString(),
                    pilihTelur.getText().toString(),
                    kencur,
                    hargaLevel,
                    hargaKuah,
                    hargaTelur,
                    hargaKencur,
                    new ArrayList<>()
            );

            // Kirim ke SecondTransaction
            Intent intent = new Intent(SelectCustomization.this, SecondTransaction.class);
            intent.putExtra("nama", nama);
            intent.putExtra("orderType", orderType);
            intent.putExtra("level", level);
            intent.putExtra("kuah", pilihKuah.getText().toString());
            intent.putExtra("telur", pilihTelur.getText().toString());
            intent.putExtra("kencur", kencur);
            intent.putExtra("hargaLevel", hargaLevel);
            intent.putExtra("hargaKuah", hargaKuah);
            intent.putExtra("hargaTelur", hargaTelur);
            intent.putExtra("hargaKencur", hargaKencur);
            intent.putExtra("existingMenus", existingMenus);

            startActivityForResult(intent, REQUEST_SECOND_TRANSACTION);
        });
    }

    // TERUSKAN HASIL DARI SECOND TRANSACTION
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SECOND_TRANSACTION && resultCode == RESULT_OK && data != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("existingMenus", data.getSerializableExtra("existingMenus"));
            setResult(RESULT_OK, resultIntent);
            finish(); // kembali ke FirstTransaction
        }
    }

    private void restorePreviousData(SharedPreferences prefs) {
        spinnerLevel.setSelection(getIndexSpinner(spinnerLevel, prefs.getString("level", "")));
        spinnerKencur.setSelection(getIndexSpinner(spinnerKencur, prefs.getString("kencur", "")));

        String kuah = prefs.getString("kuah", "");
        for (int i = 0; i < tipeKuah.getChildCount(); i++) {
            RadioButton rb = (RadioButton) tipeKuah.getChildAt(i);
            if (rb.getText().toString().equals(kuah)) rb.setChecked(true);
        }

        String telur = prefs.getString("telur", "");
        for (int i = 0; i < tipeTelur.getChildCount(); i++) {
            RadioButton rb = (RadioButton) tipeTelur.getChildAt(i);
            if (rb.getText().toString().equals(telur)) rb.setChecked(true);
        }
    }

    private int getIndexSpinner(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) return i;
        }
        return 0;
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
}
