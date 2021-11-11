package net.cherry.retrofit.entities;

import com.google.gson.annotations.SerializedName;

public class JoinDataInServer {
    @SerializedName("data")
    private JoinData data;
    @SerializedName("code")
    private String code;
    @SerializedName("message")
    private String message;

    public JoinData getData() {
        return data;
    }

    public void setData(JoinData data) {
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
