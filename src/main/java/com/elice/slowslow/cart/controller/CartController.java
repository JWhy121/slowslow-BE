package com.elice.slowslow.cart.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.elice.slowslow.cart.service.CartService;
import com.elice.slowslow.cart.domain.CartProduct;

@RestController
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService){
        this.cartService = cartService;
    }

    @GetMapping("/api/hello")
    public String hello() {
        return "테스트입니다.";
    }

    @GetMapping("/cart/{id}")
    public ResponseEntity<CartProduct> getProductById(@PathVariable(value = "id") Long id){
        CartProduct cartProduct = cartService.getProductById(id);
        if (cartProduct != null) {
            return ResponseEntity.ok(cartProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
