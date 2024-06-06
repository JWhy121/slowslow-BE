package com.elice.slowslow.order;

import com.elice.slowslow.orderDetail.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // 세션에 저장할 장바구니 데이터를 임시로 저장할 변수
    private OrderPageResponse sessionOrderPageData;

    public List<Order> getAllOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    //관리자 파트에서 아래를 쓸 수 있을 것 같습니다
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order addOrder(OrderRequest orderRequest) {
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = Order.builder()
                .user(user)
                .shipName(orderRequest.getShipName())
                .shipTel(orderRequest.getShipTel())
                .shipAddr(orderRequest.getShipAddr())
                .shipReq(orderRequest.getShipReq())
                .status("pending")
                .totalPrice(orderRequest.getTotalPrice())
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
        order.setShipName(orderRequest.getShipName());
        order.setShipTel(orderRequest.getShipTel());
        order.setShipAddr(orderRequest.getShipAddr());
        order.setShipReq(orderRequest.getShipReq());
        order.setTotalPrice(orderRequest.getTotalPrice());
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

        order.setStatus("cancelled");
        orderRepository.save(order);
        return true;
    }


    public boolean setOrderFailed(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return false;
        }

        order.setStatus("failed");
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

        OrderResponse response = new OrderResponse(
                order.getId(),
                order.getOrderEmail(),
                order.getOrderName(),
                order.getOrderTel(),
                order.getShipAddr(),
                order.getShipName(),
                order.getShipReq(),
                order.getShipTel(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getUser().getId(),
                detailResponses
        );

        return response;
    }

    public OrderPageResponse createOrderPageData(Long userId, List<OrderDetailRequest> orderDetailRequests) {
        // User 객체는 더 이상 사용되지 않음
        // User user = userRepository.findById(userId)
        //         .orElseThrow(() -> new RuntimeException("User not found"));

        // OrderDetailDTO 리스트 생성
        List<OrderDetailDTO> orderDetails = orderDetailRequests.stream()
                .map(detailRequest -> new OrderDetailDTO(
                        detailRequest.getProductId(),
                        detailRequest.getProductName(),
                        detailRequest.getProductPrice(),
                        detailRequest.getProductCnt(),
                        detailRequest.getOrderImg()))
                .collect(Collectors.toList());

        // 총 가격 계산
        int totalPrice = orderDetails.stream()
                .mapToInt(detail -> {
                    int price = detail.getProductPrice() * detail.getProductCnt();
                    logger.info("Product: {}, Price: {}, Quantity: {}, Subtotal: {}", detail.getProductName(), detail.getProductPrice(), detail.getProductCnt(), price);
                    return price;
                })
                .sum();

        // 디버깅 로그 추가
        logger.info("Calculated totalPrice: {}", totalPrice);

        // 세션에 데이터를 저장
        sessionOrderPageData = new OrderPageResponse(orderDetails, totalPrice);
        return sessionOrderPageData;
    }

    public OrderPageResponse getOrderPageData(Long userId) {
        // User 객체를 사용한 검증을 제거
        if (sessionOrderPageData == null) {
            throw new RuntimeException("장바구니 데이터가 없습니다.");
        }
        return sessionOrderPageData;
    }

//    // 특정 주문 상세 정보 조회
//    public Optional<OrderDetail> getOrderDetailById(Long id) {
//        return orderDetailRepository.findById(id);
//    }
//
//    // 주문 상세 정보 생성
//    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
//        return orderDetailRepository.save(orderDetail);
//    }
//
//    // 주문 상세 정보 업데이트
//    public Optional<OrderDetail> updateOrderDetail(Long id, OrderDetail orderDetail) {
//        return orderDetailRepository.findById(id).map(existingOrderDetail -> {
//            existingOrderDetail.setProductId(orderDetail.getProductId());
//            existingOrderDetail.setProductName(orderDetail.getProductName());
//            existingOrderDetail.setProductPrice(orderDetail.getProductPrice());
//            existingOrderDetail.setProductCnt(orderDetail.getProductCnt());
//            existingOrderDetail.setOrderImg(orderDetail.getOrderImg());
//            return orderDetailRepository.save(existingOrderDetail);
//        });
//    }
//
//    // 주문 상세 정보 삭제
//    public boolean deleteOrderDetail(Long id) {
//        return orderDetailRepository.findById(id).map(orderDetail -> {
//            orderDetailRepository.delete(orderDetail);
//            return true;
//        }).orElse(false);
//    }
}