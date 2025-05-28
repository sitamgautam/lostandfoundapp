package com.example.lostandfoundapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Item> itemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        itemList = getIntent().getParcelableArrayListExtra("itemList");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (itemList != null && !itemList.isEmpty()) {
            for (Item item : itemList) {
                LatLng position = new LatLng(item.getLatitude(), item.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(item.getName())
                        .snippet(item.getDescription()));
            }

            // Move camera to the first item
            LatLng firstLocation = new LatLng(itemList.get(0).getLatitude(), itemList.get(0).getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 12));
        }
    }
}
