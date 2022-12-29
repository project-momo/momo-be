package com.example.momobe.reservation.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, CustomReservationRepository {

}
