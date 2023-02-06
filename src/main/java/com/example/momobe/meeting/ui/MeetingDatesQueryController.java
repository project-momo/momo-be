package com.example.momobe.meeting.ui;

import com.example.momobe.common.exception.CanNotConvertException;
import com.example.momobe.meeting.dao.MonthlyMeetingScheduleInquiry;
import com.example.momobe.meeting.domain.MeetingException;
import com.example.momobe.meeting.dto.out.ResponseMeetingDatesDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.momobe.common.exception.enums.ErrorCode.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/meetings")
public class MeetingDatesQueryController {
    private final MonthlyMeetingScheduleInquiry monthlyMeetingScheduleInquiry;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{meetingId}/reservations/dates/{date}")
    public List<ResponseMeetingDatesDto> getDates(@PathVariable(name = "meetingId") Long meetingId,
                                                  @Pattern(regexp = "yyyy-MM-dd") @PathVariable(name = "date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        validateDate(localDate);

        return monthlyMeetingScheduleInquiry.getSchedules(meetingId, localDate.getMonthValue());
    }

    private void validateDate(LocalDate localDate) {
        if (localDate.isBefore(LocalDate.now())) throw new MeetingException(EXCEEDED_EXPIRATION_DATE);
    }
}