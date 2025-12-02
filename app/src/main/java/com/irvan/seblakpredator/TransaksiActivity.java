package com.irvan.seblakpredator;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.irvan.seblakpredator.model.SelectedMenu;

import java.util.ArrayList;

public class TransaksiActivity extends AppCompatActivity {

    private Button addressChangeButton, addProductButton, checkOutButton, backButton;
    private LinearLayout informationOrder;
    private TextView priceTotal, ongkirPrice, priceTotalProduct, displayOrderType;
    private int ongkir = 0;
    private ArrayList<SelectedMenu> existingMenus = new ArrayList<>();

    private final ActivityResultLauncher<Intent> addMenuLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    SelectedMenu newMenu = (SelectedMenu) result.getData().getSerializableExtra("newMenu");
                    if (newMenu != null) {
                        existingMenus.add(newMenu);
                        displayAllMenus();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );

        informationOrder = findViewById(R.id.InformationOrder);
        priceTotal = findViewById(R.id.priceTotal);
        ongkirPrice = findViewById(R.id.costDelivery);
        priceTotalProduct = findViewById(R.id.priceAllOrder);
        displayOrderType = findViewById(R.id.displayTypeOrder);
        addressChangeButton = findViewById(R.id.addressChangeButton);
        addProductButton = findViewById(R.id.addMoreProduct);
        checkOutButton = findViewById(R.id.btn_checkout);
        backButton = findViewById(R.id.backButton);

        // Ambil list menu awal jika ada
        existingMenus = getIntent().getSerializableExtra("existingMenus") != null ?
                (ArrayList<SelectedMenu>) getIntent().getSerializableExtra("existingMenus") :
                new ArrayList<>();

        // Ambil tipe order dari FirstTransaction
        String orderType = getIntent().getStringExtra("orderType");
        if(orderType != null){
            displayOrderType.setText(orderType);
        } else {
            displayOrderType.setText("Tipe Order Tidak Diketahui");
        }

        // Tampilkan semua menu
        displayAllMenus();

        // Back gesture
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showCanceled();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        // Tambah menu
        addProductButton.setOnClickListener(v -> {
            Intent intent = new Intent(TransaksiActivity.this, SelectCustomization.class);
            intent.putExtra("existingMenus", existingMenus);
            addMenuLauncher.launch(intent);
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

        // Ganti alamat
        addressChangeButton.setOnClickListener(v -> {
            startActivity(new Intent(TransaksiActivity.this, ChangeAddressActivity.class));
        });

        // Tombol back
        backButton.setOnClickListener(v -> showCanceled());
    }

    private void displayAllMenus() {
        informationOrder.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        int totalAll = 0;
        int menuNumber = 1;

        for (SelectedMenu menu : existingMenus) {
            int hargaMenuUtama = menu.getHargaLevel() + menu.getHargaKuah() +
                    menu.getHargaTelur() + menu.getHargaKencur();

            View mainItem = inflater.inflate(R.layout.item_productorder, informationOrder, false);
            TextView desc = mainItem.findViewById(R.id.descProduct);
            TextView harga = mainItem.findViewById(R.id.priceProduct);

            String menuTitle = String.format("Seblak %02d", menuNumber);
            StringBuilder description = new StringBuilder();
            description.append(menuTitle).append("\n")
                    .append("Level Pedas: ").append(menu.getLevel()).append(" (Rp ").append(menu.getHargaLevel()).append(")\n")
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

            int subtotal = hargaMenuUtama;
            for (SecondTransaction.SelectedTopping t : menu.getSelectedToppings()) {
                subtotal += t.getPrice() * t.getQuantity();
            }
            totalAll += subtotal;
            menuNumber++;
        }

        priceTotalProduct.setText("Rp " + totalAll);
        priceTotal.setText("Rp " + (totalAll + ongkir));
        ongkirPrice.setText("Rp " + ongkir);
    }

    private void showCanceled(){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.notification_cancelorder, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnOk = view.findViewById(R.id.okbutton);
        btnOk.setOnClickListener(v -> {
            Intent intent = new Intent(TransaksiActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        Button btnBatal = view.findViewById(R.id.batalbutton);
        btnBatal.setOnClickListener(v -> dialog.dismiss());
    }
}
