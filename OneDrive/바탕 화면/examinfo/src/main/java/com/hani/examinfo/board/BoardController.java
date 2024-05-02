package com.hani.examinfo.board;


import com.hani.examinfo.post.Post;
import com.hani.examinfo.post.PostRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/boards")
public class BoardController {
    @Autowired
    private PostRepository postRepo;

    @Autowired
    private BoardRepository repo;

    @GetMapping
    public String showBoardList(Model model){
        List<Board> boards = repo.findAll();
        model.addAttribute("boards", boards);
        return "board/boards";
    }

    @GetMapping("/create")
    public String showCreateBoard(Model model){
        BoardDto boardDto = new BoardDto();
        model.addAttribute("boardDto",boardDto);
        return "board/createBoard";
    }

    @PostMapping("/create")
    public String createBoard(@Valid @ModelAttribute BoardDto boardDto, BindingResult result){
        if(result.hasErrors()){
            return "board/createBoard";
        }

        Board newBoard = new Board();
        newBoard.setBoardTitle(boardDto.getBoardTitle());
        newBoard.setDescription(boardDto.getDescription());
        newBoard.setCreatedAt(new Date());

        repo.save(newBoard);
        // 생성 성공 응답
        return "redirect:/boards";

    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long boardId){
        Optional<Board> optionalBoard = repo.findById(boardId);
        if (!optionalBoard.isPresent()) {
            return "redirect:/boards";
        }
        Board board = optionalBoard.get();

        BoardDto boardDto = new BoardDto();
        boardDto.setBoardTitle(board.getBoardTitle());
        boardDto.setDescription(board.getDescription());
        model.addAttribute("board", board);
        model.addAttribute("boardDto", boardDto);

        return "board/editBoard";

    }

    @PostMapping("/edit")
    public String updateBoard(Model model, @RequestParam Long boardId, @Valid @ModelAttribute BoardDto boardDto, BindingResult result){
        if(result.hasErrors()){
            model.addAttribute("board", repo.findById(boardId).orElse(null));
            model.addAttribute("boardDto", boardDto);
            return "board/editBoard";
        }

        Optional<Board> existingBoard = repo.findById(boardId);
        if (existingBoard.isPresent()) {
            Board updatedBoard = existingBoard.get();
            updatedBoard.setBoardTitle(boardDto.getBoardTitle());
            updatedBoard.setDescription(boardDto.getDescription());
            repo.save(updatedBoard);
        }
        return "redirect:/boards";
    }

    @PostMapping("/delete")
    public String deleteBoard(@RequestParam Long boardId) {
        repo.deleteById(boardId);

        return "redirect:/boards";
    }
    @GetMapping("/{boardId}")
    public String showBoardDetails(@PathVariable Long boardId, Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        Optional<Board> board = repo.findById(boardId);
        if (!board.isPresent()) {
            return "redirect:/boards";
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postRepo.findByBoard_BoardId(boardId, pageable);

        model.addAttribute("board", board.get());
        model.addAttribute("posts", posts);

        return "board/details";
    }
}
