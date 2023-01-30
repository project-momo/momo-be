package com.example.momobe.payment.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PaymentReadDao {
    @Select("select r.reservation_id as reservationId\n" +
            "    from reservation as r\n" +
            "    join payment as p on p.reservation_id = r.reservation_id AND p.order_id = #{orderId}")
    Long getReservationIdByOrderId(String orderId);
}

