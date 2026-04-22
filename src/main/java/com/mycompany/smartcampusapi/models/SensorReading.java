/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi.models;



import java.util.UUID;



public class SensorReading {

    private String id;        // Recommended as UUID [cite: 94]

    private long timestamp;   // Epoch time [cite: 90]

    private double value;     // The actual metric [cite: 92]



    public SensorReading() {

        this.id = UUID.randomUUID().toString();

        this.timestamp = System.currentTimeMillis();

    }



    public SensorReading(double value) {

        this();

        this.value = value;

    }



    // Getters and Setters

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public long getTimestamp() { return timestamp; }

    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public double getValue() { return value; }

    public void setValue(double value) { this.value = value; }

}