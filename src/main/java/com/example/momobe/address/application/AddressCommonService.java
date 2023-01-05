package com.example.momobe.address.application;

import com.example.momobe.address.domain.Address;
import com.example.momobe.address.domain.AddressNotFoundException;
import com.example.momobe.address.domain.AddressRepository;
import com.example.momobe.common.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressCommonService {
    private final AddressRepository addressRepository;

    public Address findAddressOrThrowException(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException(ErrorCode.DATA_NOT_FOUND));
    }
}
