package com.elice.slowslow.product.controller;

import com.elice.slowslow.product.dto.ProductDto;
import com.elice.slowslow.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProductController {


    private ProductService productService;


    @GetMapping("/create/{brandId}/{categoryId}/new")
    public String showCreateProduct(@PathVariable Long brandId, @PathVariable Long categoryId, Model model) {

        model.addAttribute("brandId", brandId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("posts", new ProductDto());
        return "createProduct";//프론트 페이지
    }

    @PostMapping("/create/{brandId}/{categoryId}")
    public String createProduct(@PathVariable Long brandId, @PathVariable Long categoryId, @ModelAttribute ProductDto productDto) {

        productService.createProduct(brandId, categoryId, productDto);

        return "managerPage"; // 관리자 페이지로 이동
    }

    @GetMapping("/product/edit/{productId}")
    public String showEditProduct(@PathVariable Long productId, Model model) {
        // 상품 상세 페이지에서 수정 페이지로 넘어가게 할 예정
        ProductDto product = productService.getProductById(productId);
        model.addAttribute("products", product);
        return "editProduct";
    }

    @PostMapping("/product/edit/{productId}")
    public String editProduct(@PathVariable Long productId, @ModelAttribute ProductDto productDto) {
        productService.updatePost(productId, productDto);
        return "productDetail"; // 수정한 페이지로 이동하게 할 예정.
    }

    @DeleteMapping("/product/delete/{productId}")
    public String deleteProduct(@PathVariable Long productId) {
        // 상품 상세 페이지에서 삭제하게끔?
        productService.deleteProduct(productId);
        return "productPage"; // 상세 페이지 들어가기 전 페이지로
    }


    @GetMapping("product/{productId}")
    public String showProductDetail(@PathVariable Long productId, Model model) {
        // brandId, categoryId 가져오게 할 예정

        ProductDto product = productService.getProductById(productId);

        model.addAttribute("products", product);

        return "productDetail";
    }



}
