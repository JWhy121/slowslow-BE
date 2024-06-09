package com.elice.slowslow.order;

import com.elice.slowslow.orderDetail.OrderDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderEmail;
    private String orderName;
    private String orderTel;
    private String shipAddr;
    private String shipName;
    private String shipReq;
    private String shipTel;
    private String status;
    private int totalPrice;
    private Long userId;
    private List<OrderDetailResponse> orderDetails;
}