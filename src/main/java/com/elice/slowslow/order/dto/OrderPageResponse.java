package com.elice.slowslow.order.dto;

import com.elice.slowslow.orderDetail.dto.OrderDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderPageResponse {
    private List<OrderDetailDTO> orderDetails;
    private int totalPrice;
}