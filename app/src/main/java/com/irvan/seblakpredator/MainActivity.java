package com.irvan.seblakpredator;

import android.os.Bundle;


import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.irvan.seblakpredator.fragment.DashboardFragment;
import com.irvan.seblakpredator.fragment.PengaturanFragment;
import com.irvan.seblakpredator.fragment.TransaksiFragment;



public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigation = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, new DashboardFragment())
                                .commit();


        bottomNavigation.setOnItemSelectedListener(menuItem -> {
            Fragment selectedFragment = null;

            int id = menuItem.getItemId();


            if (id == R.id.nav_dashboard){
                selectedFragment = new DashboardFragment();
            } else if (id == R.id.nav_transaction) {
                selectedFragment = new TransaksiFragment();
            } else if (id == R.id.nav_setting) {
                selectedFragment = new PengaturanFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, selectedFragment)
                        .commit();

            }
            return true;
        });


    }



}