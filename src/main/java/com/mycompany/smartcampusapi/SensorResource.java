/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi;

import com.mycompany.smartcampusapi.models.Sensor;
import com.mycompany.smartcampusapi.models.Room;
import com.mycompany.smartcampusapi.exceptions.LinkedResourceNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private final DataStore dataStore = DataStore.getInstance();

    @POST
    public Response registerSensor(Sensor sensor) {
        // 1. Validation: Does the Room exist? (Task 3.1 - Required for 422 Error)
        Room room = dataStore.getRooms().get(sensor.getRoomId());
        if (room == null) {
            throw new LinkedResourceNotFoundException("Cannot register sensor: Room ID " + sensor.getRoomId() + " does not exist.");
        }

        // 2. Save the sensor
        dataStore.getSensors().put(sensor.getId(), sensor);
        
        // 3. Update the Room's internal list of sensor IDs
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }
        if (!room.getSensorIds().contains(sensor.getId())) {
            room.getSensorIds().add(sensor.getId());
        }

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    @GET
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        // Task 3.2: Filtering logic using Java Streams
        if (type != null && !type.isEmpty()) {
            return dataStore.getSensors().values().stream()
                    .filter(s -> s.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(dataStore.getSensors().values());
    }

    /**
     * Requirement: Fetch metadata for a specific sensor.
     */
    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = dataStore.getSensors().get(sensorId);
        if (sensor == null) {
            throw new LinkedResourceNotFoundException("Sensor not found: " + sensorId);
        }
        return Response.ok(sensor).build();
    }

    /**
     * FIXED Sub-Resource Locator (Task 4.1)
     * We capture the sensorId from the path and pass it to the constructor.
     */
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        // Validation: Verify sensor exists before delegating
        if (!dataStore.getSensors().containsKey(sensorId)) {
            throw new LinkedResourceNotFoundException("Cannot access readings: Sensor " + sensorId + " does not exist.");
        }
        return new SensorReadingResource(sensorId);
    }
}
