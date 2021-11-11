package net.cherry.retrofit.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class DonateData extends FrstLastSnDt implements Serializable {
    @SerializedName("userSetleHstSn")
    private Long userSetleHstSn;

    @SerializedName("userSn")
    private Long userSn;

    @SerializedName("mrhstId")
    private String mrhstId;

    @SerializedName("chypayTransferId")
    private String chypayTransferId;

    @SerializedName("setleDt")
    private Date setleDt;

    @SerializedName("setleAmt")
    private Long setleAmt;

    @SerializedName("setleSttusCode")
    private String setleSttusCode;

    @SerializedName("setleErrorCn")
    private String setleErrorCn;

    @SerializedName("mrhstNm")
    private String mrhstNm;

    @SerializedName("mrhstDc")
    private String mrhstDc;

    @SerializedName("userNm")
    private String userNm;

    @SerializedName("cprNm")
    private String cprNm;

    @SerializedName("bizrno")
    private String bizrno;

    @SerializedName("zipAdres")
    private String zipAdres;

    public Long getUserSetleHstSn() {
        return userSetleHstSn;
    }

    public void setUserSetleHstSn(Long userSetleHstSn) {
        this.userSetleHstSn = userSetleHstSn;
    }

    public Long getUserSn() {
        return userSn;
    }

    public void setUserSn(Long userSn) {
        this.userSn = userSn;
    }

    public String getMrhstId() {
        return mrhstId;
    }

    public void setMrhstId(String mrhstId) {
        this.mrhstId = mrhstId;
    }

    public String getChypayTransferId() {
        return chypayTransferId;
    }

    public void setChypayTransferId(String chypayTransferId) {
        this.chypayTransferId = chypayTransferId;
    }

    public Date getSetleDt() {
        return setleDt;
    }

    public void setSetleDt(Date setleDt) {
        this.setleDt = setleDt;
    }

    public Long getSetleAmt() {
        return setleAmt;
    }

    public void setSetleAmt(Long setleAmt) {
        this.setleAmt = setleAmt;
    }

    public String getSetleSttusCode() {
        return setleSttusCode;
    }

    public void setSetleSttusCode(String setleSttusCode) {
        this.setleSttusCode = setleSttusCode;
    }

    public String getSetleErrorCn() {
        return setleErrorCn;
    }

    public void setSetleErrorCn(String setleErrorCn) {
        this.setleErrorCn = setleErrorCn;
    }

    public String getMrhstNm() {
        return mrhstNm;
    }

    public void setMrhstNm(String mrhstNm) {
        this.mrhstNm = mrhstNm;
    }

    public String getMrhstDc() {
        return mrhstDc;
    }

    public void setMrhstDc(String mrhstDc) {
        this.mrhstDc = mrhstDc;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getCprNm() {
        return cprNm;
    }

    public void setCprNm(String cprNm) {
        this.cprNm = cprNm;
    }

    public String getBizrno() {
        return bizrno;
    }

    public void setBizrno(String bizrno) {
        this.bizrno = bizrno;
    }

    public String getZipAdres() {
        return zipAdres;
    }

    public void setZipAdres(String zipAdres) {
        this.zipAdres = zipAdres;
    }
}
