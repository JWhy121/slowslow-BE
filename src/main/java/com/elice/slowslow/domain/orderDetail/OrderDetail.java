package com.elice.slowslow.domain.orderDetail;

import com.elice.slowslow.domain.product.Product;
import com.elice.slowslow.global.audit.BaseEntity;
import com.elice.slowslow.domain.order.Order;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_detail")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnoreProperties("orderDetails")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_price", nullable = false)
    private Long productPrice;

    @Column(name = "product_cnt", nullable = false)
    private Integer productCnt;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    public static OrderDetail create(Product product, int quantity, Order order) {
        return OrderDetail.builder()
                .product(product)
                .productName(product.getName())
                .productPrice(product.getPrice())
                .productCnt(quantity)
                .totalPrice(product.getPrice() * quantity)
                .order(order)
                .build();
    }

    public Long getProductId() {
        return product != null ? product.getId() : null;
    }
}