package com.example.momobe.payment.dao;

import com.example.momobe.reservation.domain.enums.ReservationState;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PaymentUpdateDao {
    @Update("update reservation as r set r.reservation_state = #{reservationState}\n" +
            "    where r.reservation_id = #{reservationId}")
    void setReservationState(ReservationState reservationState, Long reservationId);
}
