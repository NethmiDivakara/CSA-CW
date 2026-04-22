/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi.models;

public class Sensor {
    private String id;        // Unique identifier, e.g., "TEMP-001" [cite: 71, 76]
    private String type;      // e.g., "Temperature", "CO2" [cite: 72, 73, 77]
    private String status;    // "ACTIVE", "MAINTENANCE", or "OFFLINE" [cite: 74, 75, 78]
    private double currentValue; // Most recent measurement [cite: 79]
    private String roomId;    // Link to the Room ID [cite: 79, 80]

    // Required for JAX-RS JSON processing [cite: 82]
    public Sensor() {}

    public Sensor(String id, String type, String status, String roomId) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.roomId = roomId;
    }

    // Getters and Setters [cite: 42, 82]
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getCurrentValue() { return currentValue; }
    public void setCurrentValue(double currentValue) { this.currentValue = currentValue; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
}