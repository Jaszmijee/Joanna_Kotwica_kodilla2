package com.crud.tasks.trello.validator;

import com.crud.tasks.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TrelloValidatorTest {

    @Autowired
    TrelloValidator trelloValidator;

      @Test
    void validateTrelloBoards() {
        //Given
        TrelloList trello_list1 = new TrelloList("1", "Trello List1", true);
        TrelloList trello_list2 = new TrelloList("2", "Trello List2", false);
        TrelloList trello_list3 = new TrelloList("3", "Trello List3", false);
        List<TrelloList> listOfTrelloList = new ArrayList<>() {{
            add(trello_list1);
            add(trello_list2);
            add(trello_list3);
        }};

        List<TrelloBoard> trelloBoards = new ArrayList<>() {{
            add(new TrelloBoard("1", "test", listOfTrelloList));
            add(new TrelloBoard("2", "no test", listOfTrelloList));
            add(new TrelloBoard("3", "also no test", listOfTrelloList));
        }};

        //When
        List<TrelloBoard> filteredList = trelloValidator.validateTrelloBoards(trelloBoards);

        //Then
        assertEquals(2, filteredList.size());
    }
}

