package com.elice.slowslow.orderDetail;

import com.elice.slowslow.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrder(Order order);
    @Query("SELECT od FROM OrderDetail od WHERE od.order.user.id = :userId")
    List<OrderDetail> findByOrderUserId(@Param("userId") Long userId);
}
