package com.example.momobe.meeting.application;

import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.reservation.domain.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.momobe.reservation.domain.enums.ReservationState.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingCloseService {
    private final MeetingCommonService meetingCommonService;
    private final ReservationRepository reservationRepository;

    public void closeMeeting(Long userId, Long meetingId) {
        meetingCommonService.getMeetingOrThrowException(meetingId)
                .closeWithHostId(userId);
        List<Reservation> reservations =
                reservationRepository.findByMeetingIdAndReservationStateIn(
                        meetingId, List.of(PAYMENT_BEFORE, PAYMENT_PROGRESS, PAYMENT_SUCCESS));
        reservations.forEach(Reservation::cancel);
    }
}
