package com.elice.slowslow.domain.order.service;

import com.elice.slowslow.domain.delivery.Delivery;
import com.elice.slowslow.domain.delivery.DeliveryStatus;
import com.elice.slowslow.domain.delivery.dto.DeliveryResponse;
import com.elice.slowslow.domain.order.Order;
import com.elice.slowslow.domain.order.OrderStatus;
import com.elice.slowslow.domain.order.repository.OrderRepository;
import com.elice.slowslow.domain.orderDetail.OrderDetail;
import com.elice.slowslow.domain.order.dto.OrderPageResponse;
import com.elice.slowslow.domain.order.dto.OrderRequest;
import com.elice.slowslow.domain.order.dto.OrderResponse;
import com.elice.slowslow.domain.orderDetail.dto.OrderDetailDTO;
import com.elice.slowslow.domain.orderDetail.dto.OrderDetailRequest;
import com.elice.slowslow.domain.orderDetail.dto.OrderDetailResponse;
import com.elice.slowslow.domain.orderDetail.repository.OrderDetailRepository;
import com.elice.slowslow.domain.payment.Payment;
import com.elice.slowslow.domain.payment.dto.PaymentResponse;
import com.elice.slowslow.domain.product.Product;
import com.elice.slowslow.domain.product.repository.ProductRepository;
import com.elice.slowslow.domain.user.User;
import com.elice.slowslow.domain.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    private OrderPageResponse sessionOrderPageData;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, OrderDetailRepository orderDetailRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getAllOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public Order addOrder(OrderRequest orderRequest) {
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING) // 기본값: 결제 대기
                .totalPrice(0L)
                .build();

        List<OrderDetail> orderDetails = new ArrayList<>();
        long totalPrice = 0L;

        for (OrderDetailRequest detailRequest : orderRequest.getOrderDetails()) {
            Product product = productRepository.findById(detailRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

            OrderDetail detail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .productName(product.getName())
                    .productPrice(product.getPrice())
                    .productCnt(detailRequest.getProductCnt())
                    .totalPrice(product.getPrice() * detailRequest.getProductCnt())
                    .build();

            totalPrice += detail.getTotalPrice();
            orderDetails.add(detail);
        }

        order.setOrderDetails(orderDetails);

        Delivery delivery = Delivery.builder()
                .receiverName(orderRequest.getShipName())
                .receiverTel(orderRequest.getShipTel())
                .receiverAddr(orderRequest.getShipAddr())
                .shipReq(orderRequest.getShipReq())
                .status(DeliveryStatus.READY) // 기본값: 배송 준비중
                .build();

        order.setDelivery(delivery);

        //배송비 계산 및 총 금액 반영
        Long shippingFee = calculateShippingFee(totalPrice);
        Long finalTotalPrice = totalPrice + shippingFee;
        order.updateTotalPrice(finalTotalPrice);

        try {
            return orderRepository.save(order);
        } catch (Exception e) {
            logger.error("Error saving order: ", e);
            throw new RuntimeException("주문을 저장하는 중에 오류가 발생했습니다.");
        }
    }

//    public Order updateOrder(Long orderId, OrderRequest orderRequest) {
//        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
//
//        // 배송비 계산
//        Long shippingFee = calculateShippingFee(orderRequest.getTotalPrice());
//
//        // 총 주문 금액 계산
//        Long finalTotalPrice = orderRequest.getTotalPrice() + shippingFee;
//
//        order.setShipName(orderRequest.getShipName());
//        order.setShipTel(orderRequest.getShipTel());
//        order.setShipAddr(orderRequest.getShipAddr());
//        order.setShipReq(orderRequest.getShipReq());
//        order.setTotalPrice(finalTotalPrice); // 최종 금액 설정
//        order.setOrderName(orderRequest.getOrderName());
//        order.setOrderTel(orderRequest.getOrderTel());
//        order.setOrderEmail(orderRequest.getOrderEmail());
//        return orderRepository.save(order);
//    }

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

    @Transactional(readOnly = true)
    public OrderResponse getOrderResponse(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream().map(detail ->
                new OrderDetailResponse(
                    detail.getProductId(),
                    detail.getProductName(),
                    detail.getProductPrice(),
                    detail.getProductCnt(),
                    detail.getTotalPrice()
                )).toList();

        Delivery delivery = order.getDelivery();
        DeliveryResponse deliveryResponse = (delivery != null)
                ? new DeliveryResponse(
                delivery.getReceiverName(),
                delivery.getReceiverTel(),
                delivery.getReceiverAddr(),
                delivery.getShipReq(),
                delivery.getStatus().name()
        )
                : null;

        Payment payment = order.getPayment();
        PaymentResponse paymentResponse = (payment != null)
                ? new PaymentResponse(
                payment.getPaymentMethod(),
                payment.getPaymentKey(),
                payment.getAmount(),
                payment.getStatus().name()
        )
                : null;

        // LocalDateTime을 문자열로 변환
        String createdDateString = order.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getStatus().name(),
                order.getTotalPrice(),
                order.getOrderName(),
                order.getOrderTel(),
                order.getOrderEmail(),
                order.getCreatedDate(),
                orderDetailResponses,
                deliveryResponse,
                paymentResponse
        );
    }

    public OrderPageResponse createOrderPageData(Long userId, List<OrderDetailRequest> orderDetailRequests) {
        List<OrderDetailDTO> orderDetails = orderDetailRequests.stream()
                .map(detailRequest -> new OrderDetailDTO(
                        detailRequest.getProductId(),
                        detailRequest.getProductName(),
                        detailRequest.getProductPrice(),
                        detailRequest.getProductCnt()))
                .collect(Collectors.toList());

        Long totalPrice = orderDetails.stream()
                .mapToLong(detail -> {
                    Long price = detail.getProductPrice() * detail.getProductCnt();
                    logger.info("Product: {}, Price: {}, Quantity: {}, Subtotal: {}", detail.getProductName(), detail.getProductPrice(), detail.getProductCnt(), price);
                    return price;
                })
                .sum();

        // 배송비 계산
        Long shippingFee = calculateShippingFee(totalPrice);

        // 총 주문 금액 계산
        Long finalTotalPrice = totalPrice + shippingFee;

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
    private Long calculateShippingFee(Long totalPrice) {
        return totalPrice >= 50000L ? 0L : 3000L;
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
