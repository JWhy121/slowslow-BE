package com.elice.slowslow.orderDetail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDetailResponse {
    private Long id;
    private Long productId;
    private String productName;
    private int productPrice;
    private int productCnt;
    private String orderImg;
}