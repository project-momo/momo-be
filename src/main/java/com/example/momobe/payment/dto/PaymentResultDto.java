package com.example.momobe.payment.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentResultDto {
    private String mId;
    private String version;
    private String paymentKey;
    private String orderId;
    private String orderName;
    private String currency;
    private String method;
    private String totalAmount;
    private String balanceAmount;
    private String suppliedAmount;
    private String vat;
    private String status;
    private String requestedAt;
    private String approvedAt;
    private String useEscrow;
    private String cultureExpense;
    private String type;
    private PaymentCardDto card;
    private PaymentCardCancelDto cancels;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PaymentCardDto {
        private String company;
        private String number;
        private String installmentPlanMonth;
        private String isInterestFree;
        private String approveNo;
        private String useCardPoint;
        private String cardType;
        private String ownerType;
        private String acquireStatus;
        private String receiptUrl;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class PaymentCardCancelDto {
        String money;
    }
}
