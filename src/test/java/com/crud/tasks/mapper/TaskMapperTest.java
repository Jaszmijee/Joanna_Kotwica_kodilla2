package com.crud.tasks.mapper;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskMapperTest {

    @Autowired
    TaskMapper taskMapper;

    @Test
    void mapToTask() {
        //Given
        TaskDto taskDto = new TaskDto(1L, "toDo", "test are the best thing ever");

        //When & Then
        assertEquals(Task.class, taskMapper.mapToTask(taskDto).getClass());
        assertEquals(1, taskMapper.mapToTask(taskDto).getId());
    }

    @Test
    void mapToTaskDto() {
        //Given
        Task task = new Task(2L, "Doing", "one more test");

        //When & Then
        assertEquals(TaskDto.class, taskMapper.mapToTaskDto(task).getClass());
        assertEquals("Doing", taskMapper.mapToTaskDto(task).getTitle());
    }

    @Test
    void mapToTaskDtoList() {
        //Given
        Task taskDto1 = new Task(1L, "toDo", "test are the best thing ever");
        Task taskDto2 = new Task(2L, "Doing", "one more test");
        Task taskDto3 = new Task(3L, "Done", "this is the last test");
        List<Task> taskList = new ArrayList<>(Arrays.asList(taskDto1, taskDto2, taskDto3));

        //When & Then
        assertEquals(TaskDto.class, taskMapper.mapToTaskDtoList(taskList).get(0).getClass());
        assertEquals("this is the last test", taskMapper.mapToTaskDtoList(taskList).get(2).getContent());
    }
}


