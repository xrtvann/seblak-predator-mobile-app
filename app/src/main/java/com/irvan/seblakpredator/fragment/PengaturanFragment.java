package com.irvan.seblakpredator.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.irvan.seblakpredator.MainActivity;
import com.irvan.seblakpredator.ProfileActivity;
import com.irvan.seblakpredator.R;
import com.irvan.seblakpredator.auth.ChangePasswordActivity;
import com.irvan.seblakpredator.auth.LoginActivity;
import com.irvan.seblakpredator.model.TokenManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PengaturanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PengaturanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PengaturanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PengaturanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PengaturanFragment newInstance(String param1, String param2) {
        PengaturanFragment fragment = new PengaturanFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengaturan, container, false);
        ConstraintLayout profile, changePassword;
        profile = view.findViewById(R.id.akunButton);
        changePassword = view.findViewById(R.id.changePassword);
        profile.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ProfileActivity.class);
            startActivity(intent);
        });
        changePassword.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });
        View logout = view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(v ->
            showLogoutDialog());
        return view;

    }
    private void showLogoutDialog() {
        // Inflate custom layout
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.notification_logoutconfirm, null);

        // Buat dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        Animation anim = AnimationUtils.loadAnimation(requireContext(), R.anim.dialog_enter);
        view.startAnimation(anim);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Tombol OK
        Button btnOk = view.findViewById(R.id.okbutton);
        btnOk.setOnClickListener(v -> {dialog.dismiss();
            // 🔥 Hapus token login
            TokenManager.removeToken(requireContext());
            // Hapus semua data login
            requireContext().getSharedPreferences("MyAppPrefs", 0)
                    .edit()
                    .clear()
                    .apply();

        // Pindah ke halaman login
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);});
        // Tombol Batal
        Button btnBatal = view.findViewById(R.id.batalbutton);
        btnBatal.setOnClickListener(v -> dialog.dismiss());
    }

}