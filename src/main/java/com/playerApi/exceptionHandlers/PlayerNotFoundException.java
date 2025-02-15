package com.playerApi.exceptionHandlers;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String id) {
        super("Player with ID " + id + " not found");
    }
}
