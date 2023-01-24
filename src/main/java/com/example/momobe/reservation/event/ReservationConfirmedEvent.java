package com.example.momobe.reservation.event;

import com.example.momobe.maill.enums.MailType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReservationConfirmedEvent {
    private final String address;
    private final MailType mailType;
}
