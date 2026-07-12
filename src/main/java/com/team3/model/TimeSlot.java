package com.team3.model;

public class TimeSlot {

	private String time;
    private String status;

    public TimeSlot(String time, String status) {
        this.time = time;
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}
