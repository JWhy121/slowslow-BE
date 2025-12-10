package com.elice.slowslow.domain.order.controller;

import com.elice.slowslow.domain.order.Order;
import com.elice.slowslow.domain.order.OrderStatus;
import com.elice.slowslow.domain.order.dto.OrderRequest;
import com.elice.slowslow.domain.order.dto.OrderResponse;
import com.elice.slowslow.domain.order.service.OrderService;
import com.elice.slowslow.domain.user.User;
import com.elice.slowslow.domain.user.dto.CustomUserDetails;
import com.elice.slowslow.domain.user.dto.UserDTO;
import com.elice.slowslow.domain.user.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }


    @PostMapping("/orders")
    @PreAuthorize("hasRole('ROLE_USER')")
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
    @GetMapping("/users/me")
    public ResponseEntity<UserDTO> getUserDetails(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            logger.error("UserDetails is null");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        logger.info("UserDetails: " + userDetails);
        UserDTO userDTO = userService.findByName(userDetails.getUsername());
        return ResponseEntity.ok(userDTO);
    }

    // 현재 사용되지 않지만 추후 필요성을 생각해 남겨뒀습니다.
    // 주문 성공 페이지 조회
//    @GetMapping("/orders/success")
//    @PreAuthorize("hasRole('ROLE_USER')")
//    public ResponseEntity<String> orderSuccess(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long orderId) {
//        try {
//            orderService.getOrderResponse(orderId);
//            return ResponseEntity.ok("주문에 성공하였습니다.");
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }

    // 현재 사용되지 않지만 추후 필요성을 생각해 남겨뒀습니다.
    // 주문 실패 페이지 조회
    @GetMapping("/orders/failure")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> orderFail(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long orderId) {
        boolean result = orderService.setOrderFailed(orderId);
        if (result) {
            return ResponseEntity.ok("주문에 실패했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없습니다.");
        }
    }

    // 마이페이지 주문내역 조회
    @GetMapping("api/v1/mypage/orders")
    @PreAuthorize("hasRole('ROLE_USER')")
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
    @GetMapping("api/v1/mypage/orders/{orderId}")
    @PreAuthorize("hasRole('ROLE_USER')")
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
//    @PutMapping("api/v1/mypage/orders/{orderId}")
//    @PreAuthorize("hasRole('ROLE_USER')")
//    public ResponseEntity<?> updateOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
//                                         @PathVariable Long orderId, @Valid @RequestBody OrderRequest orderRequest,
//                                         BindingResult bindingResult) {
//        Order order = orderService.getOrderById(orderId);
//        if (order == null || !order.getUser().getId().equals(orderService.findByUsername(userDetails.getUsername()).getId())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
//        }
//
//        if (bindingResult.hasErrors()) {
//            StringBuilder errorMessage = new StringBuilder();
//            bindingResult.getFieldErrors().forEach(error -> {
//                errorMessage.append(error.getDefaultMessage()).append("\n");
//            });
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
//        }
//
//        try {
//            Order updatedOrder = orderService.updateOrder(orderId, orderRequest);
//            return ResponseEntity.ok(updatedOrder);
//        } catch (RuntimeException e) {
//            logger.error("Error updating order: ", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문을 수정하는 중에 오류가 발생했습니다.");
//        }
//    }

    // 마이페이지 주문 취소
    @DeleteMapping("api/v1/mypage/orders/{orderId}")
    @PreAuthorize("hasRole('ROLE_USER')")
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

    // 모든 회원들의 주문 내역을 조회
    @GetMapping("/admin/orders")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (RuntimeException e) {
            logger.error("Error retrieving orders: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 사용자의 주문 내역에서 배송 상태를 수정
    @PutMapping("/admin/orders/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            logger.error("Error updating order status: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update order status.");
        }
    }

    // 회원들의 주문 내역을 삭제 (soft delete로 상태를 CANCELLED로 변경)
    @DeleteMapping("/admin/orders/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            boolean result = orderService.cancelOrder(id);
            if (result) {
                return ResponseEntity.ok("Order has been cancelled.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
            }
        } catch (RuntimeException e) {
            logger.error("Error cancelling order: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to cancel order.");
        }
    }
}
