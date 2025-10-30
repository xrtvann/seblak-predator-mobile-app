package com.irvan.seblakpredator;
import com.irvan.seblakpredator.adapter.CartAdapter;
import com.irvan.seblakpredator.model.CartItem;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class KeranjangActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private CheckBox checkAll;
    private TextView btnDelete;
    private Button btnCheckout;
    private List<CartItem> itemList;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        recyclerView = findViewById(R.id.recyclerKeranjang);
        checkAll = findViewById(R.id.checkAll);
        btnDelete = findViewById(R.id.btnHapus);
        btnCheckout = findViewById(R.id.btnCheckout);

        // ✅ Dummy data sementara (simulasi pesanan dari beberapa orang)
        itemList = new ArrayList<>();
        itemList.add(new CartItem("Elsa", "Rp 15.000", "Kuah Normal, Telur Utuh, Kencur Normal, Gurih, Level 2, Sosis (1x), Tahu (2x), Mie Gepeng (1x), Pokcoy (1x), Jamur Kancing (2x), Air Mineral (1x)", false));
        itemList.add(new CartItem("Fitrah", "Rp 18.000", "Kuah Nyemek, Orak-Arik, Kencur Strong, Gurih Manis, Level 4, CrabStick (1x), Kerupuk Bintang (2x), Sawi Hijau (1x), Mie Keriting (1x), Jamur Enoki (2x), Es Teh Manis (1x)", false));
        itemList.add(new CartItem("Radit", "Rp 21.000", "Kuah Normal, Tanpa Telur, Kencur Normal, Gurih Asin, Level 3, Bakso Sapi (3x), Kulit Pangsit (1x), Sawi Putih (1x), Mie Gepeng (1x), Jagung Manis (2x), Air Mineral (1x)", false));
        itemList.add(new CartItem("Irvan", "Rp 10.000", "Kuah Nyemek, Utuh, Kencur Strong, Gurih Manis, Level 1, Ayam Suwir (2x), Kerupuk Udang (1x), Pokcoy (1x), Mie Keriting (1x), Jamur Enoki (1x), Air Mineral (1x)", false));
        itemList.add(new CartItem("Jeje", "Rp 8.000", "Kuah Normal, Orak-Arik, Kencur Normal, Gurih Asin, Level 5, CrabStick (1x), Bakso Ikan (2x), Sawi Hijau (1x), Mie Gepeng (1x), Jagung Manis (1x), Air Mineral (1x)", false));
        itemList.add(new CartItem("Wawa", "Rp 29.000", "Kuah Nyemek, Tanpa Telur, Kencur Strong, Gurih, Level 0, Kerupuk Bintang (2x), Tahu (2x), Sawi Putih (1x), Mie Keriting (1x), Jamur Kancing (3x), Air Mineral (1x)", false));
        itemList.add(new CartItem("Dian", "Rp 15.000", "Kuah Normal, Utuh, Kencur Normal, Gurih Manis, Level 3, Bakso Sapi (2x), Ayam Suwir (1x), Sawi Hijau (1x), Mie Gepeng (1x), Jamur Enoki (2x), Air Mineral (1x)", false));
        itemList.add(new CartItem("Surya", "Rp 18.000", "Kuah Nyemek, Orak-Arik, Kencur Strong, Gurih Asin, Level 4, CrabStick (2x), Kulit Pangsit (1x), Sawi Putih (1x), Mie Keriting (1x), Jamur Kancing (2x), Air Mineral (1x)", false));
        itemList.add(new CartItem("Adel", "Rp 14.000", "Kuah Normal, Tanpa Telur, Kencur Normal, Gurih, Level 1, Sosis (2x), Kerupuk Udang (1x), Pokcoy (1x), Mie Gepeng (1x), Jagung Manis (3x), Air Mineral (1x)", false));
        itemList.add(new CartItem("Fara", "Rp 11.000", "Kuah Nyemek, Utuh, Kencur Strong, Gurih Manis, Level 2, Bakso Ikan (2x), Kerupuk Bintang (1x), Sawi Hijau (1x), Mie Keriting (1x), Jamur Enoki (1x), Air Mineral (1x)", false));
        itemList.add(new CartItem("Zaidan", "Rp 13.000", "Kuah Normal, Orak-Arik, Kencur Normal, Gurih Asin, Level 0, Ayam Suwir (2x), Kerupuk Udang (1x), Sawi Putih (1x), Mie Gepeng (1x), Jamur Kancing (2x), Air Mineral (1x)", false));
        itemList.add(new CartItem("Anisa", "Rp 30.000", "Kuah Nyemek, Tanpa Telur, Kencur Strong, Gurih, Level 5, CrabStick (1x), Bakso Sapi (2x), Pokcoy (1x), Mie Keriting (1x), Jamur Enoki (3x), Air Mineral (1x)", false));

        adapter = new CartAdapter(itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        checkAll.setOnCheckedChangeListener((buttonView, isChecked) -> adapter.selectAll(isChecked));


        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah kamu yakin ingin menghapus item yang dipilih?")
                    .setPositiveButton("Ya", (dialog, which) -> adapter.removeSelected())
                    .setNegativeButton("Batal", null)
                    .show();
        });

        // ✅ Tombol Checkout (belum aktif)
        btnCheckout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Checkout")
                    .setMessage("Proses checkout akan tersedia setelah transaksi selesai.")
                    .setPositiveButton("OK", null)
                    .show();
        });
    }
}
