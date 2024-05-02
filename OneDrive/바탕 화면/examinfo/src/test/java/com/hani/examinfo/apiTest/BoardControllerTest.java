package com.hani.examinfo.apiTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hani.examinfo.board.BoardController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BoardController.class)
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetBoards() throws Exception {
        mockMvc.perform(get("/boards"))
                .andExpect(status().isOk())
                .andExpect(view().name("board/boards"));
    }

    @Test
    public void testCreateBoard() throws Exception {
        mockMvc.perform(post("/boards/create")
                        .param("boardTitle", "New Board")
                        .param("description", "Board Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards"));
    }
}