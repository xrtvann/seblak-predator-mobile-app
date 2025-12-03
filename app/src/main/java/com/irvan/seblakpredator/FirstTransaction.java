package com.irvan.seblakpredator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.irvan.seblakpredator.model.SelectedMenu;

import java.util.ArrayList;

public class FirstTransaction extends AppCompatActivity {

    private static final String TAG = "FirstTransaction";

    EditText edtNama, inputJamAmbil, numberTable;
    RadioButton dineInOption, deliveryOption;
    Button btnLanjut, backButton;
    RadioGroup typeOrder;
    TextView labelTime, labelNumberTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_transaction);

        // Transparent status bar
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );

        // ======= INISIALISASI VIEW =======
        edtNama = findViewById(R.id.costumerName);
        dineInOption = findViewById(R.id.dineInOption);
        deliveryOption = findViewById(R.id.deliveryOption);
        inputJamAmbil = findViewById(R.id.inputJamAmbil);
        btnLanjut = findViewById(R.id.btnLanjut);
        backButton = findViewById(R.id.backButton);
        labelTime = findViewById(R.id.labelTimePick);
        labelNumberTable = findViewById(R.id.labelTableNumber);
        typeOrder = findViewById(R.id.typeOrder);
        numberTable = findViewById(R.id.tableNumberInput);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // Ambil daftar menu sebelumnya jika ada
        ArrayList<SelectedMenu> existingMenus = getIntent().getSerializableExtra("existingMenus") != null ?
                (ArrayList<SelectedMenu>) getIntent().getSerializableExtra("existingMenus") :
                new ArrayList<>();

        // ======= SET NAMA OTOMATIS DARI LOGIN =======
        String username = prefs.getString("name", "");
        if (!username.isEmpty()) {
            edtNama.setText(username);
            edtNama.setEnabled(false);
            Log.d(TAG, "Nama otomatis dari login: " + username);
        }

        // ======= KEMBALI DARI SECOND TRANSACTION =======
        boolean restoreData = prefs.getBoolean("restoreData", false);
        if (restoreData) {
            restorePreviousData(prefs);
            prefs.edit().putBoolean("restoreData", false).apply(); // reset flag
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

        // Tombol back
        backButton.setOnClickListener(v -> finish());

        // ======= SET WARNA RADIO BUTTON =======
        int color = ContextCompat.getColor(this, R.color.primary);
        dineInOption.setButtonTintList(ColorStateList.valueOf(color));
        deliveryOption.setButtonTintList(ColorStateList.valueOf(color));

        // ======= RESTORE TIPE ORDER JIKA ADA =======
        String orderType = getIntent().getStringExtra("order_type");
        if (orderType != null) {
            if (orderType.equals("Dine In")) {
                typeOrder.check(dineInOption.getId());
                inputJamAmbil.setVisibility(View.GONE);
                labelTime.setVisibility(View.GONE);
                numberTable.setVisibility(View.VISIBLE);
                labelNumberTable.setVisibility(View.VISIBLE);
            } else if (orderType.equals("Delivery")) {
                typeOrder.check(deliveryOption.getId());
                inputJamAmbil.setVisibility(View.VISIBLE);
                labelTime.setVisibility(View.VISIBLE);
                numberTable.setVisibility(View.GONE);
                labelNumberTable.setVisibility(View.GONE);
            }
        }

        // Listener perubahan tipe order
        typeOrder.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == deliveryOption.getId()) {
                inputJamAmbil.setVisibility(View.VISIBLE);
                labelTime.setVisibility(View.VISIBLE);
                numberTable.setVisibility(View.GONE);
                labelNumberTable.setVisibility(View.GONE);
            } else {
                inputJamAmbil.setVisibility(View.GONE);
                labelTime.setVisibility(View.GONE);
                numberTable.setVisibility(View.VISIBLE);
                labelNumberTable.setVisibility(View.VISIBLE);
            }
        });

        // ======= BUTTON LANJUT =======
        btnLanjut.setOnClickListener(v -> {
            String nama = edtNama.getText().toString();
            String tablenumber = numberTable.getText().toString();

            if (nama.isEmpty() || (typeOrder.getCheckedRadioButtonId() == dineInOption.getId() && tablenumber.isEmpty())) {
                Toast.makeText(this, "Harap lengkapi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kirim ke SecondTransaction / SelectCustomization
            Intent intent = new Intent(FirstTransaction.this, SelectCustomization.class);
            intent.putExtra("nama", nama);

            // Kirim tipe order
            String selectedOrderType = typeOrder.getCheckedRadioButtonId() == dineInOption.getId() ? "Dine In" : "Delivery";
            intent.putExtra("orderType", selectedOrderType);

            // Kirim data tambahan
            intent.putExtra("tableNumber", tablenumber);
            intent.putExtra("waktuAmbil", inputJamAmbil.getText().toString());

            startActivity(intent);
        });
    }

    // ======= RESTORE DATA DARI SECOND TRANSACTION =======
    private void restorePreviousData(SharedPreferences prefs) {
        edtNama.setText(prefs.getString("nama", ""));

        String orderType = prefs.getString("orderType", "");
        if(orderType.equals("Dine In")) {
            typeOrder.check(dineInOption.getId());
            inputJamAmbil.setVisibility(View.GONE);
            labelTime.setVisibility(View.GONE);
        } else {
            typeOrder.check(deliveryOption.getId());
            inputJamAmbil.setVisibility(View.VISIBLE);
            labelTime.setVisibility(View.VISIBLE);
            inputJamAmbil.setText(prefs.getString("waktuAmbil", ""));
        }
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
