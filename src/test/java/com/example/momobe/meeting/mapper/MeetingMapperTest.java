package com.example.momobe.meeting.mapper;

import com.example.momobe.common.exception.ui.ExceptionController;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.enums.MeetingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import static com.example.momobe.common.enums.TestConstants.ID1;
import static com.example.momobe.meeting.enums.MeetingConstant.MEETING_REQUEST_DTO;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest({ExceptionController.class, MeetingMapper.class, LocationMapper.class, DateTimeMapper.class, PriceMapper.class})
@MockBean(JpaMetamodelMappingContext.class)
public class MeetingMapperTest {

    @Autowired
    private MeetingMapper meetingMapper;
    @MockBean
    private JwtArgumentResolver jwtArgumentResolver;

    @Test
    public void meetingMapper() throws Exception {
        // given
        // when
        Meeting meeting = meetingMapper.toMeeting(MEETING_REQUEST_DTO, ID1);

        // then
        assertThat(meeting.getHostId()).isEqualTo(ID1);
        assertThat(meeting.getMeetingStatus()).isEqualTo(MeetingStatus.OPEN);
        assertThat(meeting.getCategory()).isEqualTo(MEETING_REQUEST_DTO.getCategory());
        assertThat(meeting.getTitle()).isEqualTo(MEETING_REQUEST_DTO.getTitle());
        assertThat(meeting.getContent()).isEqualTo(MEETING_REQUEST_DTO.getContent());
        assertThat(meeting.getNotice()).isEqualTo(MEETING_REQUEST_DTO.getNotice());

        assertThat(meeting.getTags().size()).isEqualTo(MEETING_REQUEST_DTO.getTags().size());
        assertThat(meeting.getLocations().size()).isEqualTo(MEETING_REQUEST_DTO.getLocations().size());
        assertThat(meeting.getLocations().get(0).getAddress().getAddress1())
                .isEqualTo(MEETING_REQUEST_DTO.getLocations().get(0).getAddress1());
        assertThat(meeting.getDateTimes().get(0).getDateTime())
                .isEqualTo(MEETING_REQUEST_DTO.getDateTimes().get(0));

        assertThat(meeting.getPriceInfo().getPricePolicy()).isEqualTo(MEETING_REQUEST_DTO.getPriceInfo().getPricePolicy());
        assertThat(meeting.getPriceInfo().getPrice()).isEqualTo(MEETING_REQUEST_DTO.getPriceInfo().getPrice());

    }

}
