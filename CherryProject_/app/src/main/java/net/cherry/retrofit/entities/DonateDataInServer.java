package net.cherry.retrofit.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DonateDataInServer {
    @SerializedName("data")
    private DonateData data;
    @SerializedName("code")
    private String code;
    @SerializedName("message")
    private String message;

    public DonateData getData() {
        return data;
    }

    public void setData(DonateData data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
