package com.elice.slowslow.cart.service;

import com.elice.slowslow.cart.Repository.CartRepository;
import com.elice.slowslow.cart.domain.CartProduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository)
    {
        this.cartRepository = cartRepository;
    }

    public CartProduct getProductById(Long id){
        CartProduct cartProduct = cartRepository.findById(id).orElse(null);
        return cartProduct;
    }
}
