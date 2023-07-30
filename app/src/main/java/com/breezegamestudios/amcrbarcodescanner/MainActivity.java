package com.breezegamestudios.amcrbarcodescanner;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ItemService itemService;
    private static final int PERMISSION_REQUEST_INTERNET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemService = ApiClient.getRetrofit().create(ItemService.class);


        Button buttonBarcodeScanner = findViewById(R.id.buttonBarcodeScanner);
        buttonBarcodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start BarcodeScannerActivity
                Intent intent = new Intent(MainActivity.this, BarcodeScannerActivity.class);
                startActivity(intent);
            }
        });

        Button buttonItemLookup = findViewById(R.id.buttonItemLookup);
        buttonItemLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ItemLookupActivity
                Intent intent = new Intent(MainActivity.this, ItemLookupActivity.class);
                startActivity(intent);
            }
        });

/*        Button buttonInspection = findViewById(R.id.buttonInspection);
        buttonInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start InspectionActivity
                Intent intent = new Intent(MainActivity.this, InspectionActivity.class);
                startActivity(intent);
            }
        });*/

/*        // Only fetch all items
        getAllItems();
        getItemById(352);
        // Create an Intent to switch to BarcodeScannerActivity
        Intent intent = new Intent(MainActivity.this, BarcodeScannerActivity.class);
        startActivity(intent);*/
    }

    private void getAllItems() {
        Call<List<Item>> call = itemService.getAllItems();
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful()) {
                    List<Item> items = response.body();
                    Log.d("MainActivity", "All Items: " + items);
                } else {
                    Log.e("MainActivity", "Failed to fetch items: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e("MainActivity", "Error: " + t.getMessage());
            }
        });
    }
    private void getItemById(int itemId) {
        Call<Item> call = itemService.getItemById(itemId);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful()) {
                    Item item = response.body();
                    if (item != null) {
                        // Access and print the attributes separately
                        Log.d("MainActivity", "Item ID: " + item.getId());
                        Log.d("MainActivity", "Item Name: " + item.getName());
                        Log.d("MainActivity", "Item Price: " + item.getPrice());
                        Log.d("MainActivity", "Item Description: " + item.getDescription());
                        Log.d("MainActivity", "Item Barcode: " + item.getBarcode());
                        Log.d("MainActivity", "Item Due Date: " + item.getDueDate());
                        Log.d("MainActivity", "Item Receive Date: " + item.getReceiveDate());
                        Log.d("MainActivity", "Item Shipping Date: " + item.getShippingDate());
                        Log.d("MainActivity", "Item Location ID: " + item.getLocationId());
                        Log.d("MainActivity", "Item Section ID: " + item.getSectionId());
                        Log.d("MainActivity", "Item Subsection ID: " + item.getSubsectionId());
                        Log.d("MainActivity", "Item Job History: " + item.getJobHistory());
                        Log.d("MainActivity", "Item Part Number: " + item.getPartNumber());
                        Log.d("MainActivity", "Item Serial Number: " + item.getSerialNumber());
                        Log.d("MainActivity", "Item Repair Order Number: " + item.getRepairOrderNumber());
                        Log.d("MainActivity", "Item Quantity: " + item.getQuantity());
                        Log.d("MainActivity", "Item Customer ID: " + item.getCustomerId());
                        Log.d("MainActivity", "Item Parent Item ID: " + item.getParentItemId());
                    }
                } else {
                    Log.e("MainActivity", "Failed to fetch item by ID: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.e("MainActivity", "Error: " + t.getMessage());
            }
        });
    }
}
