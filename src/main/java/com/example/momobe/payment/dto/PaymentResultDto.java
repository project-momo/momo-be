package com.example.momobe.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PaymentResultDto {
    private final String mId;
    private final String version;
    private final String paymentKey;
    private final String orderId;
    private final String orderName;
    private final String currency;
    private final String method;
    private final String totalAmount;
    private final String balanceAmount;
    private final String suppliedAmount;
    private final String vat;
    private final String status;
    private final String requestedAt;
    private final String approvedAt;
    private final String useEscrow;
    private final String cultureExpense;
    private final String type;
    private final PaymentCardDto card;
    private final PaymentCardCancelDto cancels;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PaymentCardDto {
        private final String company;
        private final String number;
        private final String installmentPlanMonth;
        private final String isInterestFree;
        private final String approveNo;
        private final String useCardPoint;
        private final String cardType;
        private final String ownerType;
        private final String acquireStatus;
        private final String receiptUrl;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    static class PaymentCardCancelDto {
        String money;
    }
}
