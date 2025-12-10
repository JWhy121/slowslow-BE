package com.elice.slowslow.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private String paymentMethod;
    private String paymentKey;
    private Long amount;
    private String paymentStatus;
}