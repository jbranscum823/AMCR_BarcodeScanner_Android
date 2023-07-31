package com.breezegamestudios.amcrbarcodescanner;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

    private TextView textViewBarcodeValue;
    private TextView textViewNameValue;
    private TextView textViewDescriptionValue;
    private TextView textViewPartNumberValue;
    private TextView textViewSerialNumberValue;
    private TextView textViewRepairOrderNumberValue;
    private TextView textViewLocationValue;
    private TextView textViewSectionValue;
    private TextView textViewSubsectionValue;
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
        textViewLocationValue = findViewById(R.id.textViewLocationValue);
        textViewSectionValue = findViewById(R.id.textViewSectionValue);
        textViewSubsectionValue = findViewById(R.id.textViewSubsectionValue);
        textViewCustomerValue = findViewById(R.id.textViewCustomerValue);
        textViewParentItemValue = findViewById(R.id.textViewParentItemValue);
        // Initialize other views for the remaining fields

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

                                        textViewLocationValue.setText(location.getName());
                                        textViewSectionValue.setText(sectionName);
                                        textViewSubsectionValue.setText(subsectionName);
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
}
