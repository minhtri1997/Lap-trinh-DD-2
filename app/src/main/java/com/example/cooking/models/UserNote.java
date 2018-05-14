package com.example.cooking.models;

public class UserNote {
    String userID;
    String keyNote;

    public UserNote() {
    }

    public UserNote(String userName, String keyNote) {
        this.userID = userName;
        this.keyNote = keyNote;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getKeyNote() {
        return keyNote;
    }

    public void setKeyNote(String keyNote) {
        this.keyNote = keyNote;
    }
}
