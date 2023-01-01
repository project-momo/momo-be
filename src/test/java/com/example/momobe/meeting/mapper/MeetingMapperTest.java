package com.example.momobe.meeting.mapper;

import com.example.momobe.common.exception.ui.ExceptionController;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.enums.MeetingState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.time.temporal.ChronoUnit;

import static com.example.momobe.common.enums.TestConstants.ID1;
import static com.example.momobe.meeting.enums.MeetingConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest({ExceptionController.class, MeetingMapper.class, DateTimeMapper.class})
@MockBean(JpaMetamodelMappingContext.class)
public class MeetingMapperTest {

    @Autowired
    private MeetingMapper meetingMapper;
    @MockBean
    private JwtArgumentResolver jwtArgumentResolver;

    @Test
    public void meetingMapper() throws Exception {
        // given / when
        Meeting meeting = meetingMapper.toMeeting(MEETING_REQUEST_DTO_WITH_FREE, ID1);

        // then
        assertThat(meeting.getHostId()).isEqualTo(ID1);
        assertThat(meeting.getMeetingState()).isEqualTo(MeetingState.OPEN);
        assertThat(meeting.getCategory()).isEqualTo(MEETING_REQUEST_DTO_WITH_FREE.getCategory());
        assertThat(meeting.getTitle()).isEqualTo(MEETING_REQUEST_DTO_WITH_FREE.getTitle());
        assertThat(meeting.getContent()).isEqualTo(MEETING_REQUEST_DTO_WITH_FREE.getContent());

        assertThat(meeting.getTags().size()).isEqualTo(MEETING_REQUEST_DTO_WITH_FREE.getTags().size());
        assertThat(meeting.getAddress().getAddressIds())
                .isEqualTo(MEETING_REQUEST_DTO_WITH_FREE.getAddress().getAddressIds());
        assertThat(meeting.getAddress().getAddressInfo())
                .isEqualTo(MEETING_REQUEST_DTO_WITH_FREE.getAddress().getAddressInfo());

        assertThat(meeting.getPrice()).isEqualTo(MEETING_REQUEST_DTO_WITH_FREE.getPrice());

    }

    @Test
    public void meetingMapperWithOneDay() throws Exception {
        // given / when
        Meeting meeting = meetingMapper.toMeeting(MEETING_REQUEST_DTO_WITH_ONE_DAY, ID1);

        // then
        assertThat(meeting.getDateTimeInfo().getDateTimes().size())
                .isEqualTo(1);
    }

    @Test
    public void meetingMapperWithPeriod() throws Exception {
        // given / when
        Meeting meeting = meetingMapper.toMeeting(MEETING_REQUEST_DTO_WITH_PERIOD, ID1);

        // then
        assertThat(ChronoUnit.DAYS.between(meeting.getDateTimeInfo().getStartDate(), meeting.getDateTimeInfo().getEndDate()) + 1)
                .isEqualTo(7L);
        assertThat(meeting.getDateTimeInfo().getDateTimes().size())
                .isEqualTo(MEETING_REQUEST_DTO_WITH_PERIOD.getDateTime().getDayWeeks().size());
    }

    @Test
    public void meetingMapperWithFree() throws Exception {
        // given / when
        Meeting meeting = meetingMapper.toMeeting(MEETING_REQUEST_DTO_WITH_FREE, ID1);

        // then
        assertThat(meeting.getDateTimeInfo().getDateTimes().size())
                .isEqualTo(MEETING_REQUEST_DTO_WITH_FREE.getDateTime().getDates().size()
                        * (MEETING_REQUEST_DTO_WITH_FREE.getDateTime().getEndTime().getHour()
                        - MEETING_REQUEST_DTO_WITH_FREE.getDateTime().getStartTime().getHour() + 1));
    }

}
