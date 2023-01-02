package com.example.momobe.reservation.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PaymentResponseDto {
    private String payType;
    private Long amount;
    private String orderId;
    private String orderName;
    private String customerEmail;
    private String customerName;
    private String successUrl;
    private String failUrl;
    private String createDate;
    private String paySuccessYn;

    public static PaymentResponseDto freeOrder() {
        return PaymentResponseDto.builder()
                .amount(0L)
                .build();
    }
}
