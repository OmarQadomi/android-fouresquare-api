package com.example.myplacesapp.myplacesapp.DataModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class VenueLocation implements Parcelable {
    @SerializedName("address")
    private
    String address;
    @SerializedName("crossStreet")
    private
    String street;
    @SerializedName("lat")
    private
    String latitued;
    @SerializedName("lng")
    private
    String longitude;
    @SerializedName("distance")
    private
    String distance;
    @SerializedName("cc")
    private
    String cc;
    @SerializedName("city")
    private
    String city;
    @SerializedName("state")
    private
    String state;
    @SerializedName("country")
    private
    String country;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLatitued() {
        return latitued;
    }

    public void setLatitued(String latitued) {
        this.latitued = latitued;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    protected VenueLocation(Parcel in) {
        address = in.readString();
        street = in.readString();
        latitued = in.readString();
        longitude = in.readString();
        distance = in.readString();
        cc = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(street);
        dest.writeString(latitued);
        dest.writeString(longitude);
        dest.writeString(distance);
        dest.writeString(cc);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VenueLocation> CREATOR = new Parcelable.Creator<VenueLocation>() {
        @Override
        public VenueLocation createFromParcel(Parcel in) {
            return new VenueLocation(in);
        }

        @Override
        public VenueLocation[] newArray(int size) {
            return new VenueLocation[size];
        }
    };
}