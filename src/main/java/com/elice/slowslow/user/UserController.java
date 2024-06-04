package com.elice.slowslow.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
public class UserController {


    //기본 페이지 요청 메서드
    @GetMapping("/")
    public String index() {
        log.info("index");
        return "index";
    }

    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "name", defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }

//    @GetMapping("/user")
//    public User getUser() {
//        User user = new User();
//        user.setId(1L);
//        user.setName("yj");
//        user.setMemberId("yj123");
//        user.setPassword("password123");
//        user.setPhoneNumber("123-4567-8910");
//        user.setEmail("yjyj@example.com");
//        user.setRole(User.RoleType.USER);
//        user.setDeleted(false);
//        return user;
//    }

}
