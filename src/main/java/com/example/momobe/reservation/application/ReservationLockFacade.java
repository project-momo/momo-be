package com.example.momobe.reservation.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.reservation.domain.ReservationException;
import com.example.momobe.reservation.dto.in.PostReservationDto;
import com.example.momobe.reservation.dto.out.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationLockFacade {
    private final RedissonClient redissonClient;
    private final ReservationBookService reservationBookService;

    public PaymentResponseDto reserve(Long meetingId, PostReservationDto reservationDto, UserInfo userInfo) {
        RLock lock = redissonClient.getLock(meetingId.toString());
        PaymentResponseDto response = null;

        try {
            boolean hasLock = lock.tryLock(5, 3, TimeUnit.SECONDS);

            if (hasLock) {
                try {
                    response = reservationBookService.reserve(meetingId, reservationDto, userInfo);
                } finally {
                    lock.unlock();
                }
            } else {
                reserve(meetingId, reservationDto, userInfo);
            }
        } catch (InterruptedException e) {
            throw new ReservationException(ErrorCode.UNABLE_TO_PROCESS);
        }

        return response;
    }
}