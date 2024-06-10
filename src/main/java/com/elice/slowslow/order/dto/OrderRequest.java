package com.elice.slowslow.order.dto;

import com.elice.slowslow.orderDetail.dto.OrderDetailRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @NotNull(message = "사용자 ID는 필수 입력값입니다.")
    private Long userId;

    @NotEmpty(message = "배송 받는 사람 이름은 필수 입력값입니다.")
    private String shipName;

    @NotEmpty(message = "배송 받는 사람 전화번호는 필수 입력값입니다.")
    private String shipTel;

    @NotEmpty(message = "배송 주소는 필수 입력값입니다.")
    private String shipAddr;

    private String shipReq;

    @NotNull(message = "총 가격은 필수 입력값입니다.")
    private int totalPrice;

    @NotEmpty(message = "주문자 이름은 필수 입력값입니다.")
    private String orderName;

    @NotEmpty(message = "주문자 전화번호는 필수 입력값입니다.")
    private String orderTel;

    @Email(message = "잘못된 이메일 형식입니다.")
    @NotEmpty(message = "주문자 이메일은 필수 입력값입니다.")
    private String orderEmail;

    @NotNull(message = "주문 상세 정보는 필수 입력값입니다.")
    private List<OrderDetailRequest> orderDetails;
}