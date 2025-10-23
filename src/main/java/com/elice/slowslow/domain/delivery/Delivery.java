package com.elice.slowslow.domain.delivery;

import com.elice.slowslow.domain.order.Order;
import com.elice.slowslow.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Table(name = "delivery")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    @Column(name = "receiver_tel", nullable = false)
    private String receiverTel;

    @Column(name = "receiver_addr", nullable = false)
    private String receiverAddr;

    @Column(name = "ship_req")
    private String shipReq; // 요청사항

    @Column(name = "tracking_number")
    private String trackingNumber; // 송장번호

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus status;

}
