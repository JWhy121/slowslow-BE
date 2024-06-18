package com.elice.slowslow.order.controller;

import com.elice.slowslow.order.Order;
import com.elice.slowslow.order.dto.OrderPageResponse;
import com.elice.slowslow.order.dto.OrderRequest;
import com.elice.slowslow.order.dto.OrderResponse;
import com.elice.slowslow.order.service.OrderService;
import com.elice.slowslow.orderDetail.dto.OrderDetailRequest;
import com.elice.slowslow.user.User;
import com.elice.slowslow.user.dto.CustomUserDetails;
import com.elice.slowslow.user.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // 주문 페이지 생성 : 장바구니 데이터를 받아서 주문 페이지를 생성하는 엔드포인트
    // 주문 페이지를 불러올 때, 장바구니 데이터는 서버에 저장하지 않고 프론트엔드에서 관리
    @PostMapping("orders/create-order-page")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderPageResponse> createOrderPageData(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @RequestBody List<OrderDetailRequest> orderDetails) {
        try {
            User user = orderService.findByUsername(userDetails.getUsername());
            OrderPageResponse response = orderService.createOrderPageData(user.getId(), orderDetails);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 주문 페이지 조회 : 주문 페이지 데이터를 가져오는 엔드포인트
    @GetMapping("orders/order-page")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderPageResponse> getOrderPageData(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            User user = orderService.findByUsername(userDetails.getUsername());
            OrderPageResponse response = orderService.getOrderPageData(user.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @Valid @RequestBody OrderRequest orderRequest, BindingResult bindingResult,
                                      @RequestParam boolean paymentConfirmed,
                                      @RequestParam boolean agreementConfirmed) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> {
                errorMessage.append(error.getDefaultMessage()).append("\n");
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
        }

        if (!paymentConfirmed) {
            return ResponseEntity.badRequest().body("결제 수단에 체크해주세요.");
        }

        if (!agreementConfirmed) {
            return ResponseEntity.badRequest().body("주문자 동의에 체크해주세요.");
        }

        try {
            Order order = orderService.addOrder(orderRequest);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            logger.error("Error processing order: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문을 처리하는 중에 오류가 발생했습니다.");
        }
    }

    // 주문 성공 페이지 조회
    @GetMapping("/orders/success")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> orderSuccess(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long orderId) {
        try {
            orderService.getOrderResponse(orderId);
            return ResponseEntity.ok("주문에 성공하였습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 주문 실패 페이지 조회
    @GetMapping("/orders/failure")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> orderFail(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long orderId) {
        boolean result = orderService.setOrderFailed(orderId);
        if (result) {
            return ResponseEntity.ok("주문에 실패했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없습니다.");
        }
    }

    // 마이페이지 주문내역 조회
    @GetMapping("/mypage/orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllOrdersByUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            User user = orderService.findByUsername(userDetails.getUsername());
            List<Order> orders = orderService.getAllOrdersByUserId(user.getId());
            return ResponseEntity.ok(orders);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문 내역을 찾을 수 없습니다.");
        }
    }

    // 마이페이지 주문 내역 상세 조회
    @GetMapping("/mypage/orders/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOrderById(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long orderId) {
        try {
            User user = orderService.findByUsername(userDetails.getUsername());
            OrderResponse response = orderService.getOrderResponse(orderId);

            // 추가적으로, 주문이 해당 사용자의 주문인지 확인하는 로직
            if (!response.getUserId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
            }

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없습니다.");
        }
    }

    // 마이페이지 주문 정보 수정
    @PutMapping("/mypage/orders/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @PathVariable Long orderId, @Valid @RequestBody OrderRequest orderRequest,
                                         BindingResult bindingResult) {
        Order order = orderService.getOrderById(orderId);
        if (order == null || !order.getUser().getId().equals(orderService.findByUsername(userDetails.getUsername()).getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
        }

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> {
                errorMessage.append(error.getDefaultMessage()).append("\n");
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
        }

        try {
            Order updatedOrder = orderService.updateOrder(orderId, orderRequest);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            logger.error("Error updating order: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문을 수정하는 중에 오류가 발생했습니다.");
        }
    }

    // 마이페이지 주문 취소
    @DeleteMapping("/mypage/orders/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cancelOrder(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null || !order.getUser().getId().equals(orderService.findByUsername(userDetails.getUsername()).getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
        }

        boolean result = orderService.cancelOrder(orderId);
        if (result) {
            return ResponseEntity.ok("주문이 취소되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없습니다.");
        }
    }
}
