package com.elice.slowslow.user.controller;

import com.elice.slowslow.user.User;
import com.elice.slowslow.user.dto.MembershipDto;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RestController
@ResponseBody
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    /*백엔드 프론트 연동 확인 테스트 컨트롤러입니다. 곧 지워져요.........*/
    @GetMapping("/api/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(String.format("백엔드에서 보내는 메시지다! %s!", name));
    }

    public static class Greeting {
        private String message;

        public Greeting(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /*---------------백엔드 프론트 연동 테스트 코드 끝-----------*/


    @PostMapping("/api/v1/membership")
    public ResponseEntity membershipProcess(@RequestBody @Valid MembershipDto membershipDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        User user = userService.membershipProcess(membershipDto);

        return new ResponseEntity(user, HttpStatus.CREATED);
    }


    //관리자 페이지
    @GetMapping("/api/v1/admin")
    public String adminP() {

        return "admin Controller";
    }


    //SecurityContextHolder를 통해 현재 로그인된 사용자 이름, role 받기
    //myPage
    @GetMapping("/myPage")
    public String mainP(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        UserDTO user = userService.findByNameProc(name);

        return "my Page" + user;
    }

    //기본 페이지 요청 메서드
    @GetMapping("/")
    public String index() {
        log.info("index");
        return "index";
    }

//    @GetMapping("/user/save")
//    public String saveForm() {
//        return "save";
//    }
//
//    @PostMapping("/user/save")
//    public String save(@ModelAttribute UserDTO userDTO) {
//        System.out.println("UserController.save");
//        System.out.println("userDTO = " + userDTO);
//        userService.save(userDTO);
//        return "login";
//    }
//
//    @GetMapping("/user/login")
//    public String loginForm() {
//        return "login";
//    }
//
//    @PostMapping("/user/login")
//    public String login(@ModelAttribute UserDTO userDTO, HttpSession session) {
//        UserDTO loginResult = userService.login(userDTO);
//        if(loginResult != null) {
//            //login 성공
//            session.setAttribute("loginEmail", loginResult.getUsername());
//            return "main";
//        } else {
//            //login 실패
//            return "login";
//        }
//    }
//
//    @GetMapping("/user/")
//    public String findAll(Model model) {
//        List<UserDTO> userDTOList = userService.findAll();
//        model.addAttribute("userList", userDTOList);
//        return "list";
//    }
//
//    @GetMapping("/user/{id}")
//    public String findById(@PathVariable Long id, Model model) {
//        UserDTO userDTO = userService.findById(id);
//        model.addAttribute("user", userDTO);
//        return "detail";
//    }
//
//    @GetMapping("/user/update")
//    public String updateForm(HttpSession session, Model model) {
//        String myEmail =  (String)session.getAttribute("loginEmail");
//        UserDTO userDTO = userService.updateForm(myEmail);
//        model.addAttribute("updateUser", userDTO);
//        log.info("check1");
//        return "update";
//    }
//
//    @PostMapping("/user/update")
//    public String update(@ModelAttribute UserDTO userDTO) {
//        userService.update(userDTO);
//        log.info("check2");
//        return "redirect:/user/" + userDTO.getId();
//    }

    @GetMapping("/api/v1/update")
    public String updateUserForm() {
        return "update";
    }

    @PostMapping("/api/v1/update")
    public String updateUser(@RequestParam("password") String password) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        UserDTO user = userService.findByNameProc(name);

        if (user == null) {
            return "사용자 정보를 찾을 수 없습니다.";
        }


        // 사용자가 입력한 비밀번호와 저장된 비밀번호 비교
        boolean passwordsMatch = userService.checkPassword(user, password);

        if (passwordsMatch) {
            // 비밀번호가 일치할 경우 업데이트 로직 실행
            // 여기에 실제로 업데이트하는 로직을 추가하면 됩니다.
            return "정보 수정 폼으로 이동";
        } else {
            // 비밀번호가 일치하지 않을 경우 처리
            return "불일치";
        }

//        return "update" + user;
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