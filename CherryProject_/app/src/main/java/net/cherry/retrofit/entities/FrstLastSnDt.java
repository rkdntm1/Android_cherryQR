package net.cherry.retrofit.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class FrstLastSnDt {
    @SerializedName("frstRegistUserSn")
    private Long frstRegistUserSn;

    @SerializedName("frstRegistDt")
    private Date frstRegistDt;

    @SerializedName("lastChangeUserSn")
    private Long lastChangeUserSn;

    @SerializedName("lastChangeDt")
    private Date lastChangeDt;

    public Long getFrstRegistUserSn() {
        return frstRegistUserSn;
    }

    public void setFrstRegistUserSn(Long frstRegistUserSn) {
        this.frstRegistUserSn = frstRegistUserSn;
    }

    public Date getFrstRegistDt() {
        return frstRegistDt;
    }

    public void setFrstRegistDt(Date frstRegistDt) {
        this.frstRegistDt = frstRegistDt;
    }

    public Long getLastChangeUserSn() {
        return lastChangeUserSn;
    }

    public void setLastChangeUserSn(Long lastChangeUserSn) {
        this.lastChangeUserSn = lastChangeUserSn;
    }

    public Date getLastChangeDt() {
        return lastChangeDt;
    }

    public void setLastChangeDt(Date lastChangeDt) {
        this.lastChangeDt = lastChangeDt;
    }
}
