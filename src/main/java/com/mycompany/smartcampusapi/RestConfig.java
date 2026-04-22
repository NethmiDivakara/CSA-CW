package com.mycompany.smartcampusapi;

import com.mycompany.smartcampusapi.filters.LoggingFilter;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/v1")
public class RestConfig extends ResourceConfig {
    public RestConfig() {
        // 1. Automatically discovery all Resources in this package
        packages("com.mycompany.smartcampusapi");
        
        // 2. Explicitly register the Logging Filter (Part 5.5 Requirement)
        register(LoggingFilter.class);
        
        // Optional: Log that the API has started (helpful for debugging)
        System.out.println("SmartCampus API Initialized at /api/v1");
    }
}