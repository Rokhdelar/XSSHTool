package com.x.rokhdelar.model;

/**
 * Created by Rokhdelar on 2015/6/18.
 */
public class CommRoom {
    private int commRoomID;
    private String commRoomName;
    private int subID;
    private String commRoomAddress;
    private String commRoomContact;
    private String commRoomPhone;
    private String commRoomMemo;

    @Override
    public String toString() {
        return commRoomName;
    }

    public int getCommRoomID() {
        return commRoomID;
    }

    public void setCommRoomID(int commRoomID) {
        this.commRoomID = commRoomID;
    }

    public String getCommRoomName() {
        return commRoomName;
    }

    public void setCommRoomName(String commRoomName) {
        this.commRoomName = commRoomName;
    }

    public int getSubID() {
        return subID;
    }

    public void setSubID(int subID) {
        this.subID = subID;
    }

    public String getCommRoomAddress() {
        return commRoomAddress;
    }

    public void setCommRoomAddress(String commRoomAddress) {
        this.commRoomAddress = commRoomAddress;
    }

    public String getCommRoomContact() {
        return commRoomContact;
    }

    public void setCommRoomContact(String commRoomContact) {
        this.commRoomContact = commRoomContact;
    }

    public String getCommRoomPhone() {
        return commRoomPhone;
    }

    public void setCommRoomPhone(String commRoomPhone) {
        this.commRoomPhone = commRoomPhone;
    }

    public String getCommRoomMemo() {
        return commRoomMemo;
    }

    public void setCommRoomMemo(String commRoomMemo) {
        this.commRoomMemo = commRoomMemo;
    }
}
