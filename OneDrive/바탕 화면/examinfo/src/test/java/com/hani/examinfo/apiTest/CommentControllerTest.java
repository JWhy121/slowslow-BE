package com.hani.examinfo.apiTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hani.examinfo.comment.CommentController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetComments() throws Exception {
        mockMvc.perform(get("/boards/{boardId}/{postId}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("post/post"));
    }

    @Test
    public void testCreateComment() throws Exception {
        mockMvc.perform(post("/boards/{boardId}/{postId}/addComment", 1L, 1L)
                        .param("author", "Commenter")
                        .param("content", "Comment content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boards/1/1"));
    }
}