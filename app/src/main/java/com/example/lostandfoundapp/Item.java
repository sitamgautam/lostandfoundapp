package com.example.lostandfoundapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private int id;
    private String name;
    private String description;
    private String status;
    private double latitude;
    private double longitude;

    // Constructor with location
    public Item(int id, String name, String description, String status, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Parcelable constructor
    protected Item(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        status = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) { this.status = status; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(status);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
