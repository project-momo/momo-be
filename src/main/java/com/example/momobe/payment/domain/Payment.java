package com.example.momobe.payment.domain;

import com.example.momobe.common.domain.BaseTime;
import com.example.momobe.payment.domain.enums.PayState;
import com.example.momobe.payment.domain.enums.PayType;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

import static com.example.momobe.payment.domain.enums.PayState.BEFORE;
import static com.example.momobe.payment.domain.enums.PayState.CANCEL;
import static java.util.UUID.randomUUID;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Payment extends BaseTime {
    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private PayType payType;

    private Long amount;

    private String orderId;

    private String orderName;

    private String customerEmail;

    private String customerName;

    private String successUrl;

    private String failUrl;

    private PayState payState;

    private Long userId;

    private Long reservationId;

    private String createDate;

    private String paymentKey;

    private String memo;

    public Payment(PayType payType, Long amount, String customerEmail, String customerName, Long userId, Long reservationID, String successUrl, String failUrl, String orderName) {
        this.orderId = randomUUID().toString();
        this.createDate = LocalDate.now().toString();
        this.payType = payType;
        this.amount = amount;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.userId = userId;
        this.reservationId = reservationID;
        this.successUrl = successUrl;
        this.failUrl = failUrl;
        this.orderName = orderName;
        this.payState = BEFORE;
        this.memo = "";
    }

    public Boolean matchAmount(Long amount) {
        return Objects.equals(this.amount, amount);
    }

    public void cancel() {
        this.payState = CANCEL;
    }

    public void setPaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }

    public void changePaymentState(PayState state) {
        this.payState = state;
    }
}
