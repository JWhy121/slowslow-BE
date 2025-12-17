package com.elice.slowslow.domain.auth.service;

import com.elice.slowslow.domain.auth.CustomOAuth2User;
import com.elice.slowslow.domain.user.User;
import com.elice.slowslow.domain.user.jwt.JWTConfig;
import com.elice.slowslow.domain.user.jwt.JWTUtil;
import com.elice.slowslow.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class OAuth2UserProcessService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public OAuth2UserProcessService(
            UserRepository userRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public OAuth2User processOAuthUser(OAuth2User oAuth2User) {

        Map<String, Object> attributes = oAuth2User.getAttributes();

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
        Map<String, Object> profile =
                kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;
        String nickname = profile != null ? (String) profile.get("nickname") : null;

        // 1) 기존 유저인지 확인
        User user = userRepository.findByUsername(email)
                .orElseGet(() -> registerNewKakaoUser(email, nickname));

        // 3) OAuth2User에 accessToken을 넣어서 SuccessHandler에서 다시 꺼낼 수 있게
        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }

    private User registerNewKakaoUser(String email, String nickname) {

        User user = User.builder()
                .username(email)
                .name(nickname)
                .role(User.RoleType.ROLE_USER)
                .phoneNumber("01012345678")
                .provider(User.Provider.KAKAO)
                .password(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()))
                .build();

        return userRepository.save(user);
    }
}
