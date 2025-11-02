package com.irvan.seblakpredator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ThirdTransaction extends AppCompatActivity {

    private Button btnLanjut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_transaction);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLanjut = findViewById(R.id.btnLanjut);

        btnLanjut.setOnClickListener(v -> {
            try {
                tambahKeKeranjang(
                        "Pelanggan Baru",
                        "Rp 17.000",
                        "Kuah Normal, Telur Utuh, Kencur Normal, Gurih, Level 2, CrabStick (1x), Kerupuk Bintang (1x), Sawi Putih (1x), Mie Gepeng (1x), Jamur Enoki (2x), Air Mineral (1x)"
                );

                Intent intent = new Intent(ThirdTransaction.this, KeranjangActivity.class);
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void tambahKeKeranjang(String nama, String harga, String detail) throws JSONException {
        SharedPreferences prefs = getSharedPreferences("KeranjangPrefs", MODE_PRIVATE);
        String json = prefs.getString("keranjang", "[]");

        JSONArray array = new JSONArray(json);
        JSONObject item = new JSONObject();
        item.put("nama", nama);
        item.put("harga", harga);
        item.put("detail", detail);
        item.put("checked", false);

        array.put(item);
        prefs.edit().putString("keranjang", array.toString()).apply();
    }
}
