package com.example.momobe.meeting.scheduler;

import com.example.momobe.meeting.application.MeetingAutoCloseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingScheduler {
    private final MeetingAutoCloseService meetingAutoCloseService;

    @Scheduled(cron = "0 0 * * * *")
    public void run() {
        meetingAutoCloseService.process();
    }
}
