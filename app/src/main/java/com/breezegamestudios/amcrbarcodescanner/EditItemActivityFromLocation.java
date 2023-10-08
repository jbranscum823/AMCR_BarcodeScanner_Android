package com.breezegamestudios.amcrbarcodescanner;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditItemActivityFromLocation extends AppCompatActivity {

    //location variables from item
    private Integer locationId;
    private Integer sectionId;
    private Integer subsectionId;
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

        // Get the barcode from the Intent extras
        String barcode = getIntent().getStringExtra("BARCODE");

        // Initialize Update Button
        Button buttonSaveItem = findViewById(R.id.buttonSaveItem);
        buttonSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click Register","Yes");
                updateItem();

            }
        });

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

                        LocationService locationService = ApiClient.getRetrofit().create(LocationService.class);
                        Call<List<Location>> allLocationsCall = locationService.getAllLocations();
                        allLocationsCall.enqueue(new Callback<List<Location>>() {
                            @Override
                            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                                if (response.isSuccessful()) {
                                    List<Location> locations = response.body();
                                    ArrayAdapter<Location> locationAdapter = new ArrayAdapter<>(EditItemActivityFromLocation.this, android.R.layout.simple_spinner_item, locations); // Corrected here
                                    spinnerLocation.setAdapter(locationAdapter);

                                    int locationPosition = 0; // default to first position if not found or sectionId is null
                                    if (locationId != null) {
                                        locationPosition = getLocationPosition(locations, locationId);
                                    }
                                    spinnerLocation.setSelection(locationPosition);


                                    spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            Location selectedLocation = (Location) parent.getItemAtPosition(position);
                                            updateSectionsSpinner(selectedLocation);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                            // Do nothing here
                                        }
                                    });
                                }
                            }
                            private void updateSectionsSpinner(Location selectedLocation) {
                                List<Section> sections = selectedLocation.getSections();
                                ArrayAdapter<Section> sectionAdapter = new ArrayAdapter<>(EditItemActivityFromLocation.this, android.R.layout.simple_spinner_item, sections); // Corrected here
                                spinnerSection.setAdapter(sectionAdapter);

                                int sectionPosition = 0; // default to first position if not found or sectionId is null
                                if (sectionId != null) {
                                    sectionPosition = getSectionPosition(sections, sectionId);
                                }

                                spinnerSection.setSelection(sectionPosition);
                                spinnerSubsection.setAdapter(null); // Clear the spinner
                                spinnerSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        Section selectedSection = (Section) parent.getItemAtPosition(position);
                                        updateSubsectionsSpinner(selectedSection);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        // Do nothing here
                                    }
                                });
                            }
                            private void updateSubsectionsSpinner(Section selectedSection) {
                                List<Subsection> subsections = selectedSection.getSubsections();
                                if (subsections == null || subsections.isEmpty()) {
                                    spinnerSubsection.setAdapter(null); // Clear the spinner
                                    return; // Exit the method early
                                }
                                ArrayAdapter<Subsection> subsectionAdapter = new ArrayAdapter<>(EditItemActivityFromLocation.this, android.R.layout.simple_spinner_item, subsections); // Corrected here
                                spinnerSubsection.setAdapter(subsectionAdapter);
                                int subsectionPosition = 0; // default to first position if not found or sectionId is null
                                if (subsectionId != null) {
                                    subsectionPosition = getSubsectionPosition(subsections, subsectionId);
                                }

                                spinnerSubsection.setSelection(subsectionPosition);

                            }
                            @Override
                            public void onFailure(Call<List<Location>> call, Throwable t) {
                                // Handle failure
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                // Handle API call failure here (e.g., show an error message)
            }
        });
    }

    private int getLocationPosition(List<Location> locations, int locationId) {
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).getId() == locationId) {
                return i;
            }
        }
        return 0; // default to first position if not found
    }

    private int getSectionPosition(List<Section> sections, int sectionId) {
        for (int i = 0; i < sections.size(); i++) {
            if (sections.get(i).getId() == sectionId) {
                return i;
            }
        }
        return 0; // default to first position if not found
    }

    private int getSubsectionPosition(List<Subsection> subsections, int subsectionId) {
        for (int i = 0; i < subsections.size(); i++) {
            if (subsections.get(i).getId() == subsectionId) {
                return i;
            }
        }
        return 0; // default to first position if not found
    }


    private void updateItem() {
        Log.d("Button Works","YES!");
        String updatedName = textViewRepairOrderNumberValue.getText().toString();
        Log.d("Update Request", "Updating item with repairOrderNumberValue: " + updatedName);
        // Create an Item object with the updated data
        //Item updatedItem = new Item();
        originalItem.setRepairOrderNumber(updatedName);
        // ... set other updated fields ...
        Location selectedLocation = (Location) spinnerLocation.getSelectedItem();
        Section selectedSection = (Section) spinnerSection.getSelectedItem();
        Subsection selectedSubsection = (Subsection) spinnerSubsection.getSelectedItem();

        if(selectedLocation.getId() != null) {
            originalItem.setLocationId(selectedLocation.getId());
            Log.d("New Location ID:","" + selectedLocation.getId());
        }else {
            originalItem.setLocationId(null);
        }

        if (selectedSection != null) {
            originalItem.setSectionId(selectedSection.getId());
        } else {
            originalItem.setSectionId(null);
        }


        if (selectedSubsection != null) {
            originalItem.setSubsectionId(selectedSubsection.getId());
        } else {
            originalItem.setSubsectionId(null);
        }


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
