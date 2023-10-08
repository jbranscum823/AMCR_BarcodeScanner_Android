package com.breezegamestudios.amcrbarcodescanner;
import java.util.List;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface LocationService {
    @GET("Location")
    Call<List<Location>> getAllLocations();

    @GET("Location/{id}")
    Call<Location> getLocationById(@Path("id") Integer id);

    @GET("Section/ByLocationId/{locationId}")
    Call<List<Section>> getSectionsByLocationId(@Path("locationId") Integer locationId);

    @GET("Location/Section/{id}")
    Call<Section> getSectionById(@Path("id") Integer id);

    @GET("Location/Subsection/BySectionId/{sectionId}")
    Call<List<Subsection>> getSubsectionsBySectionId(@Path("sectionId") Integer sectionId);

    @GET("Location/Subsection/{id}")
    Call<Subsection> getSubsectionById(@Path("id") Integer id);

    @GET("Location/Location/ByBarcode/{barcode}")
    Call<Location> getLocationByBarcode(@Path("barcode") String barcode);

    @GET("Location/Section/ByBarcode/{barcode}")
    Call<Section> getSectionByBarcode(@Path("barcode") String barcode);

    @GET("Location/Subsection/ByBarcode/{barcode}")
    Call<Subsection> getSubsectionByBarcode(@Path("barcode") String barcode);
}

