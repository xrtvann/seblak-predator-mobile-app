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
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.irvan.seblakpredator.model.SelectedMenu;

import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;

public class TransaksiActivity extends AppCompatActivity {

    private Button addressChangeButton, addProductButton, checkOutButton, backButton, selectMethodPayment;
    private LinearLayout informationOrder;
    private TextView priceTotal, ongkirPrice, priceTotalProduct, displayOrderType, displayAddress, displayMethodPayment;
    private int ongkir = 0;
    private ArrayList<SelectedMenu> existingMenus = new ArrayList<>();
    private String address = "";
    private String orderType = "";
    private String userId = "";

    private String tax = "0";
    private String discount = "0";

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

    private final ActivityResultLauncher<Intent> changeAddressLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String newAddress = result.getData().getStringExtra("address");
                    if (newAddress != null && !newAddress.isEmpty()) {
                        address = newAddress;
                        updateDisplayOrderType();
                    }
                }
            }
    );
    private JSONArray buildItemsJson() {
        JSONArray itemsArray = new JSONArray();

        try {
            for (SelectedMenu menu : existingMenus) {
                JSONObject menuJson = new JSONObject();
                menuJson.put("name", menu.getNama());
                menuJson.put("level", menu.getLevel());
                menuJson.put("kuah", menu.getKuah());
                menuJson.put("telur", menu.getTelur());
                menuJson.put("kencur", menu.getKencur());
                menuJson.put("harga_level", menu.getHargaLevel());
                menuJson.put("harga_kuah", menu.getHargaKuah());
                menuJson.put("harga_telur", menu.getHargaTelur());
                menuJson.put("harga_kencur", menu.getHargaKencur());

                // Topping
                JSONArray toppingsArray = new JSONArray();
                for (SecondTransaction.SelectedTopping t : menu.getSelectedToppings()) {
                    JSONObject toppingJson = new JSONObject();
                    toppingJson.put("id", t.getId());
                    toppingJson.put("name", t.getName());
                    toppingJson.put("quantity", t.getQuantity());
                    toppingJson.put("price", t.getPrice());
                    toppingsArray.put(toppingJson);
                }

                menuJson.put("toppings", toppingsArray);
                itemsArray.put(menuJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemsArray;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        // Status bar transparan
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );

        // Inisialisasi view
        informationOrder = findViewById(R.id.InformationOrder);
        priceTotal = findViewById(R.id.priceTotal);
        ongkirPrice = findViewById(R.id.costDelivery);
        priceTotalProduct = findViewById(R.id.priceAllOrder);
        displayOrderType = findViewById(R.id.displayTypeOrder);
        displayAddress = findViewById(R.id.displayAddress);
        selectMethodPayment = findViewById(R.id.selectMethodPayment);
        displayMethodPayment = findViewById(R.id.displayMethodPayment);
        addressChangeButton = findViewById(R.id.addressChangeButton);
        addProductButton = findViewById(R.id.addMoreProduct);
        checkOutButton = findViewById(R.id.btn_checkout);
        backButton = findViewById(R.id.backButton);


        // Ambil data awal dari intent
        existingMenus = getIntent().getSerializableExtra("existingMenus") != null ?
                (ArrayList<SelectedMenu>) getIntent().getSerializableExtra("existingMenus") :
                new ArrayList<>();
        orderType = getIntent().getStringExtra("orderType");
        address = getIntent().getStringExtra("address");

        userId = getIntent().getStringExtra("user_id");
        if (userId == null || userId.isEmpty()) {
            // user_id wajib, jika tidak ada bisa hentikan activity
            Toast.makeText(this, "User tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        // Tampilkan tipe order dan alamat
        updateDisplayOrderType();

        // Tampilkan menu awal
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
            intent.putExtra("orderType", orderType);
            intent.putExtra("address", address);
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
            Intent intent = new Intent(TransaksiActivity.this, ChangeAddressActivity.class);
            changeAddressLauncher.launch(intent);
        });

        // Tombol back
        backButton.setOnClickListener(v -> showCanceled());

        // Pilih metode pembayaran
        selectMethodPayment.setOnClickListener(v -> {
            // Menampilkan popup metode pembayaran
            showPaymentMethodPopup();
        });
    }

    private void updateDisplayOrderType() {
        // Hanya menampilkan tipe order
        if (orderType != null && !orderType.isEmpty()) {
            displayOrderType.setText(orderType);
        } else {
            displayOrderType.setText("Tipe Order Tidak Diketahui");
        }

        // Alamat ditampilkan di displayAddress
        if (address != null && !address.isEmpty()) {
            displayAddress.setText(address);
        } else {
            displayAddress.setText("Alamat belum diisi");
        }
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

    private void showCanceled() {
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

    private void showPaymentMethodPopup() {
        // Tampilkan popup metode pembayaran yang sudah didefinisikan dalam popup_methodpayment.xml
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_methodpayment, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Mengatur tombol back untuk menutup popup
        TextView backButton = view.findViewById(R.id.backbutton);
        backButton.setOnClickListener(v -> dialog.dismiss());

        // Tombol konfirmasi pembayaran
        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> {
            // Lakukan konfirmasi metode pembayaran yang dipilih
            dialog.dismiss();
        });
    }

}
