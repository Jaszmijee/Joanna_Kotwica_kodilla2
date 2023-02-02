package com.crud.tasks.service;

import com.crud.tasks.controller.TaskNotFoundException;
import com.crud.tasks.domain.*;
import com.crud.tasks.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DbServiceTest {

    @InjectMocks
    private DbService dbService;

    @Mock
    private TaskRepository repository;

    @Test
    void getAllTasks() {
        //Given
        Task task1 = new Task(1L, "1rst Task", "a dog");
        Task task2 = new Task(2L, "2nd Task", "a cat");
        Task task3 = new Task(3L, "3rd Task", "a chicken");
        List<Task> listOfTasks = new ArrayList<>(Arrays.asList(task1, task2, task3));

        //When
        when(repository.findAll()).thenReturn(listOfTasks);

        //Then
        assertEquals(3, dbService.getAllTasks().size());

    }

    @Test
    void saveTask() {
        //Given
        Task task1 = new Task(1L, "1rst Task", "a dog");
        Task task2 = new Task(2L, "2nd Task", "a cat");

        //When
        when(repository.save(task1)).thenReturn(task2);

        //Then
        assertEquals(task2, dbService.saveTask(task1));
    }

    @Test
    void getTask() {
        //Given
        Task task1 = new Task(1L, "1rst Task", "a dog");

        //When
        when(repository.findById(1L)).thenReturn(Optional.of(task1));

        //Then
        assertDoesNotThrow(() -> dbService.getTask(1L));
        try {
            assertEquals("a dog", dbService.getTask(1L).getContent());
        } catch (TaskNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertThrows(TaskNotFoundException.class, () -> dbService.getTask(4L));
    }

    @Test
    void deleteTask() {
        Task task1 = new Task(1L, "1rst Task", "a dog");

        //When
        when(repository.findById(1L)).thenReturn(Optional.of(task1));

        //Then
        assertDoesNotThrow(() -> dbService.deleteTask(1L));
        assertThrows(TaskNotFoundException.class, () -> dbService.deleteTask(4L));
    }
}

