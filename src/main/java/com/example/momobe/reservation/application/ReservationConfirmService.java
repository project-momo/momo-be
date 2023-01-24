package com.example.momobe.reservation.application;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.maill.enums.MailType;
import com.example.momobe.meeting.application.MeetingCommonService;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.reservation.dao.UserMailQueryRepository;
import com.example.momobe.reservation.domain.CanNotChangeReservationStateException;
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
public class ReservationConfirmService implements ApplicationEventPublisherAware {
    private final MeetingCommonService meetingCommonService;
    private final ReservationCommonService reservationCommonService;
    private final UserMailQueryRepository userMailQueryRepository;
    private ApplicationEventPublisher applicationEventPublisher;

    public void confirm(Long meetingId, Long reservationId, UserInfo userInfo, PatchReservationDto request) {
        Reservation reservation = validateRequest(meetingId, reservationId, userInfo);
        String userMail = userMailQueryRepository.findMailOf(reservation.getReservedUserId());

        if (isDenied(request)) {
            reservation.deny();
            publishReservationConfirmedEvent(userMail, MailType.DENY);
        } else {
            reservation.accept();
            publishReservationConfirmedEvent(userMail, MailType.ACCEPT);
        }
    }

    private Boolean isDenied(PatchReservationDto reservationDto) {
        return !Boolean.parseBoolean(reservationDto.getIsAccepted());
    }

    private Reservation validateRequest(Long meetingId, Long reservationId, UserInfo userInfo) {
        Meeting meeting = meetingCommonService.getMeeting(meetingId);
        if (!meeting.matchHostId(userInfo.getId())) throw new CanNotChangeReservationStateException(REQUEST_DENIED);
        return reservationCommonService.getReservation(reservationId);
    }

    private void publishReservationConfirmedEvent(String userMail, MailType mailType) {
        applicationEventPublisher.publishEvent(new ReservationConfirmedEvent(userMail, mailType));
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
