package com.example.lostandfoundapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddItemActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription;
    private Spinner spinnerStatus;
    private Button btnSave;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnSave = findViewById(R.id.btnSave);

        databaseHelper = new DatabaseHelper(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });
    }

    private void saveItem() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString();

        if (name.isEmpty() || description.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean inserted = databaseHelper.insertItem(name, description, status);
        if (inserted) {
            Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
            finish(); // Go back to MainActivity
        } else {
            Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
        }
    }
}
