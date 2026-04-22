/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi;

import com.mycompany.smartcampusapi.models.Room;
import com.mycompany.smartcampusapi.models.Sensor;
import com.mycompany.smartcampusapi.models.SensorReading;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    // Singleton instance
    private static final DataStore instance = new DataStore();

    // Thread-safe maps to store data 
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    private final Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();
    
    private DataStore() {
        // sample data
        rooms.put("LIB-301", new Room("LIB-301", "Library Study", 10));
    }

    public static DataStore getInstance() {
        return instance;
    }

    // Getters for your resources to use
    public Map<String, Room> getRooms() { return rooms; }
    public Map<String, Sensor> getSensors() { return sensors; }
    public Map<String, List<SensorReading>> getReadings() { return readings; }
}