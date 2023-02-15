package com.example.momobe.meeting.ui;

import com.example.momobe.meeting.dao.MeetingDao;
import com.example.momobe.meeting.dto.out.ResponseMeetingDatesDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/meetings")
public class MeetingDatesQueryController {
    private final MeetingDao meetingDao;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{meetingId}/reservations/dates/{date}")
    public List<ResponseMeetingDatesDto> getDates(@PathVariable(name = "meetingId") Long meetingId,
                                                  @Pattern(regexp = "yyyy-MM-dd") @PathVariable(name = "date") String date) {
        LocalDate localDate = LocalDate.parse(date);

        return meetingDao.getMonthlyReservationSchedule(meetingId, localDate.getMonthValue());
    }
}