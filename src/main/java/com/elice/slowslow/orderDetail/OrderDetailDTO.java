package com.elice.slowslow.orderDetail;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDetailDTO {
    private Long productId;
    private String productName;
    private int productPrice;
    private int productCnt;
    private String orderImg;


}