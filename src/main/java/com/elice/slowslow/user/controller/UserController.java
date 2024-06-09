package com.elice.slowslow.user.controller;

import com.elice.slowslow.user.dto.MembershipDto;
import com.elice.slowslow.user.dto.UserDTO;
import com.elice.slowslow.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@ResponseBody
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/membership")
    public String membershipProcess(MembershipDto membershipDto){

        userService.membershipProcess(membershipDto);

        return "ok";
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
