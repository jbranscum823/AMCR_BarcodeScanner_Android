package com.breezegamestudios.amcrbarcodescanner;

import android.content.Intent;
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
    private LocationService locationService;

    private TextInputEditText editTextBarcode;
    private Button buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        itemService = ApiClient.getRetrofit().create(ItemService.class);
        locationService = ApiClient.getRetrofit().create(LocationService.class);

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
        Log.d("BarcodeScannerActivity", "Searching item by barcode: " + barcode);

        // Step 1: Search for Item by Barcode
        Call<Item> itemCall = itemService.getItemByBarcode(barcode);
        itemCall.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Log.d("BarcodeScannerActivity", "Response code for item search: " + response.code());
                if (response.isSuccessful()) {
                    Item item = response.body();
                    if (item != null) {
                        // Item found
                        Log.d("BarcodeScannerActivity", "Item by Barcode: " + item);
                        Toast.makeText(BarcodeScannerActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();
                        // You can update your UI or perform any other actions with the item data here
                        Intent intent = new Intent(BarcodeScannerActivity.this, EditItemActivity.class);
                        // Pass any data you need to the EditItemActivity using extras (optional)
                        intent.putExtra("BARCODE", barcode);
                        startActivity(intent);
                    } else {
                        // No matching item found, proceed to Step 2: Search for Location
                        searchLocationByBarcode(barcode);
                    }
                } else {
                    Log.d("BarcodeScannerActivity", "Item not found for barcode: " + barcode);                    // Step 1 failed, proceed to Step 2: Search for Location
                    searchLocationByBarcode(barcode);
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.e("BarcodeScannerActivity", "Error searching item: " + t.getMessage());
                // Step 1 failed, proceed to Step 2: Search for Location
                searchLocationByBarcode(barcode);
            }
        });
    }

    private void searchLocationByBarcode(String barcode) {
        // Step 2: Search for Location by Barcode
        Call<Location> locationCall = locationService.getLocationByBarcode(barcode);
        locationCall.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                Log.d("BarcodeScannerActivity", "Response code for location search: " + response.code());
                if (response.isSuccessful()) {
                    Location location = response.body();
                    if (location != null) {
                        // Location found
                        Log.d("BarcodeScannerActivity", "Location by Barcode: " + location);
                        Toast.makeText(BarcodeScannerActivity.this, "Location: " + location, Toast.LENGTH_SHORT).show();
                        // You can update your UI or perform any other actions with the location data here

                        Intent intent = new Intent(BarcodeScannerActivity.this, LocationItemsActivity.class);
                        intent.putExtra("BARCODE", barcode);
                        intent.putExtra("LOCATIONID", location.getId());
                        startActivity(intent);

                    } else {
                        // No matching location found, proceed to Step 3: Search for Section
                        searchSectionByBarcode(barcode);
                    }
                } else {
                    Log.d("BarcodeScannerActivity", "Location not found for barcode: " + barcode);                    // Step 2 failed, proceed to Step 3: Search for Section
                    // Step 2 failed, proceed to Step 3: Search for Section
                    searchSectionByBarcode(barcode);
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                Log.e("BarcodeScannerActivity", "Error searching location: " + t.getMessage());
                // Step 2 failed, proceed to Step 3: Search for Section
                searchSectionByBarcode(barcode);
            }
        });
    }

    private void searchSectionByBarcode(String barcode) {
        // Step 3: Search for Section by Barcode
        Call<Section> sectionCall = locationService.getSectionByBarcode(barcode);
        sectionCall.enqueue(new Callback<Section>() {
            @Override
            public void onResponse(Call<Section> call, Response<Section> response) {
                Log.d("BarcodeScannerActivity", "Response code for section search: " + response.code());
                if (response.isSuccessful()) {
                    Section section = response.body();
                    if (section != null) {
                        // Section found
                        Log.d("BarcodeScannerActivity", "Section by Barcode: " + section);
                        Toast.makeText(BarcodeScannerActivity.this, "Section: " + section, Toast.LENGTH_SHORT).show();
                        // You can update your UI or perform any other actions with the section data here
                    } else {
                        // No matching section found, proceed to Step 4: Search for Subsection
                        searchSubsectionByBarcode(barcode);
                    }
                } else {
                    Log.d("BarcodeScannerActivity", "Section not found for barcode: " + barcode);                    // Step 3 failed, proceed to Step 4: Search for Subsection
                    // Step 3 failed, proceed to Step 4: Search for Subsection
                    searchSubsectionByBarcode(barcode);
                }
            }

            @Override
            public void onFailure(Call<Section> call, Throwable t) {
                Log.e("BarcodeScannerActivity", "Error searching section: " + t.getMessage());
                // Step 3 failed, proceed to Step 4: Search for Subsection
                searchSubsectionByBarcode(barcode);
            }
        });
    }

    private void searchSubsectionByBarcode(String barcode) {
        // Step 4: Search for Subsection by Barcode
        Call<Subsection> subsectionCall = locationService.getSubsectionByBarcode(barcode);
        subsectionCall.enqueue(new Callback<Subsection>() {
            @Override
            public void onResponse(Call<Subsection> call, Response<Subsection> response) {
                Log.d("BarcodeScannerActivity", "Response code for subsection search: " + response.code());
                if (response.isSuccessful()) {
                    Subsection subsection = response.body();
                    if (subsection != null) {
                        // Subsection found
                        Log.d("BarcodeScannerActivity", "Subsection by Barcode: " + subsection);
                        Toast.makeText(BarcodeScannerActivity.this, "Subsection: " + subsection, Toast.LENGTH_SHORT).show();
                        // You can update your UI or perform any other actions with the subsection data here
                    } else {
                        // No matching subsection found
                        Log.d("BarcodeScannerActivity", "No matching subsection found for barcode: " + barcode);
                    }
                } else {
                    Log.d("BarcodeScannerActivity", "Subsection not found for barcode: " + barcode);                    // Step 4 failed
                }

                // Final data found for barcode (even if subsection is not found)
                Log.d("BarcodeScannerActivity", "Final data found for barcode: " + barcode);
                // Do whatever you want with the final data here
            }

            @Override
            public void onFailure(Call<Subsection> call, Throwable t) {
                Log.e("BarcodeScannerActivity", "Error searching subsection: " + t.getMessage());
                // Final data found for barcode (even if subsection search failed)
                Log.d("BarcodeScannerActivity", "Final data found for barcode: " + barcode);
                // Do whatever you want with the final data here
            }
        });
    }
}
