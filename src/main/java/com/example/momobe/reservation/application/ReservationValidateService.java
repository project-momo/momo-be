package com.example.momobe.reservation.application;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.reservation.domain.GetReservationsAtSameTimeService;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.reservation.domain.ReservationException;
import com.example.momobe.reservation.domain.ReservationRepository;
import com.example.momobe.reservation.dto.in.PostReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.momobe.common.exception.enums.ErrorCode.*;
import static com.example.momobe.common.exception.enums.ErrorCode.AMOUNT_DOSE_NOT_MATCH;

@Service
@RequiredArgsConstructor
public class ReservationValidateService {
    private final ReservationRepository reservationRepository;
    private final GetReservationsAtSameTimeService getReservationsAtSameTimeService;

    public void validate(PostReservationDto reservationDto, Meeting meeting, UserInfo userInfo) {
        LocalDate reservationDate = reservationDto.getDateInfo().getReservationDate();
        LocalTime startTime = reservationDto.getDateInfo().getStartTime();
        LocalTime endTime = reservationDto.getDateInfo().getEndTime();

        List<Reservation> reservationsAtSameTime = getReservationsAtSameTime(meeting.getId(), reservationDate, startTime, endTime);

        if (isDuplicatedBookingAtSameUser(userInfo, reservationsAtSameTime)) {
            throw new ReservationException(ALREADY_EXIST_RESERVATION);
        }

        if (meeting.matchHostId(userInfo.getId())) {
            throw new ReservationException(CAN_NOT_PARTICIPATE_OWN_MEETING);
        }

        if (meeting.isClosed()) {
            throw new ReservationException(CLOSED_MEETING);
        }

        if (!meeting.hasRemainingReservations(getAcceptedReservations(reservationsAtSameTime))) {
            throw new ReservationException(FULL_OF_PEOPLE);
        }

        if (!meeting.isValidReservationSchedule(reservationDate, startTime, endTime)) {
            throw new ReservationException(INVALID_RESERVATION_TIME);
        }

        if (!meeting.matchPrice(reservationDto.getAmount(), startTime, endTime)) {
            throw new ReservationException(AMOUNT_DOSE_NOT_MATCH);
        }
    }

    private Long getAcceptedReservations(List<Reservation> reservationsAtSameTime) {
        return reservationsAtSameTime
                .stream()
                .filter(Reservation::isAccepted)
                .count();
    }

    private List<Reservation> getReservationsAtSameTime(Long meetingId, LocalDate reservationDate, LocalTime startTime, LocalTime endTime) {
        return getReservationsAtSameTimeService.getReservations(meetingId, reservationDate, startTime, endTime);
    }

    private Boolean isDuplicatedBookingAtSameUser(UserInfo userInfo, List<Reservation> reservationsAtSameTime) {
        AtomicReference<Boolean> isDuplicated = new AtomicReference<>(Boolean.FALSE);

        reservationsAtSameTime
                .forEach(e -> {
                    if (e.matchReservedUserId(userInfo.getId()) && isPaymentOrAcceptanceConfirmed(e)) {
                        isDuplicated.set(Boolean.TRUE);
                    }

                    if (e.matchReservedUserId(userInfo.getId()) && !isPaymentOrAcceptanceConfirmed(e)) {
                        reservationRepository.delete(e);
                    }
                });

        return isDuplicated.get();
    }

    private boolean isPaymentOrAcceptanceConfirmed(Reservation reservation) {
        return reservation.isPaymentSucceed() || reservation.isAccepted();
    }
}
