package com.example.momobe.meeting.application;

import com.example.momobe.meeting.dao.MeetingDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingAutoCloseService {
    private final MeetingDao meetingDao;

    @Transactional
    public void process() {
        List<Long> meetingIds = meetingDao.findExpiredOrFullCapacityMeetings();

        if (!meetingIds.isEmpty()) {
            meetingDao.updateMeetingStateToClose(meetingIds);
        }
    }
}
