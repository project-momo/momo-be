package com.example.momobe.payment.ui;

import com.example.momobe.payment.application.PaymentProgressService;
import com.example.momobe.payment.dto.PaymentResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentSuccessController {
    private final PaymentProgressService paymentProgressService;

    @GetMapping("/payments/success")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResultDto getSuccess(@RequestParam(name = "paymentKey") String paymentKey,
                                       @RequestParam(name = "orderId") String orderId,
                                       @RequestParam(name = "amount") Long amount) {
        return paymentProgressService.progress(orderId, paymentKey, amount);
    }
}
