package com.example.myplacesapp.myplacesapp.DataModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 1234485 on 3/25/2017.
 */
public class Contact implements Parcelable {
    @SerializedName("phone")
    private
    String phone;
    @SerializedName("formattedPhone")
    private
    String formattedPhone;
    @SerializedName("facebook")
    private
    String facebook;
    @SerializedName("facebookUsername")
    private
    String facebookUsername;
    @SerializedName("facebookName")
    private
    String facebookName;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFormattedPhone() {
        return formattedPhone;
    }

    public void setFormattedPhone(String formattedPhone) {
        this.formattedPhone = formattedPhone;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getFacebookUsername() {
        return facebookUsername;
    }

    public void setFacebookUsername(String facebookUsername) {
        this.facebookUsername = facebookUsername;
    }

    public String getFacebookName() {
        return facebookName;
    }

    public void setFacebookName(String facebookName) {
        this.facebookName = facebookName;
    }

    protected Contact(Parcel in) {
        phone = in.readString();
        formattedPhone = in.readString();
        facebook = in.readString();
        facebookUsername = in.readString();
        facebookName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phone);
        dest.writeString(formattedPhone);
        dest.writeString(facebook);
        dest.writeString(facebookUsername);
        dest.writeString(facebookName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}