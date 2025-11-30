package com.irvan.seblakpredator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.irvan.seblakpredator.apiclient.ApiClient;
import com.irvan.seblakpredator.apiclient.ApiService;
import com.irvan.seblakpredator.model.CustomizationOption;
import com.irvan.seblakpredator.model.CustomizationResponse;
import com.irvan.seblakpredator.model.SpiceLevel;
import com.irvan.seblakpredator.model.SpiceLevelResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstTransaction extends AppCompatActivity {

    private static final String TAG = "FirstTransaction";

    EditText edtNama, inputJamAmbil;
    Spinner spinnerLevel, spinnerKencur;
    RadioGroup tipeKuah, tipeTelur, typeOrder;
    RadioButton dineInOption, deliveryOption, kuahNormal, kuahNyemek, TelurUtuh, TelurOrak;
    Button btnLanjut , backButton;

    private List<CustomizationOption> kuahList = new ArrayList<>();
    private List<CustomizationOption> telurList = new ArrayList<>();
    private List<CustomizationOption> kencurList = new ArrayList<>();
    private List<SpiceLevel> levelPedasList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_transaction);

        // ======= INISIALISASI VIEW =======
        edtNama = findViewById(R.id.costumerName);
        spinnerLevel = findViewById(R.id.pilihanLevel);
        spinnerKencur = findViewById(R.id.pilihanKencur);
        tipeKuah = findViewById(R.id.tipeKuah);
        tipeTelur = findViewById(R.id.tipeTelur);
        typeOrder = findViewById(R.id.typeOrder);
        dineInOption = findViewById(R.id.dineInOption);
        deliveryOption = findViewById(R.id.deliveryOption);
        inputJamAmbil = findViewById(R.id.inputJamAmbil);
        btnLanjut = findViewById(R.id.btnLanjut);
        backButton = findViewById(R.id.backButton);
        TextView labelTime = findViewById(R.id.labelTimePick);

        // ======= SET NAMA OTOMATIS DARI LOGIN =======
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = prefs.getString("name", "");
        if (!username.isEmpty()) {
            edtNama.setText(username);
            edtNama.setEnabled(false);
            Log.d(TAG, "Nama otomatis dari login: " + username);
        }

        // ======= WAKTU AMBIL =======
        inputJamAmbil.setOnClickListener(v -> {
            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(7)
                    .setMinute(0)
                    .setTitleText("Pilih Waktu Ambil")
                    .build();
            picker.show(getSupportFragmentManager(), "TIME_PICK");
            picker.addOnPositiveButtonClickListener(view -> {
                int hour = picker.getHour();
                int minute = picker.getMinute();
                String waktu = String.format("%02d:%02d", hour, minute);
                inputJamAmbil.setText(waktu);
                Log.d(TAG, "Waktu ambil dipilih: " + waktu);
            });
        });

        backButton.setOnClickListener(v -> startActivity(new Intent(FirstTransaction.this, MainActivity.class)));

        // ======= SET DEFAULT TYPE ORDER =======
        String orderType = getIntent().getStringExtra("order_type");
        if(orderType != null){
            if(orderType.equals("Dine In")){
                typeOrder.check(dineInOption.getId());
                inputJamAmbil.setVisibility(View.GONE);
                labelTime.setVisibility(View.GONE);
            } else if(orderType.equals("Delivery")){
                typeOrder.check(deliveryOption.getId());
                inputJamAmbil.setVisibility(View.VISIBLE);
                labelTime.setVisibility(View.VISIBLE);
            }
        }

        typeOrder.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == deliveryOption.getId()){
                inputJamAmbil.setVisibility(View.VISIBLE);
                labelTime.setVisibility(View.VISIBLE);
            } else {
                inputJamAmbil.setVisibility(View.GONE);
                labelTime.setVisibility(View.GONE);
            }
        });

        // ======= LOAD CUSTOMIZATION OPTIONS & LEVEL PEDAS =======
        loadCustomizationOptions();

        // ======= BUTTON LANJUT =======
        btnLanjut.setOnClickListener(v -> {
            String nama = edtNama.getText().toString();
            String level = spinnerLevel.getSelectedItem() != null ? spinnerLevel.getSelectedItem().toString() : "";
            String kencur = spinnerKencur.getSelectedItem() != null ? spinnerKencur.getSelectedItem().toString() : "";
            int idKuah = tipeKuah.getCheckedRadioButtonId();
            int idTelur = tipeTelur.getCheckedRadioButtonId();

            if(idKuah == -1 || idTelur == -1 || nama.isEmpty() || level.isEmpty()){
                Toast.makeText(this, "Harap lengkapi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Menyimpan data di SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("nama", nama);
            editor.putString("level", level);
            editor.putString("kencur", kencur);
            RadioButton pilihKuah = findViewById(idKuah);
            editor.putString("kuah", pilihKuah.getText().toString());
            RadioButton pilihTelur = findViewById(idTelur);
            editor.putString("telur", pilihTelur.getText().toString());
            editor.putString("orderType", ((RadioButton)findViewById(typeOrder.getCheckedRadioButtonId())).getText().toString());
            editor.putString("waktuAmbil", inputJamAmbil.getText().toString());
            editor.apply();

            // Lanjut ke SecondTransaction
            Intent intent = new Intent(FirstTransaction.this, SecondTransaction.class);
            intent.putExtra("nama", nama);
            intent.putExtra("level", level);
            intent.putExtra("kencur", kencur);
            intent.putExtra("kuah", pilihKuah.getText().toString());
            intent.putExtra("telur", pilihTelur.getText().toString());
            intent.putExtra("orderType", ((RadioButton)findViewById(typeOrder.getCheckedRadioButtonId())).getText().toString());
            intent.putExtra("waktuAmbil", inputJamAmbil.getText().toString());
            startActivity(intent);
        });

    }

    // ================= LOAD CUSTOMIZATION =================
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
                            case "cat_690b004902b3c": kuahList.add(option); break;       // Kuah
                            case "cat_690b0054b1092": kencurList.add(option); break;    // Kencur
                            case "cat_690b003db21f4": telurList.add(option); break;     // Telur
                        }
                    }

                    populateKuahRadioGroup();
                    populateTelurRadioGroup();
                    populateKencurSpinner();

                    // Setelah custom options berhasil di-load, load Level Pedas dari API baru
                    loadLevelPedasFromAPI();

                } else {
                    Toast.makeText(FirstTransaction.this, "Gagal memuat data customization", Toast.LENGTH_SHORT).show();
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
                    if (levelPedasList.isEmpty()) {
                        Log.d(TAG, "Level pedas kosong, tidak menampilkan spinner");
                        return;
                    }

                    List<String> levels = new ArrayList<>();
                    for (SpiceLevel level : levelPedasList) {
                        levels.add(level.getName());
                    }

                    // Gunakan ArrayAdapter custom untuk spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FirstTransaction.this,
                            android.R.layout.simple_spinner_item, levels) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView text = view.findViewById(android.R.id.text1);
                            text.setTextColor(getResources().getColor(android.R.color.black)); // Teks hitam
                            return view;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView text = view.findViewById(android.R.id.text1);
                            text.setTextColor(getResources().getColor(android.R.color.black)); // Teks hitam
                            view.setBackgroundResource(R.drawable.spinner_item_selector); // Background selector
                            return view;
                        }
                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLevel.setAdapter(adapter);
                } else {
                    Toast.makeText(FirstTransaction.this, "Gagal memuat data level pedas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SpiceLevelResponse> call, Throwable t) {
                showErrorDialog("Error memuat level pedas: " + t.getMessage());
            }
        });
    }

    // ================= POPULATE VIEWS =================
    private void populateKuahRadioGroup() {
        tipeKuah.removeAllViews();
        for (CustomizationOption kuah : kuahList) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(View.generateViewId());
            radioButton.setText(kuah.getName());
            radioButton.setTextColor(getResources().getColor(android.R.color.black)); // ⚡ WARNA HITAM
            tipeKuah.addView(radioButton);
        }
    }

    private void populateTelurRadioGroup() {
        tipeTelur.removeAllViews();
        for (CustomizationOption telur : telurList) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(View.generateViewId());
            radioButton.setText(telur.getName());
            radioButton.setTextColor(getResources().getColor(android.R.color.black)); // ⚡ WARNA HITAM
            tipeTelur.addView(radioButton);
        }
    }

    private void populateKencurSpinner() {
        List<String> names = new ArrayList<>();
        for (CustomizationOption kencur : kencurList) {
            names.add(kencur.getName());
        }

        // Membuat ArrayAdapter custom untuk spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(android.R.color.black)); // Mengatur warna teks menjadi hitam
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(android.R.color.black)); // Mengatur warna teks menjadi hitam
                view.setBackgroundResource(R.drawable.spinner_item_selector); // Menggunakan spinner_item_selector.xml untuk background
                return view;
            }
        };

        // Menetapkan adapter ke Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKencur.setAdapter(adapter);
    }

    // ================= ERROR DIALOG =================
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
