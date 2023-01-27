package com.example.momobe.reservation.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.reservation.domain.ReservationException;
import com.example.momobe.reservation.dto.in.PostReservationDto;
import com.example.momobe.reservation.dto.out.PaymentResponseDto;
import com.example.momobe.user.infrastructure.UnableToProcessException;
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

        try {
            boolean hasLock = lock.tryLock(3, 2, TimeUnit.SECONDS);

            if (!hasLock) {
                throw new ReservationException(ErrorCode.UNABLE_TO_PROCESS);
            }

            return reservationBookService.reserve(meetingId, reservationDto, userInfo);
        } catch (InterruptedException e) {
            throw new ReservationException(ErrorCode.UNABLE_TO_PROCESS);
        } finally {
            lock.unlock();
        }
    }
}
