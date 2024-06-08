package com.elice.slowslow.order;

import com.elice.slowslow.orderDetail.OrderDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderPageResponse {
    private List<OrderDetailDTO> orderDetails;
    private int totalPrice;
}