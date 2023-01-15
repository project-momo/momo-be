package com.example.momobe.reservation.application;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.application.MeetingCommonService;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.reservation.domain.CanNotChangeReservationStateException;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.reservation.dto.in.PatchReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.momobe.common.exception.enums.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationConfirmService {
    private final MeetingCommonService meetingCommonService;
    private final ReservationCommonService reservationCommonService;

    public void confirm(Long meetingId, Long reservationId, UserInfo userInfo, PatchReservationDto request) {
        Meeting meeting = meetingCommonService.getMeetingOrThrowException(meetingId);
        if (!meeting.matchHostId(userInfo.getId())) throw new CanNotChangeReservationStateException(REQUEST_DENIED);
        Reservation reservation = reservationCommonService.getReservationOrThrowException(reservationId);

        if (!Boolean.parseBoolean(request.getIsAccepted())) {
            reservation.deny();
        } else {
            reservation.accept();
        }
    }
}
