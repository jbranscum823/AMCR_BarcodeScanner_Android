package com.breezegamestudios.amcrbarcodescanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemLookupActivity extends AppCompatActivity {

    private ItemAdapter adapter; // Declare adapter here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_items);

            // Initialize the RecyclerView and set an empty adapter
        RecyclerView recyclerView = findViewById(R.id.recycler_view_location_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(ItemLookupActivity.this));
        adapter = new ItemAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // User pressed the search button
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // User changed the text in the SearchView
                // Filter your list here and update your adapter
                Log.d("LocationItemsActivity", "Search text: " + newText); // Add this line
                adapter.filter(newText);
                return false;
            }

        });


        // Fetch the items for this location/section/subsection
        // This will depend on how your API is set up
        ItemService itemService = ApiClient.getRetrofit().create(ItemService.class);;
        // Step 1: Search for Item by Barcode
        Call<List<Item>> itemCall = itemService.getAllItems();
        itemCall.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful()) {
                    List<Item> items = response.body();
                    List<Item> filteredItems = new ArrayList<>(items);
                    Log.d("Items Found: ", items.toString());

                    // Display the items in the RecyclerView
                    adapter.setItems(filteredItems);
                    adapter.notifyDataSetChanged();

                    // Set an item click listener on the adapter
                    adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Item item) {
                            // Handle the item click
                            // For example, start a new activity to display the item's details
                            Intent intent = new Intent(ItemLookupActivity.this, EditItemActivityFromLocation.class);
                            intent.putExtra("BARCODE", item.getBarcode());
                            startActivity(intent);
                        }
                    });
                } else {
                    Log.d("ItemLookupActivity", "No items found");                    // Step 1 failed, proceed to Step 2: Search for Location
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e("ItemLookupActivity", "Error searching item: " + t.getMessage());
            }
        });
    }
}
