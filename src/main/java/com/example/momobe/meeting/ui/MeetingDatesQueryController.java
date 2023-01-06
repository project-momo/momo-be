package com.example.momobe.meeting.ui;

import com.example.momobe.common.exception.CanNotConvertException;
import com.example.momobe.meeting.dao.MonthlyMeetingScheduleInquiry;
import com.example.momobe.meeting.dto.ResponseMeetingDatesDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
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
                                                  @PathVariable(name = "date") String date) {
        LocalDate localDate = convertLocalDate(date);
        return monthlyMeetingScheduleInquiry.getSchedules(meetingId, localDate.getMonthValue());
    }

    private LocalDate convertLocalDate(String date) {
        try {
            return Date.valueOf(date).toLocalDate();
        } catch (IllegalArgumentException e) {
            log.error("date format ex = ()", e);
            throw new CanNotConvertException(CAN_NOT_CONVERT);
        }
    }
}