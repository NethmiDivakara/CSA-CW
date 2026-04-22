package com.mycompany.smartcampusapi;

import com.mycompany.smartcampusapi.models.SensorReading;
import com.mycompany.smartcampusapi.models.Sensor;
import com.mycompany.smartcampusapi.exceptions.DataNotFoundException;
import com.mycompany.smartcampusapi.exceptions.SensorUnavailableException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final DataStore dataStore = DataStore.getInstance();
    private final String sensorId;

    // 1. FIXED: Constructor receives sensorId from the parent SensorResource
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @POST
    public Response addReading(SensorReading reading) {
        // 2. Validate sensor existence using the ID from the constructor
        Sensor sensor = dataStore.getSensors().get(sensorId);
        if (sensor == null) {
            throw new DataNotFoundException("Sensor not found: " + sensorId);
        }

        // 3. Maintenance check (Part 5.3)
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor " + sensorId + " is under maintenance.");
        }

        // 4. FIXED: Requirement for UUID and Timestamp generation
        reading.setId(UUID.randomUUID().toString());
        reading.setTimestamp(System.currentTimeMillis());

        // 5. Side Effect: Update parent sensor's current value (Part 4.2)
        sensor.setCurrentValue(reading.getValue());

        // 6. Save reading to history
        dataStore.getReadings().computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }

    @GET
    public List<SensorReading> getReadings() {
        // 7. FIXED: Pull readings using the ID from the constructor
        return dataStore.getReadings().getOrDefault(sensorId, new ArrayList<>());
    }
}