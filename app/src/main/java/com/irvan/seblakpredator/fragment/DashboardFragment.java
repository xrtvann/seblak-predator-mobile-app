package com.irvan.seblakpredator.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.irvan.seblakpredator.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        // 🔹 Ambil container dari XML
        LinearLayout productContainer = view.findViewById(R.id.productContainer);

        // 🔹 Data Dummy
        String[] namaProduk = {"Seblak Original", "Seblak Ceker", "Seblak Seafood"};
        String[] deskripsiProduk = {"Seblak pedas khas Bandung", "Seblak ceker empuk", "Seblak udang & cumi"};
        String[] hargaProduk = {"Rp10.000", "Rp12.000", "Rp15.000"};
        int[] gambarProduk = {R.drawable.seblak_suki, R.drawable.seblak_suki, R.drawable.seblak_suki};

        // 🔹 Bersihkan dulu (jaga-jaga kalau fragmen direload)
        productContainer.removeAllViews();

        // 🔹 Loop data dummy
        for (int i = 0; i < namaProduk.length; i++) {
            // Inflate item_product.xml
            View itemView = inflater.inflate(R.layout.item_product, productContainer, false);

            // Isi datanya
            ImageView img = itemView.findViewById(R.id.imageProduct);
            TextView nama = itemView.findViewById(R.id.nameProduct);
            TextView desc = itemView.findViewById(R.id.descriptionProduct);
            TextView harga = itemView.findViewById(R.id.price_product);

            img.setImageResource(gambarProduk[i]);
            nama.setText(namaProduk[i]);
            desc.setText(deskripsiProduk[i]);
            harga.setText(hargaProduk[i]);

            // Tambahkan ke container
            productContainer.addView(itemView);
        }

        return view;
    }
}