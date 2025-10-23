package com.elice.slowslow.domain.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryResponse {
    private String receiverName;
    private String receiverTel;
    private String receiverAddr;
    private String deliveryRequest;
    private String deliveryStatus;
}