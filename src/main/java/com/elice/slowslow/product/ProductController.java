package com.elice.slowslow.product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProductController {


    private ProductService productService;


    @GetMapping("/create/{boardId}/{categoryId}/new")
    public String showCreateProduct(@PathVariable Long boardId, @PathVariable Long categoryId, Model model) {

        model.addAttribute("boardId", boardId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("posts", new ProductDto());
        return "createProduct";//프론트 페이지
    }

    @PostMapping("/create/{boardId}/{categoryId}")
    public String createProduct(@PathVariable Long boardId, @PathVariable Long categoryId, @ModelAttribute ProductDto productDto) {

        productService.createProduct(boardId, categoryId, productDto);

        return "redirect:/managerPage"; // 관리자 페이지로 이동
    }

    @GetMapping("/product/edit/{productId}")
    public String showEditProduct(@PathVariable Long postId, Model model) {
        //나중에 수정, service에 추가
        //ProductDto product = productService. get
        //model.addAttribute("products", product);
        return "editProduct";
    }

    @PostMapping("/product/edit/{productId}")
    public String editProduct(@PathVariable Long productId, @ModelAttribute ProductDto productDto) {
        productService.updatePost(productId, productDto);
        return "productDetail"; // 수정한 페이지로 이동하게 할 예정.
    }

    @DeleteMapping("/product/delete/{productId}")
    public String deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return "redirect:/managerPage";
    }


    @GetMapping("product/{productId}")
    public String showProductDetail(@PathVariable Long productId, Model model) {
        // brandId, categoryId 가져오게 할 예정

        return "productDetail";
    }

}
