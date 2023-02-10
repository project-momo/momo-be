package com.example.momobe.reservation.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PaymentDao {
    @Select("select p.payment_key " +
            "from payment as p " +
            "where p.reservation_id = #{reservationId}")
    String findPaymentKeyByReservationId(Long reservationId);
}
