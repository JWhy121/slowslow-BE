package com.elice.slowslow.domain.order.dto;

import com.elice.slowslow.domain.orderDetail.dto.OrderDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderPageResponse {
    private List<OrderDetailDTO> orderDetails;
    private Long totalPrice;
}