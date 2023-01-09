package com.example.momobe.payment.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findPaymentByOrderId(String orderId);
    Optional<Payment> findPaymentByReservationId(Long reservationId);
}
