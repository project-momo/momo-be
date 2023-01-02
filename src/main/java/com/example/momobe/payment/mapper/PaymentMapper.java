package com.example.momobe.payment.mapper;

import com.example.momobe.payment.domain.Payment;
import com.example.momobe.reservation.dto.out.PaymentResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    default PaymentResponseDto of(Payment payment) {
        return PaymentResponseDto.builder()
                .amount(payment.getAmount())
                .customerEmail(payment.getCustomerEmail())
                .customerName(payment.getCustomerName())
                .orderId(payment.getOrderId())
                .payType(payment.getPayType().getValue())
                .createDate(payment.getCreateDate())
                .successUrl(payment.getSuccessUrl())
                .failUrl(payment.getFailUrl())
                .paySuccessYn(payment.getPayState().getValue())
                .orderName(payment.getOrderName())
                .build();
    }
}
