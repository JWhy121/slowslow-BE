package com.elice.slowslow.user.jwt;

import com.elice.slowslow.user.dto.CustomUserDetails;
import com.elice.slowslow.user.dto.LoginDTO;
import com.elice.slowslow.user.dto.ExceptionDTO;
import com.elice.slowslow.user.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, CustomUserDetailsService customUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {


        System.out.println("로그인 진행");

        // request에 있는 username과 password를 파싱해서 자바 Object로 받기
        ObjectMapper om = new ObjectMapper();
        LoginDTO loginDto = null;
        try {
            loginDto = om.readValue(request.getInputStream(), LoginDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            handleLoginAttempt(loginDto);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword(), null);
            return authenticationManager.authenticate(authToken);
        } catch (UsernameNotFoundException e) {
            // JSON 형태의 에러 응답 보내기
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try {
                om.writeValue(response.getOutputStream(), new ExceptionDTO("USER_NOT_FOUND", "존재하지 않는 계정입니다. 새로운 계정을 만들어주세요."));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            throw new UsernameNotFoundException("잘못된 로그인 정보입니다.");
        } catch (BadCredentialsException e) {
            // JSON 형태의 에러 응답 보내기
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try {
                om.writeValue(response.getOutputStream(), new ExceptionDTO("INVALID_PASSWORD", "비밀번호가 틀립니다. 다시 확인해주세요."));
            } catch (IOException ex){
                ex.printStackTrace();
            }
            throw new BadCredentialsException("비밀번호가 틀렸습니다");
        } catch (DisabledException e) {
            // JSON 형태의 에러 응답 보내기
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try {
                om.writeValue(response.getOutputStream(), new ExceptionDTO("ACCOUNT_DISABLED", "삭제된 계정입니다. 다시 확인해주세요."));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            throw new DisabledException("삭제된 계정입니다. 다시 확인해주세요.");
        }

    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        System.out.println("로그인 성공");

        //UserDetailsS
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Long id = customUserDetails.getId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(id, username, role, JWTConfig.EXPIRATION);

        System.out.println(token);

        response.addHeader("Authorization", "Bearer " + token);
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        System.out.println("로그인 실패");
    }

    private void handleLoginAttempt(LoginDTO loginDto) {
        if (customUserDetailsService.loadUserByUsername(loginDto.getUsername()) == null) {
            System.out.println("존재하지 않는 계정입니다. 새로운 계정을 만들어주세요.");
            throw new UsernameNotFoundException("존재하지 않는 계정입니다. 새로운 계정을 만들어주세요.");
        }

        CustomUserDetails user = (CustomUserDetails) customUserDetailsService.loadUserByUsername(loginDto.getUsername());

        if (!bCryptPasswordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            System.out.println("비밀번호가 틀립니다. 다시 확인해주세요.");
            throw new BadCredentialsException("잘못된 로그인 정보입니다.");
        }

        if (user.isDeleted()) {
            System.out.println("이미 탈퇴된 계정입니다. 새로운 계정을 만들어주세요.");
            throw new DisabledException("계정이 비활성화되었습니다.");
        }
    }
}
