package com.svkulikov.pushandnativec;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity
class MessageData {
    @PrimaryKey(autoGenerate = true)
    private int _id;
    private String toHash;
    private String hashValue;
    private long eventTime;

    public MessageData(String toHash, String hashValue) {
        this.toHash = toHash;
        this.hashValue = hashValue;
        this.eventTime = Calendar.getInstance().getTimeInMillis();
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }


    public String getToHash() {
        return toHash;
    }

    public void setToHash(String toHash) {
        this.toHash = toHash;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }
}
