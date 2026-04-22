/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi;

import com.mycompany.smartcampusapi.models.Room;
import com.mycompany.smartcampusapi.models.ErrorMessage;
import com.mycompany.smartcampusapi.exceptions.RoomNotEmptyException;
import com.mycompany.smartcampusapi.exceptions.DataNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private final DataStore dataStore = DataStore.getInstance();

    /**
     * Task 2.1: GET / - Provide a comprehensive list of all rooms.
     */
    @GET
    public List<Room> getAllRooms() {
        return new ArrayList<>(dataStore.getRooms().values());
    }

    /**
     * Task 2.1: POST / - Enable the creation of new rooms with validation.
     */
    @POST
    public Response addRoom(Room room) {
        // Validation: Ensure Room ID is provided
        if (room.getId() == null || room.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage("Room ID is required", 400))
                    .build();
        }

        // Check if room already exists
        if (dataStore.getRooms().containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorMessage("Room with ID " + room.getId() + " already exists", 409))
                    .build();
        }

        dataStore.getRooms().put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    /**
     * Task 2.1: GET /{roomId} - Fetch metadata for a specific room.
     */
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = dataStore.getRooms().get(roomId);
        if (room == null) {
            throw new DataNotFoundException("Room not found: " + roomId);
        }
        return Response.ok(room).build();
    }

    /**
     * Task 2.2: DELETE /{roomId} - Room Deletion & Safety Logic.
     */
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = dataStore.getRooms().get(roomId);
        
        // 1. Check if the room exists
        if (room == null) {
            throw new DataNotFoundException("Cannot delete: Room " + roomId + " does not exist.");
        }

        // 2. Safety Logic: Prevent data orphans (Part 5.1 Requirement)
        // Check if any sensors are currently assigned to this roomId
        boolean hasSensors = dataStore.getSensors().values().stream()
                .anyMatch(sensor -> roomId.equals(sensor.getRoomId()));

        if (hasSensors) {
            // This triggers your RoomNotEmptyExceptionMapper (HTTP 409)
            throw new RoomNotEmptyException("Room " + roomId + " cannot be deleted because it still contains active sensors.");
        }

        // 3. Perform Deletion
        dataStore.getRooms().remove(roomId);
        
        // Operation is successful (Return 204 No Content or 200 OK)
        return Response.noContent().build();
    }
}