package com.example.momobe.settlement.infrastructure;

import com.example.momobe.payment.domain.Payment;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.settlement.domain.Settlement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@Repository
//@Transactional(readOnly = true)
public interface SettlementQueryRepository extends JpaRepository<Settlement,Long> {

    @Query(nativeQuery = true, value =
            "SELECT * " +
                    "FROM (SELECT m.meeting_id FROM meeting AS m WHERE m.end_date = CURRENT_DATE()-3) AS m2" +
                    "JOIN reservation AS r ON r.meeting_id = m2.meeting_id " +
                    "WHERE r.reservation_state = \"PAYMENT_SUCCESS\""
    )
    List<Reservation> findReservationForMeetingClosedBefore3days();

    @Query(nativeQuery = true, value = "SELECT * " + "FROM payment " + "WHERE reservation_id = :id")
    Payment findPaymentOfReservation(@Param("id") Long id);

    @Query(nativeQuery = true, value = "SELECT host_id " + "FROM meeting " + "WHERE meeting_id = :id")
    Long findHostOfReservation(@Param("id") Long id);

}


/**
 * 1. 끝난 미팅 id 쿼리
 * 2. 해당 미팅 id를 가지고 있으면서 paymentState가 PaymentCompleted인 예약 쿼리
 * 3. 2번에 해당하는 id를 가진 Payment 쿼리 및 state update
 * 4. 1번 id의 hostId들
 * 5. 4번을 id에 해당하는 유저들 포인트 update 쿼리
 * 6. 4번 id, 5번 금액, 1번 미팅,  2번 예약 정보를 담은 settlement 저장
 *
 * SELECT m2.meeting_id, r.reservation_id, p.payment_id, u.user_id, p.amount
 * FROM (SELECT m.meeting_id
 *       FROM Meeting AS m
 *       WHERE m.endDate = ( :nowDate - 3 )) AS m2
 * JOIN Reservation AS r ON r.meeting_id = m2.meeting_id
 * JOIN Payment AS p ON p.reservation_id = r.reservation_id
 * JOIN User AS u ON u.user_id = m2.host_id
 * */