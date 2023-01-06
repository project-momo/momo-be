package com.example.momobe.address.application;

import com.example.momobe.address.domain.AddressNotFoundException;
import com.example.momobe.address.domain.AddressRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.momobe.common.enums.TestConstants.ID1;
import static com.example.momobe.common.enums.TestConstants.ID2;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AddressCommonServiceTest {
    @InjectMocks
    private AddressCommonService addressCommonService;
    @Mock
    private AddressRepository addressRepository;

    @Test
    @DisplayName("AddressRepository가 true를 반환하면 예외가 발생하지 않는다.")
    void verifyAddressIdsOrThrowException() {
        // given
        List<Long> existsTagIds = List.of(ID1, ID2);
        given(addressRepository.existsAllByIdIn(existsTagIds))
                .willReturn(true);

        // when / then
        assertDoesNotThrow(() -> addressCommonService.verifyAddressIdsOrThrowException(existsTagIds));
    }

    @Test
    @DisplayName("AddressRepository가 false를 반환하면 AddressNotFoundException이 발생한다.")
    void verifyAddressIdsOrThrowExceptionFail() {
        // given
        List<Long> nonExistsTagIds = List.of(ID1, ID2);
        given(addressRepository.existsAllByIdIn(nonExistsTagIds))
                .willReturn(false);

        // when / then
        assertThrows(AddressNotFoundException.class,
                () -> addressCommonService.verifyAddressIdsOrThrowException(nonExistsTagIds));
    }
}