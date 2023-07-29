package com.breezegamestudios.amcrbarcodescanner;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface CustomerService {
    @GET("Customer")
    Call<List<Customer>> getAllCustomers();

    @GET("Customer/{id}")
    Call<Customer> getCustomerById(@Path("id") int id);
}
