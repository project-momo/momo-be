package com.example.momobe.address.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Boolean existsAllByIdIn(List<Long> ids);
}
