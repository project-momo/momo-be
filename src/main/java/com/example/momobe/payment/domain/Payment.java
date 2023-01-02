package com.example.momobe.payment.domain;

import com.example.momobe.common.domain.BaseTime;
import com.example.momobe.payment.domain.enums.PayState;
import com.example.momobe.payment.domain.enums.PayType;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

import static com.example.momobe.payment.domain.enums.PayState.*;
import static java.util.UUID.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Builder
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
    }
}
