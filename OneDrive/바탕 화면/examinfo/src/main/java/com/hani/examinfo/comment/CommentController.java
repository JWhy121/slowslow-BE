package com.hani.examinfo.comment;

import com.hani.examinfo.board.Board;
import com.hani.examinfo.board.BoardRepository;
import com.hani.examinfo.post.Post;
import com.hani.examinfo.post.PostDto;
import com.hani.examinfo.post.PostRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/boards/{boardId}/{postId}")
public class CommentController {
    @Autowired
    private PostRepository postRepo;

    @Autowired
    private BoardRepository boardRepo;

    @Autowired
    private CommentRepository commentRepo;



    @PostMapping("/addComment")
    public String createComment(@PathVariable Long boardId, @PathVariable Long postId, @Valid @ModelAttribute CommentDto commentDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("boardId", boardId);
            model.addAttribute("postId", postId);
            return "redirect:/boards/{boardId}/{postId}";
        }

        Optional<Post> postOptional = postRepo.findById(postId);
        if (!postOptional.isPresent()) {
            // 예외 처리 - 해당 게시물이 없는 경우
            return "redirect:/boards/" + boardId;
        }

        Post post = postOptional.get();
        Comment newComment = new Comment();
        newComment.setAuthor(commentDto.getAuthor());
        newComment.setContent(commentDto.getContent());
        newComment.setCreatedAt(new Date());
        newComment.setPost(post);

        commentRepo.save(newComment);
        return "redirect:/boards/{boardId}/{postId}";
    }

    @PostMapping("/editComment/{commentId}")
    public String editComment(@PathVariable Long boardId, @PathVariable Long postId, @PathVariable Long commentId, @Valid @ModelAttribute CommentDto commentDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("boardId", boardId);
            model.addAttribute("postId", postId);
            return "redirect:/boards/{boardId}/{postId}";
        }

        Optional<Comment> commentOptional = commentRepo.findById(commentId);
        if (!commentOptional.isPresent()) {
            // 예외 처리 - 해당 댓글이 없는 경우
            return "redirect:/boards/{boardId}/{postId}";
        }

        Comment comment = commentOptional.get();
        comment.setAuthor(commentDto.getAuthor());
        comment.setContent(commentDto.getContent());

        commentRepo.save(comment);
        return "redirect:/boards/{boardId}/{postId}";

    }

    @PostMapping("/deleteComment")
    public String deleteComment(@PathVariable Long boardId, @PathVariable Long postId, @RequestParam Long commentId) {
        Optional<Comment> commentOptional = commentRepo.findById(commentId);
        if (!commentOptional.isPresent()) {
            // 예외 처리 - 해당 댓글이 없는 경우
            return "redirect:/boards/{boardId}/{postId}";
        }

        commentRepo.delete(commentOptional.get());
        return "redirect:/boards/{boardId}/{postId}";
    }

}
