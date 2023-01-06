package com.example.momobe.address.ui;

import com.example.momobe.address.dao.AddressQueryRepository;
import com.example.momobe.address.dto.AddressResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressQueryController {
    private final AddressQueryRepository addressQueryRepository;

    @GetMapping("/addresses")
    public List<AddressResponseDto> getAddresses() {
        return addressQueryRepository.findAll();
    }
}
