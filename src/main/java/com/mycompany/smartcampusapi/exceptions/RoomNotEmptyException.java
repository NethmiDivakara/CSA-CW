/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi.exceptions;

/**
 * Thrown when a user attempts to delete a room that still contains sensors.
 * Maps to HTTP 409 Conflict.
 */
public class RoomNotEmptyException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RoomNotEmptyException(String message) {
        super(message);
    }
}