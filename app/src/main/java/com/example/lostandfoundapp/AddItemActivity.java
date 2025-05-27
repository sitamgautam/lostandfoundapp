package com.example.lostandfoundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.*;

public class AddItemActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription;
    private Spinner spinnerStatus;
    private Button btnSave, btnGetLocation;
    private TextView textViewLocation;
    private DatabaseHelper databaseHelper;

    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0.0;
    private double longitude = 0.0;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnSave = findViewById(R.id.btnSave);
        btnGetLocation = findViewById(R.id.btnGetLocation);
        textViewLocation = findViewById(R.id.textViewLocation);

        databaseHelper = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        btnGetLocation.setOnClickListener(v -> getCurrentLocation());

        btnSave.setOnClickListener(view -> saveItem());
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setNumUpdates(1);

        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    Toast.makeText(AddItemActivity.this, "Lat: " + latitude + ", Lng: " + longitude, Toast.LENGTH_SHORT).show();
                    textViewLocation.setText("Location: " + latitude + ", " + longitude);
                } else {
                    Toast.makeText(AddItemActivity.this, "Still unable to get location", Toast.LENGTH_SHORT).show();
                }
            }
        }, getMainLooper());
    }

    private void saveItem() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString();

        if (name.isEmpty() || description.isEmpty() || status.isEmpty() || latitude == 0.0 || longitude == 0.0) {
            Toast.makeText(this, "Please fill all fields and get location", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean inserted = databaseHelper.insertItem(name, description, status, latitude, longitude);
        if (inserted) {
            Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
