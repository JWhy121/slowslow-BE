package com.elice.slowslow.domain.orderDetail.dto;

import lombok.Data;

@Data
public class OrderDetailRequest {
    private Long productId;
    private String productName;
    private Long productPrice;
    private int productCnt;
}