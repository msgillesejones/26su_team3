package com.team3.model;

public class ReservationRecord {
    private String spaceName;
    private String userName;
    private String date;
    private String startTime;
    private String endTime;

    public ReservationRecord(String spaceName, String userName, String date, String startTime, String endTime) {
        this.spaceName = spaceName;
        this.userName = userName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public boolean matchesUser(String userName) {
        return this.userName.equalsIgnoreCase(userName);
    }
}
