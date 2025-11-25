package com.irvan.seblakpredator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class DeliveryTransaction extends AppCompatActivity {

    EditText edtNama;
    Spinner spinnerLevel, spinnerKencur;
    RadioGroup rgKuah, rgTelur;
    Button btnLanjut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery_transaction);

        // Setup EdgeToEdge (hindari error padding layar)
        RelativeLayout header = findViewById(R.id.header);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.header), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    v.getPaddingLeft(),
                    systemBars.top,     // kasih padding dari status bar
                    v.getPaddingRight(),
                    v.getPaddingBottom()
            );
            return insets;
        });

        // ================== INISIALISASI VIEW ==================

        edtNama = findViewById(R.id.costumerName);
        spinnerLevel = findViewById(R.id.pilihanLevel);
        spinnerKencur = findViewById(R.id.pilihanKencur);
        rgKuah = findViewById(R.id.tipeKuah);
        rgTelur = findViewById(R.id.tipeTelur);
        btnLanjut = findViewById(R.id.btnLanjut);

        EditText inputJamAmbil = findViewById(R.id.inputJamAmbil);

        inputJamAmbil.setOnClickListener(v -> {

            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H) // atau CLOCK_24H
                    .setHour(7)     // default jam
                    .setMinute(0)   // default menit
                    .setTitleText("Pilih Waktu Ambil")
                    .build();

            picker.show(getSupportFragmentManager(), "TIME_PICK");

            picker.addOnPositiveButtonClickListener(view -> {
                int hour = picker.getHour();
                int minute = picker.getMinute();

                // Format jam jadi 2 digit
                String waktu = String.format("%02d:%02d", hour, minute);

                inputJamAmbil.setText(waktu);
            });
        });

        // ================== BUTTON LANJUT ==================
        btnLanjut.setOnClickListener(v -> {

            // Ambil semua data input
            String nama = edtNama.getText().toString();
            String level = spinnerLevel.getSelectedItem().toString();
            String kencur = spinnerKencur.getSelectedItem().toString();

            int idKuah = rgKuah.getCheckedRadioButtonId();
            int idTelur = rgTelur.getCheckedRadioButtonId();

            // Validasi biar user isi semua
            if (idKuah == -1 || idTelur == -1 || nama.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi semua data terlebih dahulu!", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton pilihKuah = findViewById(idKuah);
            RadioButton pilihTelur = findViewById(idTelur);

            String kuah = pilihKuah.getText().toString();
            String telur = pilihTelur.getText().toString();

            // Tampilkan data (opsional)
            Toast.makeText(this,
                    "Nama : " + nama +
                            "\nLevel : " + level +
                            "\nKuah : " + kuah +
                            "\nTelur : " + telur +
                            "\nKencur : " + kencur,
                    Toast.LENGTH_LONG).show();

            // ================== PINDAH HALAMAN ==================
            Intent intent = new Intent(DeliveryTransaction.this, SecondTransaction.class);

            // (Opsional) Kirim data ke halaman kedua
            intent.putExtra("nama", nama);
            intent.putExtra("level", level);
            intent.putExtra("kuah", kuah);
            intent.putExtra("telur", telur);
            intent.putExtra("kencur", kencur);

            startActivity(intent);
        });
    }
}
