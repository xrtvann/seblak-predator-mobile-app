package com.irvan.seblakpredator.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.irvan.seblakpredator.R;

public class TransaksiFragment extends Fragment {

    private ConstraintLayout processingCategory, doneCategory;
    private ConstraintLayout barcolor1, barcolor2;

    public TransaksiFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_transaksi, container, false);

        // inisialisasi view
        processingCategory = view.findViewById(R.id.processingCategory);
        doneCategory = view.findViewById(R.id.doneCategory);

        barcolor1 = view.findViewById(R.id.barcolor1);
        barcolor2 = view.findViewById(R.id.barcolor2);

        // set listener untuk klik
        processingCategory.setOnClickListener(v -> {
            // tampilkan bar pertama, sembunyikan bar kedua
            barcolor1.setVisibility(View.VISIBLE);
            barcolor2.setVisibility(View.INVISIBLE);
        });

        doneCategory.setOnClickListener(v -> {
            // tampilkan bar kedua, sembunyikan bar pertama
            barcolor1.setVisibility(View.INVISIBLE);
            barcolor2.setVisibility(View.VISIBLE);
        });

        // default: bar pertama aktif
        barcolor1.setVisibility(View.VISIBLE);
        barcolor2.setVisibility(View.INVISIBLE);

        return view;
    }
}
