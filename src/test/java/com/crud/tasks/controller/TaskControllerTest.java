package com.crud.tasks.controller;

import com.crud.tasks.domain.*;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitWebConfig
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService service;

    @MockBean
    private TaskMapper taskMapper;

    @Test
    void getTasksEmptyList() throws Exception {
        // Given
        List<Task> taskList = List.of();
        List<TaskDto> taskDtos = List.of();
        when(service.getAllTasks()).thenReturn(taskList);
        when(taskMapper.mapToTaskDtoList(taskList)).thenReturn(taskDtos);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200)) // or isOk()
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void getTasksList() throws Exception {
        // Given
        List<Task> list = List.of(new Task(1L, "task1", "test1"));
        List<TaskDto> taskDtos = List.of(new TaskDto(1L, "task1", "test1"));
        when(service.getAllTasks()).thenReturn(list);
        when(taskMapper.mapToTaskDtoList(list)).thenReturn(taskDtos);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                // Tasks fields
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("task1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content", Matchers.is("test1")));

        verify(service, times(1)).getAllTasks();
        verify(taskMapper, times(1)).mapToTaskDtoList(list);
    }

    @Test
    void getTaskDoesntExist() throws Exception {
        // Given
        Task task = new Task(1L, "task1", "test1");
        TaskDto taskDto = new TaskDto(1L, "task1", "test1");
        when(service.getTask(4L)).thenThrow(new TaskNotFoundException());
        when(taskMapper.mapToTaskDto(task)).thenReturn(taskDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/tasks/{taskId}", 4L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertEquals("Task with given id doesn't exist", result.getResponse().getContentAsString()));

        verify(service, times(1)).getTask(4L);
        verify(taskMapper, times(0)).mapToTaskDto(task);
    }

    @Test
    void getTaskExists() throws Exception {
        // Given
        Task task = new Task(2L, "task2", "test2");
        TaskDto taskDto = new TaskDto(2L, "task2", "test2");
        when(service.getTask(2L)).thenReturn(task);
        when(taskMapper.mapToTaskDto(task)).thenReturn(taskDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/tasks/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                // Tasks fields
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("task2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("test2")));

        verify(service, times(1)).getTask(2L);
        verify(taskMapper, times(1)).mapToTaskDto(task);
    }

    @Test
    void deleteTaskDoesNotExist() throws Exception {
        // Given
        doCallRealMethod().when(service).deleteTask(4L);
        when(service.getTask(4L)).thenThrow(TaskNotFoundException.class);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/v1/tasks/4")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertEquals("Task with given id doesn't exist", result.getResponse().getContentAsString()));

        verify(service, times(1)).deleteTask(4L);
    }

    @Test
    void deleteTaskExists() throws Exception {
        // Given
        Task task = new Task(2L, "task2", "test2");
        when(service.getTask(2L)).thenReturn(task);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/v1/tasks/2")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(service, times(1)).deleteTask(2L);
    }

    @Test
    void updateTask() throws Exception {
        //Given
        Task taskToUpdate = new Task(1L, "task2", "test2");
        TaskDto taskDtoToUpdate = new TaskDto(1L, "task2", "test2");

        when(taskMapper.mapToTask(any(TaskDto.class))).thenReturn(taskToUpdate);
        when(taskMapper.mapToTaskDto(taskToUpdate)).thenReturn(taskDtoToUpdate);

        when(service.saveTask(any(Task.class))).thenReturn(taskToUpdate);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(taskDtoToUpdate);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("task2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("test2")));

        verify(taskMapper, times(1)).mapToTask(any(TaskDto.class));
        verify(service, times(1)).saveTask(taskToUpdate);
        verify(taskMapper, times(1)).mapToTaskDto(taskToUpdate);
    }


    @Test
    void createTask() throws Exception {
        //Given
        Task task = new Task(1L, "task2", "test2");
        TaskDto taskDto = new TaskDto(1L, "task2", "test2");

        when(taskMapper.mapToTask(any(TaskDto.class))).thenReturn(task);
        when(service.saveTask(task)).thenReturn(task);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(taskDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(taskMapper, times(1)).mapToTask(any(TaskDto.class));
        verify(service, times(1)).saveTask(task);
    }
}

