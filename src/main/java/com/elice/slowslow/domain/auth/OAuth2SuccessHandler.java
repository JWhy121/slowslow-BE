package com.elice.slowslow.domain.auth;

import com.elice.slowslow.domain.user.User;
import com.elice.slowslow.domain.user.jwt.JWTConfig;
import com.elice.slowslow.domain.user.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    public OAuth2SuccessHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        System.out.println("CustomOAuth2User user: " + oAuth2User.getUser());


        User user = oAuth2User.getUser();
        String role = user.getRole().toString();

        // JWT 발급
        String token = jwtUtil
                .createJwt(
                        user.getId(),
                        user.getUsername(),
                        role,
                        JWTConfig.EXPIRATION
                );

        System.out.println("Generated token = " + token);

        String redirectUrl = "http://ec2-34-228-144-19.compute-1.amazonaws.com:3000/oauth/callback?token=Bearer " + token;

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }
}

