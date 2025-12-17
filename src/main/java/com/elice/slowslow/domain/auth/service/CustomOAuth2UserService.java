package com.elice.slowslow.domain.auth.service;

import com.elice.slowslow.domain.user.repository.UserRepository;
import com.elice.slowslow.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final OAuth2UserProcessService oAuth2UserProcessService;

    public CustomOAuth2UserService(OAuth2UserProcessService oAuth2UserProcessService) {
        this.oAuth2UserProcessService = oAuth2UserProcessService;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        return oAuth2UserProcessService.processOAuthUser(oAuth2User);
    }
}
