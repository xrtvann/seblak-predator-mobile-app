package com.irvan.seblakpredator;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.irvan.seblakpredator.model.SelectedMenu;

import java.util.ArrayList;

public class TransaksiActivity extends AppCompatActivity {

    private Button addressChangeButton, addProductButton, checkOutButton, backButton;
    private LinearLayout informationOrder;
    private TextView priceTotal, ongkirPrice, priceTotalProduct;
    private int ongkir = 0;
    private ArrayList<SelectedMenu> existingMenus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        informationOrder = findViewById(R.id.InformationOrder);
        priceTotal = findViewById(R.id.priceTotal);
        ongkirPrice = findViewById(R.id.costDelivery);
        priceTotalProduct = findViewById(R.id.priceAllOrder);
        addressChangeButton = findViewById(R.id.addressChangeButton);
        addProductButton = findViewById(R.id.addMoreProduct);
        checkOutButton = findViewById(R.id.btn_checkout);
        backButton = findViewById(R.id.backButton);

        // Ambil list menu dari SecondTransaction (SUDAH SELESAI!)
        existingMenus = getIntent().getSerializableExtra("existingMenus") != null ?
                (ArrayList<SelectedMenu>) getIntent().getSerializableExtra("existingMenus") :
                new ArrayList<>();

        // Tampilkan semua menu
        displayAllMenus();

        // Tambah menu lain → kembali ke FirstTransaction
        addProductButton.setOnClickListener(v -> {
            Intent intent = new Intent(TransaksiActivity.this, FirstTransaction.class);
            intent.putExtra("existingMenus", existingMenus);
            startActivity(intent);
            finish();
        });

        // Checkout
        checkOutButton.setOnClickListener(v -> {
            int totalAll = 0;
            for (SelectedMenu menu : existingMenus) {
                int subtotal = menu.getHargaLevel() + menu.getHargaKuah() +
                        menu.getHargaTelur() + menu.getHargaKencur();

                for (SecondTransaction.SelectedTopping t : menu.getSelectedToppings()) {
                    subtotal += t.getPrice() * t.getQuantity();
                }
                totalAll += subtotal;
            }

            Intent checkoutIntent = new Intent(TransaksiActivity.this, SuccessOrder.class);
            checkoutIntent.putExtra("totalPrice", totalAll);
            startActivity(checkoutIntent);
        });

        addressChangeButton.setOnClickListener(v -> {
            Intent intent = new Intent(TransaksiActivity.this, ChangeAddressActivity.class);
            startActivity(intent);
        });
    }

    private void displayAllMenus() {
        informationOrder.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (SelectedMenu menu : existingMenus) {
            int hargaMenuUtama = menu.getHargaLevel() + menu.getHargaKuah() +
                    menu.getHargaTelur() + menu.getHargaKencur();

            View mainItem = inflater.inflate(R.layout.item_productorder, informationOrder, false);
            TextView desc = mainItem.findViewById(R.id.descProduct);
            TextView harga = mainItem.findViewById(R.id.priceProduct);

            StringBuilder description = new StringBuilder();
            description.append("Level Pedas: ").append(menu.getLevel()).append(" (Rp ").append(menu.getHargaLevel()).append(")\n")
                    .append("Kuah: ").append(menu.getKuah()).append(" (Rp ").append(menu.getHargaKuah()).append(")\n")
                    .append("Telur: ").append(menu.getTelur()).append(" (Rp ").append(menu.getHargaTelur()).append(")\n")
                    .append("Kencur: ").append(menu.getKencur()).append(" (Rp ").append(menu.getHargaKencur()).append(")");

            desc.setText(description.toString());
            harga.setText("Rp " + hargaMenuUtama);
            informationOrder.addView(mainItem);

            for (SecondTransaction.SelectedTopping s : menu.getSelectedToppings()) {
                View toppingItem = inflater.inflate(R.layout.item_topping, informationOrder, false);
                TextView toppingName = toppingItem.findViewById(R.id.toppingName);
                TextView toppingQtyPrice = toppingItem.findViewById(R.id.toppingQtyPrice);

                toppingName.setText(s.getName() + " x" + s.getQuantity());
                toppingQtyPrice.setText("Rp " + (s.getPrice() * s.getQuantity()));

                informationOrder.addView(toppingItem);
            }
        }

        int totalAll = 0;
        for (SelectedMenu menu : existingMenus) {
            int subtotal = menu.getHargaLevel() + menu.getHargaKuah() +
                    menu.getHargaTelur() + menu.getHargaKencur();

            for (SecondTransaction.SelectedTopping t : menu.getSelectedToppings()) {
                subtotal += t.getPrice() * t.getQuantity();
            }
            totalAll += subtotal;
        }

        priceTotalProduct.setText("Rp " + totalAll);
        priceTotal.setText("Rp " + (totalAll + ongkir));
        ongkirPrice.setText("Rp " + ongkir);
    }
}
