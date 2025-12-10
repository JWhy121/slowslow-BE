package com.elice.slowslow.domain.orderDetail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDetailResponse {
    private Long productId;
    private String productName;
    private Long productPrice;
    private Integer productCnt;
    private Long totalPrice;
}