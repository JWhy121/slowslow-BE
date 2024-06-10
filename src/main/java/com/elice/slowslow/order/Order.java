package com.elice.slowslow.order;

import com.elice.slowslow.audit.BaseEntity;
import com.elice.slowslow.orderDetail.OrderDetail;
import com.elice.slowslow.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("orders")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("order")
    private List<OrderDetail> orderDetails;

    @Column(name = "ship_name", nullable = false, length = 20)
    private String shipName;

    @Column(name = "ship_tel", nullable = false, length = 50)
    private String shipTel;

    @Column(name = "ship_addr", nullable = false, length = 200)
    private String shipAddr;

    @Column(name = "ship_req", length = 200)
    private String shipReq;

    // order 삭제 시에 status = 'CANCELLED'
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private OrderStatus status;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @Column(name = "order_name", nullable = false, length = 20)
    private String orderName;

    @Column(name = "order_tel", nullable = false, length = 50)
    private String orderTel;

    @Column(name = "order_email", nullable = false, length = 50)
    private String orderEmail;

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = OrderStatus.PENDING;
        }
    }
}