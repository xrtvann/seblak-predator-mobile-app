package com.irvan.seblakpredator.apiclient;
import com.irvan.seblakpredator.model.BasicResponse;
import com.irvan.seblakpredator.model.ChangePasswordRequest;
import com.irvan.seblakpredator.model.ChangePasswordResponse;
import com.irvan.seblakpredator.model.CreateOrderRequest;
import com.irvan.seblakpredator.model.CreateOrderResponse;
import com.irvan.seblakpredator.model.CustomizationOption;
import com.irvan.seblakpredator.model.CustomizationResponse;
import com.irvan.seblakpredator.model.ForgetPassRequest;
import com.irvan.seblakpredator.model.ForgetPassResponse;
import com.irvan.seblakpredator.model.GetOrderResponse;
import com.irvan.seblakpredator.model.LoginRequest;
import com.irvan.seblakpredator.model.LoginResponse;
import com.irvan.seblakpredator.model.OrderRequest;
import com.irvan.seblakpredator.model.OrderResponse;
import com.irvan.seblakpredator.model.ProfileRequest;
import com.irvan.seblakpredator.model.ProfileResponse;
import com.irvan.seblakpredator.model.RegisterRequest;
import com.irvan.seblakpredator.model.RegisterResponse;
import com.irvan.seblakpredator.model.SecondMenuResponse;
import com.irvan.seblakpredator.model.SnapRequest;
import com.irvan.seblakpredator.model.SnapResponse;
import com.irvan.seblakpredator.model.SpiceLevelResponse;
import com.irvan.seblakpredator.model.UpdateProfileRequest;
import com.irvan.seblakpredator.model.UpdateProfileResponse;
import com.irvan.seblakpredator.model.UpdateStatusRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("auth/login.php")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @Headers("Content-Type: application/json")
    @POST("auth/register.php")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @Headers("Content-Type: application/json")
    @POST("auth/forgot-password.php")
    Call<ForgetPassResponse> forgetpassword(@Body ForgetPassRequest forgetPassRequest);

    @POST("auth/updateprofilemobile.php")
    Call<UpdateProfileResponse> updateProfile(
            @Header("Authorization") String token,
            @Body UpdateProfileRequest request
    );
    @GET("auth/profilemobile.php")
    Call<ProfileResponse> getProfile(
            @Header("Authorization") String token
    );


    @GET("menu/toppings.php")
    Call<SecondMenuResponse> getToppings();
    // ===============================================
    // 🔥 BAGIAN BARU — API TRANSAKSI (orders.php)
    // ===============================================

    // 1. POST – Create Order
    @Headers("Content-Type: application/json")
    @POST("orders/orders.php")
    Call<CreateOrderResponse> createOrder(@Body CreateOrderRequest request);

    // 2. GET – Get All Orders
    @GET("orders/orders.php")
    Call<GetOrderResponse> getOrders();

    // 3. GET – Get Order by ID
    @GET("orders/orders.php")
    Call<CreateOrderResponse> getOrderById(@Query("id") String id);

    // 4. PATCH – Update Order Status / Payment
    @Headers("Content-Type: application/json")
    @PATCH("orders/orders.php")
    Call<BasicResponse> updateOrderStatus(@Body UpdateStatusRequest request);

    // 5. DELETE – Delete Order
    @DELETE("orders/orders.php")
    Call<BasicResponse> deleteOrder(@Query("id") String id);
    @GET("customization-options.php")
    Call<CustomizationResponse> getCustomizationOptions();
    @GET("spice-levels.php")
    Call<SpiceLevelResponse> getAllSpiceLevels();
    @POST("mobile/create-order.php")
    Call<OrderResponse> createOrder(@Body OrderRequest body);
    @Headers("Content-Type: application/json")
    @POST("auth/change-password.php")  // Pastikan ini adalah endpoint yang benar
    Call<ChangePasswordResponse> changePassword (@Body ChangePasswordRequest request);

    @POST("midtrans/get_snap_token.php")
    Call<SnapResponse> getSnapToken(@Body SnapRequest request);

}
