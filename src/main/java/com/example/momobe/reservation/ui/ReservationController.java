package com.example.momobe.reservation.ui;

import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.reservation.application.ReserveService;
import com.example.momobe.reservation.dto.in.RequestReservationDto;
import com.example.momobe.reservation.dto.out.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
public class ReservationController {
    private final ReserveService reserveService;

    @PostMapping("/{meetingId}/reservations")
    public PaymentResponseDto postReservation(@PathVariable(name = "meetingId") Long meetingId,
                                              @Valid @RequestBody RequestReservationDto request,
                                              @Token UserInfo userInfo) {
        return reserveService.reserve(meetingId, request, userInfo);
    }
}
