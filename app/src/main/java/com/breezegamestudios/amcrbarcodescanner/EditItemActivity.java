package com.breezegamestudios.amcrbarcodescanner;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditItemActivity extends AppCompatActivity {

    //location variables from item
    private int locationId;
    private int sectionId;
    private int subsectionId;
    String sectionName = null;
    String subsectionName = null;

    private Item originalItem;

    private TextView textViewBarcodeValue;
    private TextView textViewNameValue;
    private TextView textViewDescriptionValue;
    private TextView textViewPartNumberValue;
    private TextView textViewSerialNumberValue;
    private EditText textViewRepairOrderNumberValue;
    private Spinner spinnerLocation;
    private Spinner spinnerSection;
    private Spinner spinnerSubsection;
    private TextView textViewCustomerValue;
    private TextView textViewParentItemValue;

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
        spinnerLocation = findViewById(R.id.spinnerLocation);
        spinnerSection = findViewById(R.id.spinnerSection);
        spinnerSubsection = findViewById(R.id.spinnerSubsection);
        textViewCustomerValue = findViewById(R.id.textViewCustomerValue);
        textViewParentItemValue = findViewById(R.id.textViewParentItemValue);
        // Initialize other views for the remaining fields

        // Initialize Update Button
        Button buttonSaveItem = findViewById(R.id.buttonSaveItem);
        buttonSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.d("Click Register","Yes");
            updateItem();

            }
        });

        // Get the barcode from the Intent extras
        String barcode = getIntent().getStringExtra("BARCODE");

        // Make the API call to get the item data by barcode
        ItemService itemService = ApiClient.getRetrofit().create(ItemService.class);
        Call<Item> itemCall = itemService.getItemByBarcode(barcode);
        itemCall.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Log.d("Item Call: ",itemCall.toString());
                if (response.isSuccessful()) {
                    Item item = response.body();
                    originalItem = item;
                    if (item != null) {

                        //set location variables from item data
                        locationId = item.getLocationId();
                        sectionId = item.getSectionId();
                        subsectionId = item.getSubsectionId();

                        // Populate the fields with the item data
                        textViewBarcodeValue.setText(item.getBarcode());
                        textViewNameValue.setText(item.getName());
                        textViewDescriptionValue.setText(item.getDescription());
                        textViewPartNumberValue.setText(item.getPartNumber());
                        textViewSerialNumberValue.setText(item.getSerialNumber());
                        textViewRepairOrderNumberValue.setText(item.getRepairOrderNumber());
                        // Populate other views for the remaining fields

                        // Fetch Customer data from API based on the item data
                        if(item.getParentItemId() != null) {
                            // Make the API call to get the item data by barcode
                            ItemService parentItemService = ApiClient.getRetrofit().create(ItemService.class);
                            Call<Item> parentItemCall = parentItemService.getItemById(item.getParentItemId());
                            parentItemCall.enqueue(new Callback<Item>() {
                                @Override
                                public void onResponse(Call<Item> call, Response<Item> parentItemResponse) {
                                    int responseCode = parentItemResponse.code();
                                    Log.d("Location Response Code: ", String.valueOf(responseCode));
                                    Log.d("Location API URL: ", parentItemCall.toString());

                                    if (parentItemResponse.isSuccessful()) {
                                        Item parentItem = parentItemResponse.body();
                                        if (parentItem != null) {
                                            textViewParentItemValue.setText(parentItem.getName());
                                            Log.d("Parent Item: ", parentItem.getName());
                                        } else {

                                        }
                                    } else {
                                        // Handle unsuccessful response for Location
                                        //locationTextView.setText("Location: Error");
                                        //sectionTextView.setText("Section: Error");
                                        //subsectionTextView.setText("Subsection: Error");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Item> call, Throwable t) {
                                    // Handle failure to fetch Location data
                                    //locationTextView.setText("Location: Error");
                                    //sectionTextView.setText("Section: Error");
                                    //subsectionTextView.setText("Subsection: Error");
                                }
                            });
                        }

                        // Fetch Customer data from API based on the item data
                        if(item.getCustomerId() != null) {
                            CustomerService customerService = ApiClient.getRetrofit().create(CustomerService.class);
                            Call<Customer> customerCall = customerService.getCustomerById(item.getCustomerId());
                            customerCall.enqueue(new Callback<Customer>() {
                                @Override
                                public void onResponse(Call<Customer> call, Response<Customer> customerResponse) {
                                    int responseCode = customerResponse.code();
                                    Log.d("Location Response Code: ", String.valueOf(responseCode));
                                    Log.d("Location API URL: ", customerCall.toString());

                                    if (customerResponse.isSuccessful()) {
                                        Customer customer = customerResponse.body();
                                        if (customer != null) {
                                            textViewCustomerValue.setText(customer.companyName);
                                            Log.d("Customer ID: ", customer.id.toString());
                                            Log.d("Customer Name: ", customer.companyName);
                                        } else {

                                        }
                                    } else {
                                        // Handle unsuccessful response for Location
                                        //locationTextView.setText("Location: Error");
                                        //sectionTextView.setText("Section: Error");
                                        //subsectionTextView.setText("Subsection: Error");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Customer> call, Throwable t) {
                                    // Handle failure to fetch Location data
                                    //locationTextView.setText("Location: Error");
                                    //sectionTextView.setText("Section: Error");
                                    //subsectionTextView.setText("Subsection: Error");
                                }
                            });
                        }

                        //Location call to get location information
                        // Fetch Location data from API based on the barcode
                        LocationService locationService = ApiClient.getRetrofit().create(LocationService.class);
                        Call<Location> locationCall = locationService.getLocationById(locationId);
                        locationCall.enqueue(new Callback<Location>() {
                            @Override
                            public void onResponse(Call<Location> call, Response<Location> locationResponse) {
                                int responseCode = locationResponse.code();
                                Log.d("Location Response Code: ", String.valueOf(responseCode));
                                String locationApiUrl = ApiClient.getRetrofit().baseUrl() + "Location/" + locationId;
                                Log.d("Location API URL: ", locationCall.toString());

                                if (locationResponse.isSuccessful()) {
                                    Location location = locationResponse.body();
                                    if (location != null) {
                                        // Set the Location name in the TextView
                                        //textViewLocation.setText("Location: " + location.getName());
                                        Log.d("Location Data: ",location.toString());
                                        // Fetch Section data based on the retrieved Location's sectionId

                                        // Iterate through the Sections to find the one with the matching sectionId
                                        for (Section section : location.getSections()) {
                                            if (section.getId() == sectionId) {
                                                sectionName = section.getName();
                                                break;
                                            }
                                        }

                                        // If the Section is found, find the Subsection with the matching subsectionId
                                        if (sectionName != null) {
                                            for (Section section : location.getSections()) {
                                                if (section.getId() == sectionId) {
                                                    // Iterate through the subsections to find the one with the matching subsectionId
                                                    for (Subsection subsection : section.getSubsections()) {
                                                        if (subsection.getId() == subsectionId) {
                                                            subsectionName = subsection.getName();
                                                            break;
                                                        }
                                                    }
                                                    break;
                                                }
                                            }
                                        }

/*                                        textViewLocationValue.setText(location.getName());
                                        textViewSectionValue.setText(sectionName);
                                        textViewSubsectionValue.setText(subsectionName);*/
                                        // Now you have the Section name and Subsection name based on the item's sectionId and subsectionId
                                        if (sectionName != null && subsectionName != null) {
                                            Log.d("Section Name: ", sectionName);
                                            Log.d("Subsection Name: ", subsectionName);
                                        } else {
                                            Log.d("Section or Subsection not found.", "");
                                        }

                                    } else {
                                        Log.d("Location: Not Found","");
                                        Log.d("Section: Not Found","");
                                        Log.d("Subsection: Not Found","");
                                    }
                                } else {
                                    // Handle unsuccessful response for Location
                                    //locationTextView.setText("Location: Error");
                                    //sectionTextView.setText("Section: Error");
                                    //subsectionTextView.setText("Subsection: Error");
                                }
                            }

                            @Override
                            public void onFailure(Call<Location> call, Throwable t) {
                                // Handle failure to fetch Location data
                                //locationTextView.setText("Location: Error");
                                //sectionTextView.setText("Section: Error");
                                //subsectionTextView.setText("Subsection: Error");
                            }
                        });
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

    private void updateItem() {
        Log.d("Button Works","YES!");
        String updatedName = textViewRepairOrderNumberValue.getText().toString();
        Log.d("Update Request", "Updating item with repairOrderNumberValue: " + updatedName);
        // Create an Item object with the updated data
        //Item updatedItem = new Item();
        originalItem.setRepairOrderNumber(updatedName);
        // ... set other updated fields ...

        // Make a PUT request to update the item in the database
        ItemService itemService = ApiClient.getRetrofit().create(ItemService.class);
        Call<ResponseBody> updateCall = itemService.updateItem(originalItem.getId(), originalItem); // Initialize updateCall here
        updateCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("Response", "Success");
                } else {
                    Log.d("Response", "Fail");
                    // Log the response code and error body for more details
                    Log.d("Response Code", String.valueOf(response.code()));
                    try {
                        Log.d("Error Body", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("API Call", "Failure");
                Log.d("Error", t.getMessage());
            }
        });

    }
}
