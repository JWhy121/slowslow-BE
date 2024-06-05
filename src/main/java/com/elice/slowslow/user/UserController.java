package com.elice.slowslow.user;


import com.elice.slowslow.user.dto.MembershipDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@ResponseBody
@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/membership")
    public String membershipProcess(MembershipDto membershipDto){

        System.out.println(membershipDto.getName());
        userService.membershipProcess(membershipDto);

        return "ok";
    }
}
