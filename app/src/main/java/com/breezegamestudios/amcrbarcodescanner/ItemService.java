package com.breezegamestudios.amcrbarcodescanner;

import java.util.List;

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
    Call<Item> updateItem(@Path("id") int id, @Body Item item);

    @DELETE("Item/{id}")
    Call<Void> deleteItem(@Path("id") int id);
}

