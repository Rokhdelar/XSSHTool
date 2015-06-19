package com.x.rokhdelar.model;

/**
 * Created by Rokhdelar on 2015/6/18.
 */
public class SubStation {
    @Override
    public String toString() {
        return subName;
    }

    public int getSubID() {
        return subID;
    }

    public void setSubID(int subID) {
        this.subID = subID;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubLeader() {
        return subLeader;
    }

    public void setSubLeader(String subLeader) {
        this.subLeader = subLeader;
    }

    public String getSubPhone() {
        return subPhone;
    }

    public void setSubPhone(String subPhone) {
        this.subPhone = subPhone;
    }

    public String getSubMemo() {
        return subMemo;
    }

    public void setSubMemo(String subMemo) {
        this.subMemo = subMemo;
    }

    int subID;
    String subName;
    String subLeader;
    String subPhone;
    String subMemo;
}
