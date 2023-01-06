package com.example.momobe.reservation.ui;

import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.reservation.application.ReservationConfirmService;
import com.example.momobe.reservation.application.ReservationSaveService;
import com.example.momobe.reservation.dto.in.PatchReservationDto;
import com.example.momobe.reservation.dto.in.PostReservationDto;
import com.example.momobe.reservation.dto.out.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationSaveService reservationSaveService;
    private final ReservationConfirmService reservationConfirmService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{meetingId}/reservations")
    public PaymentResponseDto postReservation(@PathVariable(name = "meetingId") Long meetingId,
                                              @Valid @RequestBody PostReservationDto request,
                                              @Token UserInfo userInfo) {
        return reservationSaveService.reserve(meetingId, request, userInfo);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{meetingId}/reservations/{reservationId}")
    public void patchReservation(@PathVariable Long meetingId,
                                 @PathVariable Long reservationId,
                                 @Valid @RequestBody PatchReservationDto isAccepted,
                                 @Token UserInfo userInfo) {
        reservationConfirmService.confirm(meetingId, reservationId, userInfo, Boolean.valueOf(isAccepted.getIsAccepted()));
    }
}
