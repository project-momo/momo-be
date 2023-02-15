package com.example.momobe.reservation.dao;

import com.example.momobe.reservation.dao.vo.PaymentIdentification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PaymentDao {
    @Select("select p.payment_key " +
            "from payment as p " +
            "where p.reservation_id = #{reservationId}")
    String findPaymentKeyByReservationId(Long reservationId);

    @Select("select p.payment_key " +
            "from payment as p " +
            "where p.reservation_id in (#{reservationIds})")
    List<String> findPaymentKeyByReservationIds(List<Long> reservationIds);

    @Select("select p.payment_id, p.payment_key " +
            "from payment as p " +
            "where p.reservation_id in (#{reservationIds})")
    List<PaymentIdentification> findPaymentIdentificationsByReservationIds(List<Long> reservationIds);
}
