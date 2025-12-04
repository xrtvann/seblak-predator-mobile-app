package com.irvan.seblakpredator;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.irvan.seblakpredator.fragment.DashboardFragment;
import com.irvan.seblakpredator.fragment.PengaturanFragment;
import com.irvan.seblakpredator.fragment.RiwayatFragment;
import com.irvan.seblakpredator.fragment.TransaksiFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    int lastPosition = 0; // posisi terakhir tombol

    // urutan tombol (kiri → kanan)
    Map<Integer, Integer> navOrder = new HashMap<Integer, Integer>() {{
        put(R.id.nav_home, 0);
        put(R.id.nav_transaction, 1);
        put(R.id.nav_history, 2);
        put(R.id.nav_setting, 3);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, new DashboardFragment())
                .commit();


        bottomNavigation.setOnItemSelectedListener(menuItem -> {
            Fragment selectedFragment = null;
            int id = menuItem.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new DashboardFragment();
            } else if (id == R.id.nav_transaction) {
                selectedFragment = new TransaksiFragment();
            } else if (id == R.id.nav_history) {
                // Saat klik Riwayat, langsung tampilkan fragment RiwayatFragment (list utama)
                selectedFragment = new RiwayatFragment();
            } else if (id == R.id.nav_setting) {
                selectedFragment = new PengaturanFragment();
            }

            if (selectedFragment != null) {
                int newPosition = navOrder.get(id);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if (newPosition > lastPosition) {
                    // geser ke kanan (next)
                    transaction.setCustomAnimations(
                            R.anim.slide_in_right, // masuk dari kanan
                            R.anim.slide_out_left  // keluar ke kiri
                    );
                } else if (newPosition < lastPosition) {
                    // geser ke kiri (back)
                    transaction.setCustomAnimations(
                            R.anim.slide_in_left,  // masuk dari kiri
                            R.anim.slide_out_right // keluar ke kanan
                    );
                }

                transaction.replace(R.id.frame_container, selectedFragment).commit();
                lastPosition = newPosition;
            }

            return true;
        });
    }

    // ✅ Tambahan: method untuk membuka halaman detail RiwayatPesananFragment
    public void bukaRiwayatPesanan() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, new RiwayatFragment())
                .addToBackStack(null) // supaya bisa tombol back
                .commit();
    }
    public void riwayatOfDashboard() {
        // ganti fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, new RiwayatFragment())
                .addToBackStack(null)
                .commit();

        // set menu bottom navigation aktif
        bottomNavigation.setSelectedItemId(R.id.nav_history);
    }
    public void showExitConfirmationDialog() {
        Log.d("BackPressed", "showExitConfirmationDialog called");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.notification_closeapp, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnOk = view.findViewById(R.id.okbutton);
        btnOk.setOnClickListener(v -> {
            Log.d("BackPressed", "Exit confirmed, finishing app");
            dialog.dismiss();
            finishAffinity();
        });

        Button btnBatal = view.findViewById(R.id.batalbutton);
        btnBatal.setOnClickListener(v -> {
            Log.d("BackPressed", "Exit cancelled");
            dialog.dismiss();
        });
    }

}
