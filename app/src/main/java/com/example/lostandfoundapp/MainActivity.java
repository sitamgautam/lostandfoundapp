package com.example.lostandfoundapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private ArrayList<Item> itemList;
    private DatabaseHelper databaseHelper;
    private Button btnAddItem, btnShowOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnShowOnMap = findViewById(R.id.btnShowOnMap);  // new button to open map

        databaseHelper = new DatabaseHelper(this);
        itemList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter(this, itemList);
        recyclerView.setAdapter(itemAdapter);

        loadItemsFromDatabase();

        btnAddItem.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
            startActivity(intent);
        });

        btnShowOnMap.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            intent.putParcelableArrayListExtra("itemList", itemList);
            startActivity(intent);
        });
    }

    private void loadItemsFromDatabase() {
        itemList.clear();
        Cursor cursor = databaseHelper.getAllItems();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));

                Item item = new Item(id, name, description, status, latitude, longitude);
                itemList.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItemsFromDatabase();
    }
}
