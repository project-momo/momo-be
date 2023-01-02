package com.example.momobe.reservation.dto.out;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.domain.Meeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Builder
@ToString
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

    public static PaymentResponseDto freeOrder(Meeting meeting, UserInfo userInfo) {
        return PaymentResponseDto.builder()
                .orderName(meeting.getTitle())
                .amount(0L)
                .createDate(LocalDate.now().toString())
                .customerEmail(userInfo.getEmail())
                .customerName(userInfo.getNickname())
                .build();
    }
}
