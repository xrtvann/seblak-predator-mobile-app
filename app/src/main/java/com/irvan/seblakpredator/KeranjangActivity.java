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
        itemList.add(new CartItem("Ajeng Elsa", "Rp 15.000", "Seblak original pedas level 2", false));
        itemList.add(new CartItem("Fitrah", "Rp 18.000", "Seblak seafood level 3", false));
        itemList.add(new CartItem("Radit", "Rp 20.000", "Seblak komplit level 4", false));

        adapter = new CartAdapter(itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // ✅ Pilih semua item
        checkAll.setOnCheckedChangeListener((buttonView, isChecked) -> adapter.selectAll(isChecked));

        // ✅ Hapus item terpilih dengan konfirmasi
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
