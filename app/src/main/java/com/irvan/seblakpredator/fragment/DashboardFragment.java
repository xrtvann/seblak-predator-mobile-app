package com.irvan.seblakpredator.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.irvan.seblakpredator.FirstTransaction;
import com.irvan.seblakpredator.MainActivity;
import com.irvan.seblakpredator.ProfileActivity;
import com.irvan.seblakpredator.R;

public class DashboardFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // SwipeRefreshLayout
        SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.swipeRefreshLayout);

        LinearLayout productContainer = view.findViewById(R.id.productContainer);
        LinearLayout riwayatButton = view.findViewById(R.id.btnRiwayat);
        ConstraintLayout profilePage = view.findViewById(R.id.information);
        ConstraintLayout dineInBtn = view.findViewById(R.id.dineinOptionButton);
        ConstraintLayout deliveryBtn = view.findViewById(R.id.deliveryOptionButton);


// Klik Dine In -> ke FirstTransaction dengan order_type = "Dine In"
        dineInBtn.setOnClickListener(v -> {
            // Reset data sebelum masuk FirstTransaction
            SharedPreferences prefs = requireActivity()
                    .getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("restoreData", false); // reset data
            editor.apply();

            Intent intent = new Intent(requireActivity(), FirstTransaction.class);
            intent.putExtra("order_type", "Dine In");
            startActivity(intent);
        });

// Klik Delivery -> ke FirstTransaction dengan order_type = "Delivery"
        deliveryBtn.setOnClickListener(v -> {
            // Reset data sebelum masuk FirstTransaction
            SharedPreferences prefs = requireActivity()
                    .getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("restoreData", false); // reset data
            editor.apply();

            Intent intent = new Intent(requireActivity(), FirstTransaction.class);
            intent.putExtra("order_type", "Delivery");
            startActivity(intent);
        });


        profilePage.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ProfileActivity.class);
            startActivity(intent);
        });
        riwayatButton.setOnClickListener(v -> {
            if(getActivity() instanceof MainActivity) {
                ((MainActivity)getActivity()).riwayatOfDashboard();
            }
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, new RiwayatFragment()) // ganti fragmentContainer sesuai id container di activity
                    .addToBackStack(null) // supaya bisa back ke dashboard
                    .commit();
        });

        // Dummy data produk
        String[] namaProduk = {"Seblak Original", "Seblak Ceker", "Seblak Seafood"};
        String[] deskripsiProduk = {"Seblak pedas khas Bandung", "Seblak ceker empuk", "Seblak udang & cumi"};
        String[] hargaProduk = {"Rp10.000", "Rp12.000", "Rp15.000"};
        int[] gambarProduk = {R.drawable.seblak_suki, R.drawable.seblak_suki, R.drawable.seblak_suki};

        // === FUNCTION REFRESH (untuk dipanggil saat swipe & saat awal tampil) ===
        Runnable refreshDashboard = () -> {

            // 🔄 Refresh greeting pengguna
            SharedPreferences sharedPreferences = requireActivity()
                    .getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
            String name = sharedPreferences.getString("name", "User");

            String displayName = name;
            int maxLength = 12;
            if (name.length() > maxLength) {
                displayName = name.substring(0, maxLength) + "...";
            }

            TextView person = view.findViewById(R.id.personGreeting);
            person.setText("Halo, " + displayName);

            // 🔄 Refresh list produk
            productContainer.removeAllViews();

            for (int i = 0; i < namaProduk.length; i++) {
                View itemView = inflater.inflate(R.layout.item_product, productContainer, false);

                ImageView img = itemView.findViewById(R.id.imageProduct);
                TextView nama = itemView.findViewById(R.id.nameProduct);
                TextView desc = itemView.findViewById(R.id.descriptionProduct);
                TextView harga = itemView.findViewById(R.id.price_product);

                img.setImageResource(gambarProduk[i]);
                nama.setText(namaProduk[i]);
                desc.setText(deskripsiProduk[i]);
                harga.setText(hargaProduk[i]);

                productContainer.addView(itemView);
            }
        };

        // Panggil refresh pertama kali (saat fragment dibuka)
        refreshDashboard.run();

        // === IMPLEMENTASI SWIPE REFRESH ===
        swipeRefresh.setOnRefreshListener(() -> {

            refreshDashboard.run(); // refresh data dashboard

            swipeRefresh.setRefreshing(false); // stop loading
        });

        return view;
    }
}
