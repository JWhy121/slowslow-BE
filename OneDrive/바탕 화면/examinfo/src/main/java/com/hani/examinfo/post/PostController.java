package com.hani.examinfo.post;

import com.hani.examinfo.comment.Comment;
import com.hani.examinfo.comment.CommentRepository;
import com.hani.examinfo.post.Post;
import com.hani.examinfo.board.Board;
import com.hani.examinfo.board.BoardDto;
import com.hani.examinfo.board.BoardRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/boards/{boardId}")
public class PostController {
    @Autowired
    private PostRepository postRepo;

    @Autowired
    private BoardRepository boardRepo;

    @Autowired
    private CommentRepository commentRepo;

    @GetMapping("/create")
    public String showCreatePost(@PathVariable Long boardId, Model model) {
        PostDto postDto = new PostDto();
        model.addAttribute("postDto", postDto);
        model.addAttribute("boardId", boardId);
        return "post/createPost";
    }

    @PostMapping("/create")
    public String createPost(@PathVariable Long boardId, @Valid @ModelAttribute PostDto postDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("boardId", boardId);
            return "post/createPost";
        }

        Post newPost = new Post();
        newPost.setTitle(postDto.getTitle());
        newPost.setAuthor(postDto.getAuthor());
        newPost.setContent(postDto.getContent());
        newPost.setCreatedAt(new Date());

        // 생성 성공 응답
        Optional<Board> board = boardRepo.findById(boardId);
        if (!board.isPresent()) {
            return "redirect:/boards";
        }
        newPost.setBoard(board.get());

        postRepo.save(newPost);
        return "redirect:/boards/" + boardId;

    }

    @GetMapping("/search")
    public String searchPosts(@PathVariable Long boardId, @RequestParam String keyword, Model model) {
        Optional<Board> board = boardRepo.findById(boardId);
        if (!board.isPresent()) {
            return "redirect:/boards";
        }

        List<Post> searchResults = postRepo.searchPostsByKeyword(keyword);
        model.addAttribute("board", board.get());
        model.addAttribute("posts", searchResults);

        return "post/searchResults"; // 검색 결과를 보여줄 새로운 HTML 페이지
    }

    @GetMapping("/{postId}")
    public String showPostDetails(@PathVariable Long boardId, @PathVariable Long postId,
                                  @RequestParam(required = false, defaultValue = "asc") String sortOrder,
                                  Model model) {
        Optional<Board> board = boardRepo.findById(boardId);
        Optional<Post> post = postRepo.findById(postId);

        if (!board.isPresent()) {
            return "redirect:/boards";
        }
        if (!post.isPresent()) {
            return "redirect:/boards/" + boardId;
        }

        // 댓글을 정렬하여 가져오기
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by("createdAt").descending() : Sort.by("createdAt").ascending();
        List<Comment> comments = commentRepo.findByPost_PostId(postId, sort);

        // 게시물과 댓글 정보를 모델에 추가
        model.addAttribute("board", board.get());
        model.addAttribute("post", post.get());
        model.addAttribute("comments", comments);
        model.addAttribute("boardId", boardId); // 게시판 ID를 모델에 추가하여 링크에 사용
        model.addAttribute("sortOrder", sortOrder); // 정렬 순서도 추가

        return "post/post";
    }

    @GetMapping("/{postId}/edit")
    public String editPost(@PathVariable Long boardId, @PathVariable Long postId, Model model) {
        Optional<Board> board = boardRepo.findById(boardId);
        Optional<Post> post = postRepo.findById(postId);

        if (!board.isPresent() || !post.isPresent()) {
            return "redirect:/boards/" + boardId;
        }

        model.addAttribute("postDto", post.get()); // Assuming you have a way to convert Post to PostDto
        model.addAttribute("boardId", boardId);
        return "post/editPost";
    }

    @PostMapping("/{postId}/edit")
    public String updatePost(@PathVariable Long boardId, @PathVariable Long postId,
                             @Valid @ModelAttribute("postDto") PostDto postDto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("boardId", boardId);
            return "post/editPost";
        }

        Optional<Board> board = boardRepo.findById(boardId);
        Optional<Post> existingPost = postRepo.findById(postId);

        if (!board.isPresent() || !existingPost.isPresent()) {
            return "redirect:/boards/" + boardId;
        }

        Post post = existingPost.get();
        post.setTitle(postDto.getTitle());
        post.setAuthor(postDto.getAuthor());
        post.setContent(postDto.getContent());
        postRepo.save(post);

        return "redirect:/boards/{boardId}/{postId}";
    }

    @PostMapping("/{postId}/delete")
    public String deletePost(@PathVariable Long boardId, @PathVariable Long postId) {
        postRepo.deleteById(postId);
        return "redirect:/boards/" + boardId;
    }


}