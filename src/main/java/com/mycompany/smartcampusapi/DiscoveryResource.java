package com.mycompany.smartcampusapi;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response; // Required import
import java.util.HashMap;
import java.util.Map;

@Path("/") // Root of the /api/v1 path
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscovery() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "Smart Campus API");
        response.put("version", "v1.0");
        response.put("admin", "nethmidivakara@gmail.com");

        // HATEOAS Links: Enhances discoverability (Part 1.2 requirement)
        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        
        response.put("links", links);
        
        // Wrapping the map in a Response object for standard compliance
        return Response.ok(response).build();
    }
}