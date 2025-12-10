package com.elice.slowslow.domain.auth;

import com.elice.slowslow.domain.user.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Map;
import java.util.Set;

public class CustomOAuth2User extends DefaultOAuth2User {

    private final User user;

    public CustomOAuth2User(User user, Map<String, Object> attributes) {
        super(Set.of(new SimpleGrantedAuthority("ROLE_USER")), attributes, "id");
        this.user = user;
    }

    public User getUser() { return user; }
}
