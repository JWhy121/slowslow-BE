package com.elice.slowslow.user.jwt;

public interface JWTConfig {
    long EXPIRATION = 600000;
    String SECRET = "rrpppglflldldldlkflfkjdlksdjccxcznwweordslkklj";
    String TOKEN_PREFIX ="Bearer ";
    String AUTH_HEADER = "Authorization";
}
