package com.elice.slowslow.cart.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    @GetMapping("/api/hello")
    public String hello() {
        return "테스트입니다.";
    }

}
