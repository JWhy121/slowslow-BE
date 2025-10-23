package com.elice.slowslow.domain.order.dto;

import com.elice.slowslow.domain.delivery.dto.DeliveryResponse;
import com.elice.slowslow.domain.orderDetail.dto.OrderDetailResponse;
import com.elice.slowslow.domain.payment.dto.PaymentResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Long userId;
    private String orderStatus;
    private Long totalPrice;
    private LocalDateTime createdAt;

    private List<OrderDetailResponse> orderDetails;
    private DeliveryResponse delivery;
    private PaymentResponse payment;
}