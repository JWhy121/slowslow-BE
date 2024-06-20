package com.elice.slowslow.user.controller;

import com.elice.slowslow.user.User;
import com.elice.slowslow.user.dto.*;
import com.elice.slowslow.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    public ResponseEntity<Object> membershipProcess(@RequestBody @Valid MembershipDTO membershipDto, BindingResult bindingResult){

        Map<String, Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            response.put("success", false);
            response.put("errors", bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            FieldError::getDefaultMessage
                    )));
            return ResponseEntity.badRequest().body(response);
        }

        try {
            userService.membershipProcess(membershipDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            ExceptionDTO exceptionDTO = new ExceptionDTO("INTERNAL_SERVER_ERROR", "An unexpected error occurred.");
            return new ResponseEntity<>(exceptionDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }

    //관리자 페이지
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/v1/admin")
    public String adminP() {

        return "admin Controller";
    }

    //SecurityContextHolder를 통해 현재 로그인된 사용자 이름, role 받기
    //myPage
    @GetMapping("/api/v1/mypage")
    public ResponseEntity<MypageResponseDTO> mypage(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String name = customUserDetails.getUsername();

        String role = customUserDetails.getAuthorities().iterator().next().getAuthority();

        System.out.println(role);
        System.out.println("mypage");

        MypageResponseDTO myPageDto = userService.findByNameProc(name);

        return ResponseEntity.ok().header("Content-Type", "application/json").body(myPageDto);
    }

    @PostMapping("/api/v1/checkPasswordForUpdate")
    public ResponseEntity<String> checkPasswordForUpdate(@RequestBody @Valid CheckPasswordDTO checkPasswordDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return checkPasswordAndProceed(customUserDetails, checkPasswordDTO, "정보 수정");
    }

    @PostMapping("/api/v1/checkPasswordForDelete")
    public ResponseEntity<String> checkPasswordForDelete(@RequestBody @Valid CheckPasswordDTO checkPasswordDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return checkPasswordAndProceed(customUserDetails, checkPasswordDTO, "회원 탈퇴");
    }

    private ResponseEntity<String> checkPasswordAndProceed(CustomUserDetails customUserDetails, CheckPasswordDTO checkPasswordDTO, String successMessage) {
        if (customUserDetails == null) {
            log.info("check customUserDetails == null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 정보를 찾을 수 없습니다.");
        }

        String role = customUserDetails.getAuthorities().iterator().next().getAuthority();
        System.out.println(role);
        System.out.println(checkPasswordDTO);

        String name = customUserDetails.getUsername();
        UserDTO userDTO = userService.findByName(name);

        // @GetMapping에서 가져온 비밀번호 정보 사용
        String storedPassword = userDTO.getPassword();

        log.info("Stored Password: {}", storedPassword);

        try {
            boolean passwordsMatch = userService.checkPassword(userDTO, checkPasswordDTO.getPassword());
            if (passwordsMatch) {
                Logger.getLogger(this.getClass().getName()).info("Password check successful for user: " + name);
                return ResponseEntity.ok(successMessage);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
            }
        } catch (IllegalArgumentException e) {
            // 비밀번호가 일치하지 않는 경우 처리
            Logger.getLogger(this.getClass().getName()).info("Password check failed for user: " + name + " - " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
        } catch (Exception e) {
            // 그 외 예외 처리
            Logger.getLogger(this.getClass().getName()).severe("Error during password check for user: " + name + " - " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 확인 중 오류가 발생했습니다.");
        }
    }


    @PostMapping("/api/v1/update")
    public String updateUser(@RequestBody UserDTO userDTO, @AuthenticationPrincipal UserDetails userDetails) {
        String name = userDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        UserDTO user = userService.findByName(name);

        // 입력받은 정보로 사용자 정보 업데이트
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        // 사용자 정보 업데이트
        UserDTO updatedUser = userService.update(user);

        return "수정완료";
    }


    @GetMapping("/api/v1/delete")
    public String deleteByName(@RequestParam("username") String username) {
        // 로그 추가
        log.info("deleteCheck");

        userService.deletedByName(username);
        return "삭제완료";
    }


    @GetMapping("/api/v1/restoreUser/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public String restoreUser(@PathVariable String username) {
        userService.restorationByName(username); // userService에서 회원 복구 작업을 처리
        return "복구완료"; // 복구 성공 메시지 반환
    }

    @GetMapping("/api/v1/admin/userList")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
}
