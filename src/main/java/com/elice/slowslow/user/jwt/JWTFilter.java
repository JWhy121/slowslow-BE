package com.elice.slowslow.user.jwt;

import com.elice.slowslow.user.User;
import com.elice.slowslow.user.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.elice.slowslow.user.jwt.JWTConfig.*;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request 헤더에서 Authorization에 해당하는 값을 찾음
        String authorization = request.getHeader(AUTH_HEADER);

        System.out.println(authorization);

        //Authorization 헤더 검증
        //토큰이 없거나, jwt 토큰이 아닌지 확인 (접두사 Bearer면 jwt)
        if (authorization == null || !authorization.startsWith(TOKEN_PREFIX)) {


            System.out.println("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;

        }

        System.out.println("authorization now");
        //Bearer 부분 제거 후 순수 토큰만 획득
        //[0]에는 Bearer가 들어있음. 제거한 후 뒷부분만 획득하는 과정
        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);


        //userEntity를 생성하여 값 set
        User user = new User();
        user.setUsername(username);
        user.setPassword("temppassword");
        user.setRole(User.RoleType.valueOf(role));

        //UserDetails에 회원 정보 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        System.out.println(user.getUsername());;

        //스프링 시큐리티를 통해서 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());


        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }


}
