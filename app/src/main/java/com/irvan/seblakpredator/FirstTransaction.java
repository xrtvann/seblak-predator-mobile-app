package com.irvan.seblakpredator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FirstTransaction extends AppCompatActivity {

    EditText edtNama;
    Spinner spinnerLevel, spinnerKencur;
    RadioGroup rgKuah, rgTelur;
    Button btnLanjut;

    // Data Dropdown
    String[] dataLevel = {"Level 0", "Level 1", "Level 2", "Level 3"};
    String[] dataKencur = {"Sedikit", "Normal", "Banyak"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_transaction);

        // Setup EdgeToEdge (hindari error padding layar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ================== INISIALISASI VIEW ==================

        edtNama = findViewById(R.id.costumerName);
        spinnerLevel = findViewById(R.id.pilihanLevel);
        spinnerKencur = findViewById(R.id.pilihanKencur);
        rgKuah = findViewById(R.id.tipeKuah);
        rgTelur = findViewById(R.id.tipeTelur);
        btnLanjut = findViewById(R.id.btnLanjut);

        // ================== SET DROPDOWN (SPINNER) ==================
        spinnerLevel.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dataLevel));
        spinnerKencur.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dataKencur));

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
            Intent intent = new Intent(FirstTransaction.this, SecondTransaction.class);

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
