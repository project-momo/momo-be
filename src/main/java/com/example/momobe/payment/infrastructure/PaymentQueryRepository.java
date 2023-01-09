package com.example.momobe.payment.infrastructure;

import com.example.momobe.meeting.dao.MeetingQueryFactoryUtil;
import com.example.momobe.payment.domain.Payment;
import com.example.momobe.payment.domain.enums.PayState;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.momobe.payment.domain.QPayment.payment;

@Repository
@RequiredArgsConstructor
public class PaymentQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Payment findPaymentByReservationId(Long reservationId){
        return (Payment) queryFactory.selectFrom(payment)
                .where(payment.reservationId.eq(reservationId).and(payment.payState.eq(PayState.SUCCESS)))
                .fetch();
    }
}
