package com.example.myplacesapp.myplacesapp.DataModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 1234485 on 3/25/2017.
 */

public class VenuesRespons {
    @SerializedName("meta")
    Meta meta;
    @SerializedName("notifications")
    List<Notification> notifications;
    @SerializedName("response")
    private
    Response response;


    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}

class Meta {
    @SerializedName("code")
    int code;
    @SerializedName("requestId")
    String requestId;
}

class Notification {
    @SerializedName("type")
    String type;
    @SerializedName("item")
    Item item;

}

class Item {
    int unreadCount;
}