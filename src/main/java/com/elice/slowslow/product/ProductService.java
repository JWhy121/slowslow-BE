package com.elice.slowslow.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class ProductService {
    @Autowired // required .. 찾아봐서 바꾸기
    private ProductRepository productRepository;


    public void createProduct(Long boardId, Long categoryId, ProductDto productDto) {

        Product product = Product.fromDto(productDto);
       productRepository.save(product);
    }

    @Transactional
    public void updatePost(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
           product.setName(productDto.getName());
           product.setPrice(product.getPrice());
           product.setDescription(product.getDescription());
           product.setImageLink(product.getImageLink());
           productRepository.save(product);
        }
    }


    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

}
