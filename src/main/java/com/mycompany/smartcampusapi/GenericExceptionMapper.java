/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi;

import com.mycompany.smartcampusapi.models.ErrorMessage;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider // Important: This registers the class automatically
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        // Log the error to your NetBeans console so you can still see it
        exception.printStackTrace();

        ErrorMessage error = new ErrorMessage(
            "Request failed: " + exception.getMessage(), 
            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()
        );
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .type("application/json")
                .build();
    }
}