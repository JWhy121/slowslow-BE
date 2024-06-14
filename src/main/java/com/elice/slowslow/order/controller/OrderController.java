package com.elice.slowslow.order.controller;


import com.elice.slowslow.order.Order;
import com.elice.slowslow.order.dto.OrderPageResponse;
import com.elice.slowslow.order.dto.OrderRequest;
import com.elice.slowslow.order.dto.OrderResponse;
import com.elice.slowslow.order.service.OrderService;
import com.elice.slowslow.orderDetail.dto.OrderDetailRequest;
import com.elice.slowslow.user.User;
import com.elice.slowslow.user.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<OrderPageResponse> createOrderPageData(@RequestParam Long userId, @RequestBody List<OrderDetailRequest> orderDetails) {
        try {
            OrderPageResponse response = orderService.createOrderPageData(userId, orderDetails);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 주문 페이지 조회 : 주문 페이지 데이터를 가져오는 엔드포인트
    @GetMapping("orders/order-page")
    public ResponseEntity<OrderPageResponse> getOrderPageData(@RequestParam Long userId) {
        try {
            OrderPageResponse response = orderService.getOrderPageData(userId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/orders")
    public ResponseEntity<?> addOrder(@Valid @RequestBody OrderRequest orderRequest, BindingResult bindingResult,
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 수단에 체크해주세요.");
        }

        if (!agreementConfirmed) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("주문자 동의에 체크해주세요.");
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
    public ResponseEntity<OrderResponse> orderSuccess(@RequestParam Long orderId) {
        try {
            OrderResponse response = orderService.getOrderResponse(orderId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 주문 실패 페이지 조회
    @GetMapping("/orders/failure")
    public ResponseEntity<String> orderFail(@RequestParam Long orderId) {
        boolean result = orderService.setOrderFailed(orderId);
        if (result) {
            return ResponseEntity.ok("주문에 실패했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없습니다.");
        }
    }

    // 마이페이지 주문내역 조회
    @GetMapping("/mypage/orders")
    public ResponseEntity<?> getAllOrdersByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = orderService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        List<Order> orders = orderService.getAllOrdersByUserId(user.getId());
        return ResponseEntity.ok(orders);
    }

    // 마이페이지 주문 내역 상세 조회
    @GetMapping("/mypage/orders/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = orderService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        try {
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
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @Valid @RequestBody OrderRequest orderRequest, BindingResult bindingResult) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = orderService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // 추가적으로, 주문이 해당 사용자의 주문인지 확인하는 로직
        Order order = orderService.getOrderById(orderId);
        if (order == null || !order.getUser().getId().equals(user.getId())) {
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
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = orderService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // 추가적으로, 주문이 해당 사용자의 주문인지 확인하는 로직
        Order order = orderService.getOrderById(orderId);
        if (order == null || !order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
        }

        boolean result = orderService.cancelOrder(orderId);
        if (result) {
            return ResponseEntity.ok("주문이 취소되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없습니다.");
        }
    }
    /*
    예제 프론트엔드 코드(JavaScript)
    // 장바구니 데이터를 로컬 스토리지에서 가져오기
    const cartItems = JSON.parse(localStorage.getItem('cartItems'));

    // 백엔드로 장바구니 데이터 보내기
    fetch('http://localhost:8080/orders/create-order-page?userId=1', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cartItems)
    })
    .then(response => response.json())
    .then(data => {
        // 데이터를 받아와서 주문 페이지 렌더링
        renderOrderPage(data);
    })
    .catch(error => console.error('Error:', error));

    function renderOrderPage(orderPageData) {
        // 주문 페이지 렌더링 로직
        document.getElementById('orderTotalPrice').textContent = orderPageData.totalPrice;
        // 나머지 데이터들을 페이지에 삽입
    }
    */
}
