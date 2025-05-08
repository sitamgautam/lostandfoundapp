package com.example.lostandfoundapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ItemDetailActivity extends AppCompatActivity {

    private TextView textViewTitleDetail, textViewDescriptionDetail, textViewStatusDetail;
    private Button buttonDelete;
    private DatabaseHelper databaseHelper;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        textViewTitleDetail = findViewById(R.id.textViewTitleDetail);
        textViewDescriptionDetail = findViewById(R.id.textViewDescriptionDetail);
        textViewStatusDetail = findViewById(R.id.textViewStatusDetail);
        buttonDelete = findViewById(R.id.buttonDelete);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("itemId")) {
            itemId = intent.getIntExtra("itemId", -1);
            loadItemDetails(itemId);
        }

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(itemId);
            }
        });
    }

    private void loadItemDetails(int id) {
        Cursor cursor = databaseHelper.getAllItems();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int currentId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                if (currentId == id) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

                    textViewTitleDetail.setText(name);
                    textViewDescriptionDetail.setText(description);
                    textViewStatusDetail.setText(status);
                    break;
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void deleteItem(int id) {
        boolean deleted = databaseHelper.deleteItem(id);
        if (deleted) {
            Toast.makeText(this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
            finish(); // go back to MainActivity
        } else {
            Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show();
        }
    }
}
