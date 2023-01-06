package com.example.momobe.address.application;

import com.example.momobe.address.domain.AddressNotFoundException;
import com.example.momobe.address.domain.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.momobe.common.exception.enums.ErrorCode.DATA_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressCommonService {
    private final AddressRepository addressRepository;

    public void verifyAddressIdsOrThrowException(List<Long> addressIds) {
        if (!addressRepository.existsAllByIdIn(addressIds)) {
            throw new AddressNotFoundException(DATA_NOT_FOUND);
        }
    }
}
