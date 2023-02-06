package com.crud.tasks.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GlobalHttpErrorHandlerTest {

    @Autowired
    GlobalHttpErrorHandler httpErrorHandler;

    @Test
    void handleTaskNotFoundException() {
        //Given
        TaskNotFoundException taskNotFoundException = new TaskNotFoundException();
        ResponseEntity responseToError = httpErrorHandler.handleTaskNotFoundException(taskNotFoundException);

        //When & Then
        assertEquals("Task with given id doesn't exist", responseToError.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseToError.getStatusCode());
    }
}
