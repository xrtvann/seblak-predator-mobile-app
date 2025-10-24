package com.irvan.seblakpredator;

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
    Spinner spinnerLevel, spinnerKencur, spinnerRasa;
    RadioGroup rgKuah, rgTelur;
    Button btnLanjut;

    // Data Dropdown
    String[] dataLevel = {"Level 0", "Level 1", "Level 2", "Level 3"};
    String[] dataKencur = {"Sedikit", "Normal", "Banyak"};
    String[] dataRasa = {"Pedas", "Sedang", "Manis"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_transaction);

        // FIX fitur EdgeToEdge (tidak error)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ================== INISIALISASI VIEW ==================
        edtNama = findViewById(R.id.edtNama);
        spinnerLevel = findViewById(R.id.spinnerLevel);
        spinnerKencur = findViewById(R.id.spinnerKencur);
        spinnerRasa = findViewById(R.id.spinnerRasa);
        rgKuah = findViewById(R.id.rgKuah);
        rgTelur = findViewById(R.id.rgTelur);
        btnLanjut = findViewById(R.id.btnLanjut);

        // ================== SET DROPDOWN (SPINNER) ==================
        spinnerLevel.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dataLevel));
        spinnerKencur.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dataKencur));
        spinnerRasa.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dataRasa));

        // ================== BUTTON KLIK ==================
        btnLanjut.setOnClickListener(v -> {
            String nama = edtNama.getText().toString();
            String level = spinnerLevel.getSelectedItem().toString();
            String kencur = spinnerKencur.getSelectedItem().toString();
            String rasa = spinnerRasa.getSelectedItem().toString();

            int idKuah = rgKuah.getCheckedRadioButtonId();
            RadioButton pilihKuah = findViewById(idKuah);
            String kuah = pilihKuah.getText().toString();

            int idTelur = rgTelur.getCheckedRadioButtonId();
            RadioButton pilihTelur = findViewById(idTelur);
            String telur = pilihTelur.getText().toString();

            Toast.makeText(this,
                    "Nama : " + nama +
                            "\nLevel : " + level +
                            "\nKuah : " + kuah +
                            "\nTelur : " + telur +
                            "\nKencur : " + kencur +
                            "\nRasa : " + rasa,
                    Toast.LENGTH_LONG).show();
        });
    }
}
