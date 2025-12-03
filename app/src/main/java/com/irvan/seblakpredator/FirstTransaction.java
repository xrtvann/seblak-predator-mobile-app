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

    EditText edtNama;
    RadioButton deliveryOption;
    Button btnLanjut, backButton;
    RadioGroup typeOrder;

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
        deliveryOption = findViewById(R.id.deliveryOption);
        btnLanjut = findViewById(R.id.btnLanjut);
        backButton = findViewById(R.id.backButton);
        typeOrder = findViewById(R.id.typeOrder);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // Ambil daftar menu sebelumnya jika ada
        ArrayList<SelectedMenu> existingMenus = getIntent().getSerializableExtra("existingMenus") != null ?
                (ArrayList<SelectedMenu>) getIntent().getSerializableExtra("existingMenus") :
                new ArrayList<>();
        String userId = prefs.getString("user_id", null);

        if (userId == null) {
            Toast.makeText(this, "User tidak ditemukan, silakan login ulang.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("FirstTransaction", "User ID ditemukan: " + userId);


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


        // Tombol back
        backButton.setOnClickListener(v -> finish());

        // ======= SET WARNA RADIO BUTTON =======
        int color = ContextCompat.getColor(this, R.color.primary);
        deliveryOption.setButtonTintList(ColorStateList.valueOf(color));

        // ======= RESTORE TIPE ORDER JIKA ADA =======
        String orderType = getIntent().getStringExtra("order_type");

        // Listener perubahan tipe order

        // ======= BUTTON LANJUT =======
        btnLanjut.setOnClickListener(v -> {
            String nama = edtNama.getText().toString();
            String address = "";

            if (nama.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kirim ke SecondTransaction / SelectCustomization
            Intent intent = new Intent(FirstTransaction.this, SelectCustomization.class);
            intent.putExtra("user_id", userId);

            intent.putExtra("nama", nama);

            // Kirim tipe order
            String selectedOrderType = typeOrder.getCheckedRadioButtonId() == deliveryOption.getId() ? "Dine In" : "Delivery";
            intent.putExtra("orderType", selectedOrderType);

            // Kirim data tambahan
            intent.putExtra("address", address); // <--- kirim alamat

            startActivity(intent);
        });
    }

    // ======= RESTORE DATA DARI SECOND TRANSACTION =======
    private void restorePreviousData(SharedPreferences prefs) {
        edtNama.setText(prefs.getString("nama", ""));

        String orderType = prefs.getString("orderType", "");
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
