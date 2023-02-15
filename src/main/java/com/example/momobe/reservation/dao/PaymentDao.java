package com.example.momobe.reservation.dao;

import com.example.momobe.reservation.dao.vo.PaymentIdentification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PaymentDao {
    @Select("select p.payment_key " +
            "from payment as p " +
            "where p.reservation_id = #{reservationId}")
    String findPaymentKeyByReservationId(Long reservationId);

    @Select({
            "<script>",
            "SELECT p.payment_id, p.payment_key",
            "FROM payment AS p",
            "WHERE p.reservation_id IN",
            "<foreach collection='reservationIds' item='reservationId' index='index' open='(' separator=',' close=')'>",
            "#{reservationId}",
            "</foreach>",
            "</script>"
    })
    List<PaymentIdentification> findPaymentIdentificationsByReservationIds(@Param("reservationIds") List<Long> reservationIds);
}
