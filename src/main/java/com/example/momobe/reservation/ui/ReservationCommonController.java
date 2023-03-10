package com.example.momobe.reservation.ui;

import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.reservation.application.ReservationLockFacade;
import com.example.momobe.reservation.application.ReservationCancelService;
import com.example.momobe.reservation.application.ReservationConfirmService;
import com.example.momobe.reservation.application.ReservationBookService;
import com.example.momobe.reservation.dto.in.DeleteReservationDto;
import com.example.momobe.reservation.dto.in.PatchReservationDto;
import com.example.momobe.reservation.dto.in.PostReservationDto;
import com.example.momobe.reservation.dto.out.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
public class ReservationCommonController {
    private final ReservationLockFacade reservationLockFacade;
    private final ReservationConfirmService reservationConfirmService;
    private final ReservationCancelService reservationCancelService;

    @ResponseStatus(CREATED)
    @PostMapping("/{meetingId}/reservations")
    public PaymentResponseDto postReservation(@PathVariable(name = "meetingId") Long meetingId,
                                              @Valid @RequestBody PostReservationDto request,
                                              @Token UserInfo userInfo) {
        return reservationLockFacade.reserve(meetingId, request, userInfo);
    }

    @ResponseStatus(OK)
    @PatchMapping("/{meetingId}/reservations/{reservationId}")
    public void patchReservation(@PathVariable Long meetingId,
                                 @PathVariable Long reservationId,
                                 @Valid @RequestBody PatchReservationDto request,
                                 @Token UserInfo userInfo) {
        reservationConfirmService.confirm(meetingId, reservationId, userInfo, request);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{meetingId}/reservations/{reservationId}")
    public void deleteReservation(@PathVariable Long meetingId,
                                  @PathVariable Long reservationId,
                                  @Token UserInfo userInfo,
                                  @Valid @RequestBody DeleteReservationDto request) {
        reservationCancelService.cancelReservation(reservationId, request, userInfo);
    }
}
