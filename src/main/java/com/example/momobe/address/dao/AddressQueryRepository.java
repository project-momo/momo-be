package com.example.momobe.address.dao;

import com.example.momobe.address.dto.AddressResponseDto;
import com.example.momobe.address.dto.QAddressResponseDto;
import com.example.momobe.address.dto.QAddressResponseDto_AddressDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.momobe.address.domain.QAddress.address;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<AddressResponseDto> findAll() {
        return queryFactory.from(address)
                .orderBy(address.id.asc())
                .transform(groupBy(address.si)
                        .list(new QAddressResponseDto(
                                address.si,
                                list(new QAddressResponseDto_AddressDto(
                                        address.id, address.gu))
                        )));
    }
}
