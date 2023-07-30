package com.breezegamestudios.amcrbarcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_items);

        String barcode = getIntent().getStringExtra("BARCODE");

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
                    Log.d("Items Found: ", items.toString());

                    // Display the items in the RecyclerView
                    RecyclerView recyclerView = findViewById(R.id.recycler_view_location_items);
                    recyclerView.setLayoutManager(new LinearLayoutManager(LocationItemsActivity.this));
                    ItemAdapter adapter = new ItemAdapter(items);
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
}
