package com.x.rokhdelar.model;

import java.io.Serializable;

/**
 * Created by Rokhdelar on 2015/6/18.
 */
public class Device implements Serializable{
    private int deviceID;
    private String deviceName;
    private String deviceIP;
    private String deviceSNMPRO;
    private String deviceSNMPRW;
    private String deviceType;
    private String deviceModel;
    private String deviceBRAS;
    private String deviceBRASPort;
    private String deviceInternetVLAN;
    private int commRoomID;
    private String deviceMemo;

    @Override
    public String toString() {
        return deviceName+"--"+deviceIP;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceIP() {
        return deviceIP;
    }

    public void setDeviceIP(String deviceIP) {
        this.deviceIP = deviceIP;
    }

    public String getDeviceSNMPRO() {
        return deviceSNMPRO;
    }

    public void setDeviceSNMPRO(String deviceSNMPRO) {
        this.deviceSNMPRO = deviceSNMPRO;
    }

    public String getDeviceSNMPRW() {
        return deviceSNMPRW;
    }

    public void setDeviceSNMPRW(String deviceSNMPRW) {
        this.deviceSNMPRW = deviceSNMPRW;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceBRAS() {
        return deviceBRAS;
    }

    public void setDeviceBRAS(String deviceBRAS) {
        this.deviceBRAS = deviceBRAS;
    }

    public String getDeviceBRASPort() {
        return deviceBRASPort;
    }

    public void setDeviceBRASPort(String deviceBRASPort) {
        this.deviceBRASPort = deviceBRASPort;
    }

    public String getDeviceInternetVLAN() {
        return deviceInternetVLAN;
    }

    public void setDeviceInternetVLAN(String deviceInternetVLAN) {
        this.deviceInternetVLAN = deviceInternetVLAN;
    }

    public int getCommRoomID() {
        return commRoomID;
    }

    public void setCommRoomID(int commRoomID) {
        this.commRoomID = commRoomID;
    }

    public String getDeviceMemo() {
        return deviceMemo;
    }

    public void setDeviceMemo(String deviceMemo) {
        this.deviceMemo = deviceMemo;
    }
}
