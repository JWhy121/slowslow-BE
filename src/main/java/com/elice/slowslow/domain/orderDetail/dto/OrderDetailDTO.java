package com.elice.slowslow.domain.orderDetail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDetailDTO {
    private Long productId;
    private String productName;
    private Long productPrice;
    private int productCnt;

}