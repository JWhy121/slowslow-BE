package com.hani.examinfo.apiTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hani.examinfo.post.PostController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetPosts() throws Exception {
        mockMvc.perform(get("/boards/{boardId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("board/details"));
    }

    @Test
    public void testCreatePost() throws Exception {
        mockMvc.perform(post("/boards/{boardId}/create", 1L)
                        .param("title", "New Post")
                        .param("author", "Author")
                        .param("content", "Content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards/1"));
    }
}