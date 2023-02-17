package com.example.momobe.reservation.application;

import com.example.momobe.maill.enums.MailType;
import com.example.momobe.reservation.dao.PaymentDao;
import com.example.momobe.reservation.dao.PointHistoryDao;
import com.example.momobe.reservation.dao.ReservationDao;
import com.example.momobe.reservation.dao.vo.PaymentIdentification;
import com.example.momobe.reservation.dao.UserDao;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.settlement.domain.enums.PointState;
import com.example.momobe.settlement.domain.enums.PointUsedType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.momobe.maill.enums.MailType.*;

@Service
@RequiredArgsConstructor
public class ReservationAutoCancelService {
    private final ReservationDao reservationDao;
    private final PaymentDao paymentDao;
    private final UserDao userDao;
    private final PointHistoryDao pointHistoryDao;
    private final ReservationEventPublishService reservationEventPublishService;
    private final MailEventPublishService mailEventPublishService;

    @Transactional
    public void process() {
        List<Reservation> reservations = reservationDao.findOverdueUnapprovedReservations();

        List<Reservation> freeReservations = getFreeReservations(reservations);
        denyFreeReservations(freeReservations);

        List<Reservation> paidReservations = getPaidReservations(reservations);

        if (!paidReservations.isEmpty()) {
            cancelPaymentAndRefund(paidReservations);
            notifyUser(paidReservations);
        }
    }

    private void notifyUser(List<Reservation> paidReservations) {
        List<Long> paidUserIds = getUserIds(paidReservations);
        List<String> userMails = getUserMails(paidUserIds);
        publishUserMailEvents(userMails);
    }

    private void cancelPaymentAndRefund(List<Reservation> paidReservations) {
        List<PaymentIdentification> paymentIdentifications = getPaymentIdentifications(paidReservations);
        publishPaymentCancelEvent(paymentIdentifications);
        paidReservations.forEach(this::refund);
    }

    private void publishUserMailEvents(List<String> userMails) {
        userMails.forEach(address -> mailEventPublishService.publish(address, DENY));
    }

    private List<String> getUserMails(List<Long> paidUserIds) {
        return userDao.findUserMailByReservationUserId(paidUserIds);
    }

    private List<Long> getUserIds(List<Reservation> paidReservations) {
        return paidReservations
                .stream()
                .map(Reservation::getReservedUserId)
                .collect(Collectors.toList());
    }

    private void denyFreeReservations(List<Reservation> freeReservations) {
        freeReservations.forEach(Reservation::deny);
    }

    private void publishPaymentCancelEvent(List<PaymentIdentification> paymentIdentifications) {
        paymentIdentifications
                .forEach(e -> reservationEventPublishService.publishCancelEvent(e.getPaymentKey(), e.getPaymentId(), "DENY"));
    }

    private List<PaymentIdentification> getPaymentIdentifications(List<Reservation> paidReservations) {
        List<Long> paidReservationsIds = getPaidReservationsIds(paidReservations);
        return paymentDao.findPaymentIdentificationsByReservationIds(paidReservationsIds);
    }

    private List<Long> getPaidReservationsIds(List<Reservation> paidReservations) {
        return paidReservations.stream()
                .map(Reservation::getId)
                .collect(Collectors.toList());
    }

    private List<Reservation> getFreeReservations(List<Reservation> reservations) {
        return reservations.stream()
                .filter(Reservation::isFreeOrder)
                .collect(Collectors.toList());
    }

    private List<Reservation> getPaidReservations(List<Reservation> reservations) {
        return reservations.stream()
                .filter(e -> !e.isFreeOrder())
                .collect(Collectors.toList());
    }

    private void refund(Reservation reservation) {
        Long prevPoint = userDao.selectUserPointById(reservation.getReservedUserId());
        userDao.updateUserPoint(reservation.getReservedUserId(), reservation.getWon());
        pointHistoryDao.insertPointHistory(prevPoint, LocalDate.now(), reservation.getWon(), PointState.SAVE, PointUsedType.REFUND, reservation.getReservedUserId());
        reservation.deny();
    }
}
