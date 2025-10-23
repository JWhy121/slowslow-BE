package com.elice.slowslow.domain.product.controller;

import com.elice.slowslow.domain.product.dto.ProductDto;
import com.elice.slowslow.domain.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("admin/product/create")
    public ProductDto createProduct(@RequestBody ProductDto productDto) {

        return productService.createProduct(productDto);
    }

    @PutMapping("admin/product/update/{productId}")
    public ProductDto updateProduct(@PathVariable Long productId, @RequestBody ProductDto productDto) {

        return  productService.updatePost(productId, productDto);
    }



    @DeleteMapping("admin/product/delete/{productId}")
    public void deleteProduct(@PathVariable Long productId) {

        productService.deleteProduct(productId);

    }

    @GetMapping("product/{productId}")
    public ProductDto getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @GetMapping("product/all")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("product/latest")
    public List<ProductDto> getLatestProducts() {
        return productService.getLatestProducts();
    }

    @GetMapping("product/modify")
    public List<ProductDto> getRecentlyModifiedProducts() {
        return productService.getRecentlyModifiedProducts();
    }


}

