package com.example.momobe.reservation.ui;

import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.reservation.application.ReserveService;
import com.example.momobe.reservation.dto.in.RequestReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
public class ReservationController {
    private final ReserveService reserveService;

    @PostMapping("/{meetingId}/reservations")
    public void postReservation(@PathVariable(name = "meetingId") Long meetingId,
                                @RequestBody RequestReservationDto request,
                                @Token UserInfo userInfo) {
        reserveService.reserve(meetingId, request, userInfo);
    }
}
