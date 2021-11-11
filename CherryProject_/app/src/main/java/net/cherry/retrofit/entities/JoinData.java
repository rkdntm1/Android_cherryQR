package net.cherry.retrofit.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class JoinData {
    @SerializedName("userSn")
    private Long userSn;

    @SerializedName("userId")
    private String userId;

    @SerializedName("trmnlUno")
    private String trmnlUno;

    @SerializedName("userNm")
    private String userNm;

    @SerializedName("brthdy")
    private String brthdy;

    @SerializedName("frgnrYn")
    private String frgnrYn;

    @SerializedName("sexSeCode")
    private String sexSeCode;

    @SerializedName("userCi")
    private String userCi;

    @SerializedName("moblphonNo")
    private String moblphonNo;

    @SerializedName("emailAdres")
    private String emailAdres;

    @SerializedName("userSttusCode")
    private String userSttusCode;

    @SerializedName("useYn")
    private String useYn;

    @SerializedName("lastLoginDt")
    private Date lastLoginDt;

    @SerializedName("userSecsnDt")
    private Date userSecsnDt;

    public Long getUserSn() {
        return userSn;
    }

    public void setUserSn(Long userSn) {
        this.userSn = userSn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTrmnlUno() {
        return trmnlUno;
    }

    public void setTrmnlUno(String trmnlUno) {
        this.trmnlUno = trmnlUno;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getBrthdy() {
        return brthdy;
    }

    public void setBrthdy(String brthdy) {
        this.brthdy = brthdy;
    }

    public String getFrgnrYn() {
        return frgnrYn;
    }

    public void setFrgnrYn(String frgnrYn) {
        this.frgnrYn = frgnrYn;
    }

    public String getSexSeCode() {
        return sexSeCode;
    }

    public void setSexSeCode(String sexSeCode) {
        this.sexSeCode = sexSeCode;
    }

    public String getUserCi() {
        return userCi;
    }

    public void setUserCi(String userCi) {
        this.userCi = userCi;
    }

    public String getMoblphonNo() {
        return moblphonNo;
    }

    public void setMoblphonNo(String moblphonNo) {
        this.moblphonNo = moblphonNo;
    }

    public String getEmailAdres() {
        return emailAdres;
    }

    public void setEmailAdres(String emailAdres) {
        this.emailAdres = emailAdres;
    }

    public String getUserSttusCode() {
        return userSttusCode;
    }

    public void setUserSttusCode(String userSttusCode) {
        this.userSttusCode = userSttusCode;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public Date getLastLoginDt() {
        return lastLoginDt;
    }

    public void setLastLoginDt(Date lastLoginDt) {
        this.lastLoginDt = lastLoginDt;
    }

    public Date getUserSecsnDt() {
        return userSecsnDt;
    }

    public void setUserSecsnDt(Date userSecsnDt) {
        this.userSecsnDt = userSecsnDt;
    }
}
