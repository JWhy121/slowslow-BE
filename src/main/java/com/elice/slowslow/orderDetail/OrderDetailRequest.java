package com.elice.slowslow.orderDetail;

import lombok.Data;

@Data
public class OrderDetailRequest {
    private Long productId;
    private String productName;
    private int productPrice;
    private int productCnt;
    private String orderImg;
}