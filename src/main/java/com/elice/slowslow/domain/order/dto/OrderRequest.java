package com.elice.slowslow.domain.order.dto;

import com.elice.slowslow.domain.orderDetail.dto.OrderDetailRequest;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @NotNull(message = "사용자 ID는 필수 입력값입니다.")
    private Long userId;

    //배송 정보
    @NotBlank(message = "배송 받는 사람 이름은 필수 입력값입니다.")
    private String shipName;

    @NotBlank(message = "배송 받는 사람 전화번호는 필수 입력값입니다.")
    private String shipTel;

    @NotBlank(message = "배송 주소는 필수 입력값입니다.")
    private String shipAddr;

    private String shipReq;

    //주문 요약
    @NotNull(message = "총 가격은 필수 입력값입니다.")
    private Long totalPrice;

    //주문자 정보
    @NotBlank(message = "주문자 이름은 필수 입력값입니다.")
    private String orderName;

    @NotBlank(message = "주문자 전화번호는 필수 입력값입니다.")
    private String orderTel;

    @Email(message = "잘못된 이메일 형식입니다.")
    @NotBlank(message = "주문자 이메일은 필수 입력값입니다.")
    private String orderEmail;

    @NotNull(message = "주문 상세 정보는 필수 입력값입니다.")
    @Size(min = 1, message = "하나 이상의 상품이 필요합니다.")
    private List<OrderDetailRequest> orderDetails;
}