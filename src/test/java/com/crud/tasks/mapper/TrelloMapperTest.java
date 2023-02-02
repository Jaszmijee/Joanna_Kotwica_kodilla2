package com.crud.tasks.mapper;

import com.crud.tasks.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TrelloMapperTest {

    @Autowired
    private TrelloMapper trelloMapper;

    @Test
    void mapToBoards() {
        // Given
        TrelloListDto listDto1 = new TrelloListDto("1", "Trello List1", true);
        TrelloListDto listDto2 = new TrelloListDto("2", "Trello List2", false);
        TrelloListDto listDto3 = new TrelloListDto("3", "Trello List3", false);
        List<TrelloListDto> listOfTrelloListDtos = new ArrayList<>(Arrays.asList(listDto1, listDto2, listDto3));

        TrelloBoardDto boardDto = new TrelloBoardDto("200", "Trello Board 200", listOfTrelloListDtos);
        List<TrelloBoardDto> boardDtoList = new ArrayList<>(Arrays.asList(boardDto, boardDto, boardDto));
        List<TrelloBoardDto> boardDtoListEmpty = new ArrayList<>();

        //When & Then
        assertEquals(TrelloBoard.class, trelloMapper.mapToBoards(boardDtoList).get(0).getClass());
        assertEquals(3, trelloMapper.mapToBoards(boardDtoList).size());
        assertTrue(trelloMapper.mapToBoards(boardDtoListEmpty).isEmpty());
    }

    @Test
    void mapToBoardsDto() {
        //Given
        TrelloList trello_list1 = new TrelloList("1", "Trello List1", true);
        TrelloList trello_list2 = new TrelloList("1", "Trello List2", false);
        TrelloList trello_list3 = new TrelloList("1", "Trello List3", false);
        List<TrelloList> listOfTrelloList = new ArrayList<>() {{
            add(trello_list1);
            add(trello_list2);
            add(trello_list3);
        }};

        TrelloBoard trelloBoard1 = new TrelloBoard("100", "TrelloBoard1", listOfTrelloList);
        TrelloBoard trelloBoard2 = new TrelloBoard("200", "TrelloBoard2", listOfTrelloList);
        TrelloBoard trelloBoard3 = new TrelloBoard("300", "TrelloBoard3", listOfTrelloList);
        List<TrelloBoard> boardList = new ArrayList<>(Arrays.asList(trelloBoard1, trelloBoard2, trelloBoard3));

        //When & Then
        TrelloBoardDto result3 = trelloMapper.mapToBoardsDto(boardList).get(2);
        assertEquals(TrelloBoardDto.class, trelloMapper.mapToBoardsDto(boardList).get(0).getClass());
        assertEquals("TrelloBoard3", result3.getName());
    }

    @Test
    void mapToList() {
        // Given
        TrelloListDto listDto1 = new TrelloListDto("1", "Trello List1", true);
        TrelloListDto listDto2 = new TrelloListDto("2", "Trello List2", false);
        TrelloListDto listDto3 = new TrelloListDto("3", "Trello List3", false);
        List<TrelloListDto> listOfTrelloListDtos = new ArrayList<>(Arrays.asList(listDto1, listDto2, listDto3));

        //When & Then
        TrelloList result1 = trelloMapper.mapToList(listOfTrelloListDtos).get(0);
        assertEquals(TrelloList.class, trelloMapper.mapToList(listOfTrelloListDtos).get(0).getClass());
        assertEquals("Trello List1", result1.getName());

    }

    @Test
    void mapToListDto() {
        //Given
        TrelloList trello_list1 = new TrelloList("1", "Trello List1", true);
        TrelloList trello_list2 = new TrelloList("2", "Trello List2", false);
        TrelloList trello_list3 = new TrelloList("2", "Trello List3", false);
        List<TrelloList> listOfTrelloList = new ArrayList<>() {{
            add(trello_list1);
            add(trello_list2);
            add(trello_list3);
        }};

        //When & Then
        TrelloListDto result1 = trelloMapper.mapToListDto(listOfTrelloList).get(0);
        assertEquals(TrelloListDto.class, trelloMapper.mapToListDto(listOfTrelloList).get(0).getClass());
        assertEquals("Trello List1", result1.getName());
    }

    @Test
    void mapToCardDto() {
        //Given
        TrelloCard trelloCard = new TrelloCard("First", "Things to do first", "up", "1");

        //When & Then
        assertEquals(TrelloCardDto.class, trelloMapper.mapToCardDto(trelloCard).getClass());
        assertEquals("up", trelloCard.getPos());
    }

    @Test
    void mapToCard() {
        //Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("First", "Things to do first", "down", "1");

        //When & Then
        assertEquals(TrelloCard.class, trelloMapper.mapToCard(trelloCardDto).getClass());
        assertEquals("down", trelloCardDto.getPos());
    }
}

