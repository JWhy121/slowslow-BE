package com.elice.slowslow.order.service;

import com.elice.slowslow.order.*;
import com.elice.slowslow.order.dto.OrderPageResponse;
import com.elice.slowslow.order.dto.OrderRequest;
import com.elice.slowslow.order.dto.OrderResponse;
import com.elice.slowslow.order.repository.OrderRepository;
import com.elice.slowslow.orderDetail.*;
import com.elice.slowslow.orderDetail.dto.OrderDetailDTO;
import com.elice.slowslow.orderDetail.dto.OrderDetailRequest;
import com.elice.slowslow.orderDetail.dto.OrderDetailResponse;
import com.elice.slowslow.orderDetail.repository.OrderDetailRepository;
import com.elice.slowslow.user.User;
import com.elice.slowslow.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    private OrderPageResponse sessionOrderPageData;

    public List<Order> getAllOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order addOrder(OrderRequest orderRequest) {
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 배송비 계산
        int shippingFee = calculateShippingFee(orderRequest.getTotalPrice());

        // 총 주문 금액 계산
        int finalTotalPrice = orderRequest.getTotalPrice() + shippingFee;

        Order order = Order.builder()
                .user(user)
                .shipName(orderRequest.getShipName())
                .shipTel(orderRequest.getShipTel())
                .shipAddr(orderRequest.getShipAddr())
                .shipReq(orderRequest.getShipReq())
                .status(OrderStatus.PENDING)  // 기본값 설정
                .totalPrice(finalTotalPrice)  // 최종 금액 설정
                .orderName(orderRequest.getOrderName())
                .orderTel(orderRequest.getOrderTel())
                .orderEmail(orderRequest.getOrderEmail())
                .orderDetails(orderRequest.getOrderDetails().stream().map(detailRequest -> OrderDetail.builder()
                        .productId(detailRequest.getProductId())
                        .productName(detailRequest.getProductName())
                        .productPrice(detailRequest.getProductPrice())
                        .productCnt(detailRequest.getProductCnt())
                        .orderImg(detailRequest.getOrderImg())
                        .order(null) // order는 나중에 설정
                        .build()).collect(Collectors.toList()))
                .build();

        order.getOrderDetails().forEach(detail -> detail.setOrder(order)); // order 설정

        try {
            return orderRepository.save(order);
        } catch (Exception e) {
            logger.error("Error saving order: ", e);
            throw new RuntimeException("주문을 저장하는 중에 오류가 발생했습니다.");
        }
    }

    public Order updateOrder(Long orderId, OrderRequest orderRequest) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        // 배송비 계산
        int shippingFee = calculateShippingFee(orderRequest.getTotalPrice());

        // 총 주문 금액 계산
        int finalTotalPrice = orderRequest.getTotalPrice() + shippingFee;

        order.setShipName(orderRequest.getShipName());
        order.setShipTel(orderRequest.getShipTel());
        order.setShipAddr(orderRequest.getShipAddr());
        order.setShipReq(orderRequest.getShipReq());
        order.setTotalPrice(finalTotalPrice); // 최종 금액 설정
        order.setOrderName(orderRequest.getOrderName());
        order.setOrderTel(orderRequest.getOrderTel());
        order.setOrderEmail(orderRequest.getOrderEmail());
        return orderRepository.save(order);
    }

    public boolean cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return false;
        }

        order.setStatus(OrderStatus.CANCELLED);  // 변경된 부분
        orderRepository.save(order);
        return true;
    }

    public boolean setOrderFailed(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return false;
        }

        order.setStatus(OrderStatus.FAILED);  // 변경된 부분
        orderRepository.save(order);
        return true;
    }

    public OrderResponse getOrderResponse(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderDetailResponse> detailResponses = order.getOrderDetails().stream().map(detail -> {
            OrderDetailResponse detailResponse = new OrderDetailResponse(
                    detail.getId(),
                    detail.getProductId(),
                    detail.getProductName(),
                    detail.getProductPrice(),
                    detail.getProductCnt(),
                    detail.getOrderImg()
            );
            return detailResponse;
        }).collect(Collectors.toList());

        // LocalDateTime을 문자열로 변환
        String createdDateString = order.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        OrderResponse response = new OrderResponse(
                order.getId(),
                createdDateString, // 추가된 부분
                order.getOrderEmail(),
                order.getOrderName(),
                order.getOrderTel(),
                order.getShipAddr(),
                order.getShipName(),
                order.getShipReq(),
                order.getShipTel(),
                order.getStatus().name(),
                order.getTotalPrice(),
                order.getUser().getId(),
                detailResponses
        );

        return response;
    }

    public OrderPageResponse createOrderPageData(Long userId, List<OrderDetailRequest> orderDetailRequests) {
        List<OrderDetailDTO> orderDetails = orderDetailRequests.stream()
                .map(detailRequest -> new OrderDetailDTO(
                        detailRequest.getProductId(),
                        detailRequest.getProductName(),
                        detailRequest.getProductPrice(),
                        detailRequest.getProductCnt(),
                        detailRequest.getOrderImg()))
                .collect(Collectors.toList());

        int totalPrice = orderDetails.stream()
                .mapToInt(detail -> {
                    int price = detail.getProductPrice() * detail.getProductCnt();
                    logger.info("Product: {}, Price: {}, Quantity: {}, Subtotal: {}", detail.getProductName(), detail.getProductPrice(), detail.getProductCnt(), price);
                    return price;
                })
                .sum();

        // 배송비 계산
        int shippingFee = calculateShippingFee(totalPrice);

        // 총 주문 금액 계산
        int finalTotalPrice = totalPrice + shippingFee;

        logger.info("Calculated totalPrice: {}, with shippingFee: {}", totalPrice, shippingFee);

        sessionOrderPageData = new OrderPageResponse(orderDetails, finalTotalPrice);
        return sessionOrderPageData;
    }

    public OrderPageResponse getOrderPageData(Long userId) {
        if (sessionOrderPageData == null) {
            throw new RuntimeException("장바구니 데이터가 없습니다.");
        }
        return sessionOrderPageData;
    }

    // 배송비 계산 로직
    private int calculateShippingFee(int totalPrice) {
        return totalPrice >= 50000 ? 0 : 3000;
    }

    // 모든 주문 조회
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 주문 상태 업데이트
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
