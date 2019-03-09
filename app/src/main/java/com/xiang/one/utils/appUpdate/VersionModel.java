package com.xiang.one.utils.appUpdate;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;

public class VersionModel {


    /**
     * versionLog : sfdssfds
     * update : true
     * force : false
     * versionCode : 1.0.10
     * versionUrl : sffsa
     */




    @SerializedName("remark")
    private String versionLog;
//    private boolean update;
    @SerializedName("isUpdateInstall")
    private boolean force;
    @SerializedName("version")
    private String versionCode;
    @SerializedName("appUrl")
    private String versionUrl;

    public String getVersionLog() {
        return versionLog;
    }

    public void setVersionLog(String versionLog) {
        this.versionLog = versionLog;
    }

    public boolean isUpdate() {
       return !TextUtils.isEmpty(versionUrl);
    }

    /*
    public void setUpdate(boolean update) {
        this.update = update;
    }
    */

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }
}