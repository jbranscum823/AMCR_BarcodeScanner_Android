package com.breezegamestudios.amcrbarcodescanner;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BarcodeScannerActivity extends AppCompatActivity {

    private ItemService itemService;
    private TextInputEditText editTextBarcode;
    private Button buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        itemService = ApiClient.getRetrofit().create(ItemService.class);

        editTextBarcode = findViewById(R.id.editTextBarcode);
        buttonSearch = findViewById(R.id.buttonSearch);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode = editTextBarcode.getText().toString();
                searchItemByBarcode(barcode);
            }
        });
    }

    private void searchItemByBarcode(String barcode) {
        Call<Item> call = itemService.getItemByBarcode(barcode);
        Log.d("BarcodeScannerActivity", "Searching item by barcode: " + barcode); // Add this log statement
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Log.d("BarcodeScannerActivity", "Response code: " + response.code()); // Add this log statement
                if (response.isSuccessful()) {
                    Item item = response.body();
                    if (item != null) {
                        // Handle the retrieved item
                        Log.d("BarcodeScannerActivity", "Item by Barcode: " + item);
                        Toast.makeText(BarcodeScannerActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();
                        // You can update your UI or perform any other actions with the item data here
                    } else {
                        // No matching item found
                        Toast.makeText(BarcodeScannerActivity.this, "No matching item found for barcode: " + barcode, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("BarcodeScannerActivity", "Failed to fetch item by barcode: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.e("BarcodeScannerActivity", "Error: " + t.getMessage());
            }
        });
    }
}
