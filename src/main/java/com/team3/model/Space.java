package com.team3.model;

public class Space {
	private String name;
    private int capacity;
    private String features;

    public Space(String name, int capacity, String features) {
        this.name = name;
        this.capacity = capacity;
        this.features = features;
}
    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getFeatures() {
        return features;
    }
}    
