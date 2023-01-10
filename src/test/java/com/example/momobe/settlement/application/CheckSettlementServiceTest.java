package com.example.momobe.settlement.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.meeting.dao.MeetingQueryRepository;
import com.example.momobe.settlement.domain.NotFoundEndMeetingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckSettlementServiceTest {
    @InjectMocks
    private CheckSettlementService checkSettlementService;
    @Mock
    private MeetingQueryRepository meetingQueryRepository;

    List<Long> meetingId;
    List<Long> meetingId2;

    @BeforeEach
    void init(){
        meetingId = List.of(1L, 30L, 40L, 22L);
        meetingId2 = new ArrayList<>();
    }

    @Test
    @DisplayName("오늘 날짜 기준 정산할 모임 건 있으면 모임 목록 반환")
    void test01(){
        //given
        given(meetingQueryRepository.findMeetingClosedBefore3days()).willReturn(meetingId);

        //when
        List<Long> results = checkSettlementService.checkEndMeetingExist();

        //then
        assertThat(meetingId.size()).isEqualTo(4);

        //verify
        verify(meetingQueryRepository,times(1)).findMeetingClosedBefore3days();

    }

    //아니면 모임 목록 출력

    @Test
    @DisplayName("오늘 날짜 기준 정산할 모임 건 없으면 예외 로그 출력")
    void test02() {
        //given
        given(meetingQueryRepository.findMeetingClosedBefore3days()).willReturn(meetingId2);

        //when,then
        assertThrows(NotFoundEndMeetingException.class, () -> {
            checkSettlementService.checkEndMeetingExist();
        });

        //then
        assertThat(meetingId2.size()).isZero();
//        assertTrue(exception.getMessage().contains("금일"));
        }
    }

