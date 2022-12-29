package com.example.momobe.meeting.dao;

import com.example.momobe.meeting.dto.MeetingParticipantResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingParticipantQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<MeetingParticipantResponseDto> findAll(Long participantId, Pageable pageable) {
        return null;
    }

}
