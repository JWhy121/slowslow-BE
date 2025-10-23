package com.elice.slowslow.domain.user.jwt;

public interface JWTConfig {
    long EXPIRATION = 1000 * 60 * 60; //한 시간 유효
    String SECRET = "rrpppglflldldldlkflfkjdlksdjccxcznwweordslkklj";
    String TOKEN_PREFIX ="Bearer ";
    String AUTH_HEADER = "Authorization";
}
