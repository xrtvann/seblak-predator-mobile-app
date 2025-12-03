package com.irvan.seblakpredator.auth;

import android.app.Application;

import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

public class MidtransPayment extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Inisialisasi Midtrans UI Kit
        SdkUIFlowBuilder.init()
                .setClientKey("YOUR_CLIENT_KEY")
                .setContext(this)
                .setMerchantBaseUrl("https://YOUR_MERCHANT_BASE_URL/")
                .enableLog(true)
                .buildSDK();
    }
}
