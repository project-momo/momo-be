package com.example.momobe.reservation.application;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.maill.enums.MailType;
import com.example.momobe.meeting.application.MeetingCommonService;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.reservation.dao.UserMailDao;
import com.example.momobe.reservation.domain.ReservationException;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.reservation.dto.in.PatchReservationDto;
import com.example.momobe.reservation.event.ReservationConfirmedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.momobe.common.exception.enums.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationConfirmService {
    private final MeetingCommonService meetingCommonService;
    private final ReservationFindService reservationFindService;
    private final UserMailDao userMailDao;
    private final MailEventPublishService mailEventPublishService;

    public void confirm(Long meetingId, Long reservationId, UserInfo userInfo, PatchReservationDto request) {
        Reservation reservation = validateRequest(meetingId, reservationId, userInfo);
        String userMail = userMailDao.findMailOf(reservation.getReservedUserId());

        if (isDenied(request)) {
            reservation.deny();
            mailEventPublishService.publish(userMail, MailType.DENY);
        } else {
            reservation.accept();
            mailEventPublishService.publish(userMail, MailType.ACCEPT);
        }
    }

    private Boolean isDenied(PatchReservationDto reservationDto) {
        return !Boolean.parseBoolean(reservationDto.getIsAccepted());
    }

    private Reservation validateRequest(Long meetingId, Long reservationId, UserInfo userInfo) {
        Meeting meeting = meetingCommonService.getMeeting(meetingId);
        if (!meeting.matchHostId(userInfo.getId())) throw new ReservationException(REQUEST_DENIED);
        return reservationFindService.getReservation(reservationId);
    }
}
