package com.breezegamestudios.amcrbarcodescanner;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ItemService {
    @GET("Item")
    Call<List<Item>> getAllItems();

    @GET("Item/{id}")
    Call<Item> getItemById(@Path("id") int id);

    @GET("Item/ByBarcode/{barcode}")
    Call<Item> getItemByBarcode(@Path("barcode") String barcode);

    @POST("Item")
    Call<Void> addItem(@Body Item item);

    @PUT("Item/{id}")
    Call<ResponseBody> updateItem(@Path("id") int id, @Body Item item);

    @DELETE("Item/{id}")
    Call<Void> deleteItem(@Path("id") int id);
}

