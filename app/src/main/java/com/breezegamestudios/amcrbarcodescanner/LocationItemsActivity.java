package com.breezegamestudios.amcrbarcodescanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationItemsActivity extends AppCompatActivity {

    private int locationId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_items);

        String barcode = getIntent().getStringExtra("BARCODE");
        sharedPreferences = getSharedPreferences("LocationItemsActivity", MODE_PRIVATE);
        locationId = getIntent().getIntExtra("LOCATIONID", -1);

        // If locationId is not available in the intent, get it from shared preferences
        if (locationId == -1) {
            locationId = sharedPreferences.getInt("LOCATIONID", -1);
        }

        Log.d("LocationId: ", String.valueOf(locationId));
        // Fetch the items for this location/section/subsection
        // This will depend on how your API is set up
        ItemService itemService = ApiClient.getRetrofit().create(ItemService.class);;
        // Step 1: Search for Item by Barcode
        Call<List<Item>> itemCall = itemService.getAllItems();
        itemCall.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                Log.d("BarcodeScannerActivity", "Response code for item search: " + response.code());
                if (response.isSuccessful()) {
                    List<Item> items = response.body();
                    List<Item> filteredItems = items.stream()
                            .filter(item -> (locationId == item.getLocationId()))
                            .collect(Collectors.toList());


                    Log.d("Items Found: ", items.toString());
                    Log.d("Filtered Items: " , filteredItems.toString());
                    // Display the items in the RecyclerView
                    RecyclerView recyclerView = findViewById(R.id.recycler_view_location_items);
                    recyclerView.setLayoutManager(new LinearLayoutManager(LocationItemsActivity.this));
                    ItemAdapter adapter = new ItemAdapter(filteredItems);
                    recyclerView.setAdapter(adapter);

                    // Set an item click listener on the adapter
                    adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Item item) {
                            // Handle the item click
                            // For example, start a new activity to display the item's details
                            Intent intent = new Intent(LocationItemsActivity.this, EditItemActivityFromLocation.class);
                            intent.putExtra("BARCODE", item.getBarcode());
                            startActivity(intent);
                        }
                    });
                } else {
                    Log.d("BarcodeScannerActivity", "Items not found");                    // Step 1 failed, proceed to Step 2: Search for Location
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e("BarcodeScannerActivity", "Error searching item: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save the locationId in shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("LOCATIONID", locationId);
        editor.apply();
    }
}
