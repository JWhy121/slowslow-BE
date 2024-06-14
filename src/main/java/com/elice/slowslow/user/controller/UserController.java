package com.elice.slowslow.user.controller;

import com.elice.slowslow.user.User;
import com.elice.slowslow.user.dto.MembershipDTO;
import com.elice.slowslow.user.dto.MypageResponseDTO;
import com.elice.slowslow.user.dto.UserDTO;
import com.elice.slowslow.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static io.jsonwebtoken.Jwts.header;

@Slf4j
@RestController
@ResponseBody
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/api/v1/membership")
    public ResponseEntity<String> membershipProcess(@RequestBody @Valid MembershipDTO membershipDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = userService.membershipProcess(membershipDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    //관리자 페이지
    @GetMapping("/api/v1/admin")
    public String adminP() {

        return "admin Controller";
    }


    //SecurityContextHolder를 통해 현재 로그인된 사용자 이름, role 받기
    //myPage
    @GetMapping("/mypage")
    public ResponseEntity<MypageResponseDTO> mypage(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        MypageResponseDTO mypageDto = userService.findByNameProc(name);

        return ResponseEntity.ok().header("Content-Type", "application/json").body(mypageDto);
    }

    //기본 페이지 요청 메서드
    @GetMapping("/")
    public String index() {
        log.info("index");
        return "index";
    }

    @GetMapping("/user/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/user/save")
    public String save(@ModelAttribute UserDTO userDTO) {
        System.out.println("UserController.save");
        System.out.println("userDTO = " + userDTO);
        userService.save(userDTO);
        return "login";
    }

    @GetMapping("/user/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/user/login")
    public String login(@ModelAttribute UserDTO userDTO, HttpSession session) {
        UserDTO loginResult = userService.login(userDTO);
        if(loginResult != null) {
            //login 성공
            session.setAttribute("loginEmail", loginResult.getUsername());
            return "main";
        } else {
            //login 실패
            return "login";
        }
    }

    @GetMapping("/user/")
    public String findAll(Model model) {
        List<UserDTO> userDTOList = userService.findAll();
        model.addAttribute("userList", userDTOList);
        return "list";
    }

    @GetMapping("/user/{id}")
    public String findById(@PathVariable Long id, Model model) {
        UserDTO userDTO = userService.findById(id);
        model.addAttribute("user", userDTO);
        return "detail";
    }

    @GetMapping("/user/update")
    public String updateForm(HttpSession session, Model model) {
        String myEmail =  (String)session.getAttribute("loginEmail");
        UserDTO userDTO = userService.updateForm(myEmail);
        model.addAttribute("updateUser", userDTO);
        log.info("check1");
        return "update";
    }

    @PostMapping("/user/update")
    public String update(@ModelAttribute UserDTO userDTO) {
        userService.update(userDTO);
        log.info("check2");
        return "redirect:/user/" + userDTO.getId();
    }

    @GetMapping("/user/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        userService.deletedById(id);
        return "redirect:/user/";
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }
}