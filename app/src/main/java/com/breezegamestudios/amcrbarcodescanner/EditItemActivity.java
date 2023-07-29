package com.breezegamestudios.amcrbarcodescanner;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditItemActivity extends AppCompatActivity {

    private TextView textViewBarcodeValue;
    private TextView textViewNameValue;
    private TextView textViewDescriptionValue;
    private TextView textViewPartNumberValue;
    private TextView textViewSerialNumberValue;
    private TextView textViewRepairOrderNumberValue;
    private TextView textViewLocationValue;
    private TextView textViewSectionValue;
    private TextView textViewSubsectionValue;
    // Add other views for the remaining fields

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Initialize your views
        textViewBarcodeValue = findViewById(R.id.textViewBarcodeValue);
        textViewNameValue = findViewById(R.id.textViewNameValue);
        textViewDescriptionValue = findViewById(R.id.textViewDescriptionValue);
        textViewPartNumberValue = findViewById(R.id.textViewPartNumberValue);
        textViewSerialNumberValue = findViewById(R.id.textViewSerialNumberValue);
        textViewRepairOrderNumberValue = findViewById(R.id.textViewRepairOrderNumberValue);
        //textViewLocationValue = findViewById(R.id.textViewLocationValue);
        //textViewSectionValue = findViewById(R.id.textViewSectionValue);
        //textViewSubsectionValue = findViewById(R.id.textViewSubsectionValue);
        // Initialize other views for the remaining fields

        // Get the barcode from the Intent extras
        String barcode = getIntent().getStringExtra("barcode");

        // Make the API call to get the item data by barcode
        ItemService itemService = ApiClient.getRetrofit().create(ItemService.class);
        Call<Item> itemCall = itemService.getItemByBarcode(barcode);
        itemCall.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful()) {
                    Item item = response.body();
                    if (item != null) {
                        // Populate the fields with the item data
                        textViewBarcodeValue.setText(item.getBarcode());
                        textViewNameValue.setText(item.getName());
                        textViewDescriptionValue.setText(item.getDescription());
                        textViewPartNumberValue.setText(item.getPartNumber());
                        textViewSerialNumberValue.setText(item.getSerialNumber());
                        textViewRepairOrderNumberValue.setText(item.getRepairOrderNumber());
                        // Populate other views for the remaining fields

                        // For Location, Section, and Subsection, you may need to make additional API calls
                        // to get the actual names of the corresponding entities based on their IDs
                        // For example:
                        // textViewLocationValue.setText("Location Name");

                        // Note: You can add additional logic here to handle missing or null data
                        // and display appropriate default values or error messages.
                    }
                } else {
                    // Handle API call failure here (e.g., show an error message)
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                // Handle API call failure here (e.g., show an error message)
            }
        });
    }
}
