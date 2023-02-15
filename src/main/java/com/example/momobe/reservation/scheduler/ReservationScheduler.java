package com.example.momobe.reservation.scheduler;

import com.example.momobe.reservation.application.ReservationAutoCancelService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationScheduler {
    private final ReservationAutoCancelService reservationAutoCancelService;

    @Scheduled(cron = "0 0 3 * * ?")
    public void autoCancelReservation() {
        reservationAutoCancelService.process();
    }
}
