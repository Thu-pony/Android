package com.example.anyiassistor;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class DeviceBean implements Parcelable {
    private int id;
    private String codes;
    private Double lat;

    private Double lng;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private Double height;
    private int state;
    private int off;

    private int batt;

    private int bdm;

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getOff() {
        return off;
    }

    public void setOff(int off) {
        this.off = off;
    }

    public int getBatt() {
        return batt;
    }

    public void setBatt(int batt) {
        this.batt = batt;
    }

    public int getBdm() {
        return bdm;
    }

    public void setBdm(int bdm) {
        this.bdm = bdm;
    }

    public int getWifi() {
        return wifi;
    }

    public void setWifi(int wifi) {
        this.wifi = wifi;
    }

    private int wifi;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(codes);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeDouble(height);
        dest.writeInt(state);
        dest.writeInt(off);
        dest.writeInt(batt);
        dest.writeInt(bdm);
        dest.writeInt(wifi);
    }
    public DeviceBean() {};

    protected DeviceBean(Parcel in) {
        this.id = in.readInt();
        this.codes = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.height = in.readDouble();
        this.state = in.readInt();
        this.off = in.readInt();
        this.batt = in.readInt();
        this.bdm = in.readInt();
        this.wifi = in.readInt();

    }

    public static final Creator<DeviceBean> CREATOR = new Creator<DeviceBean>() {
        @Override
        public DeviceBean createFromParcel(Parcel in) {
            return new DeviceBean(in);
        }

        @Override
        public DeviceBean[] newArray(int size) {
            return new DeviceBean[size];
        }
    };
}
