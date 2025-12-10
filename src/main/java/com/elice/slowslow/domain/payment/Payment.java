package com.elice.slowslow.domain.payment;


import com.elice.slowslow.domain.order.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "payment_key", unique = true)
    private String paymentKey; // Toss 등 PG사 결제 고유 키

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod; // CARD, TOSS, KAKAO 등

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "amount", nullable = false)
    private Long amount; // 결제 금액

    @CreationTimestamp
    @Column(name = "approved_at")
    private LocalDateTime approvedAt; // 결제 승인 시각

    // ====== 연관 로직 ======
    public void success(String paymentKey) {
        this.paymentKey = paymentKey;
        this.status = PaymentStatus.SUCCESS;
        this.approvedAt = LocalDateTime.now();
    }

    public void fail() {
        this.status = PaymentStatus.FAIL;
    }
}
