package com.elice.slowslow.cart.Repository;

import com.elice.slowslow.cart.domain.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartProduct, Long>{

    Optional<CartProduct> findBycartProductId(Long id);
}
