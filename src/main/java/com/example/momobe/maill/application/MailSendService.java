package com.example.momobe.maill.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.maill.domain.MailSender;
import com.example.momobe.maill.dto.MailDto;
import com.example.momobe.maill.enums.MailType;
import com.example.momobe.user.infrastructure.UnableToProcessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailSendService {
    private final MailSender mailSender;

    private final String ACCEPT_TITLE = "요청하신 예약이 수락되었습니다";
    private final String ACCEPT_CONTENT = "요청하신 예약이 수락되었습니다!" +
            "마이페이지에서 예약 내역을 확인하세요." +
            "(프론트 페이지 완성 후 링크)";

    private final String DENY_TITLE = "요청하신 예약이 거부되었습니다";
    private final String DENY_CONTENT = "요청하신 예약이 거부되었습니다." +
        "결제 금액 반환은 영업일 제외 7일 이내에 처리됩니다.";

    public void sendTo(String address, MailType mailType) {
        try {
            MailDto mail = createMail(address, mailType);
            mailSender.send(mail);
        } catch (UnableToProcessException | MailParseException | MailAuthenticationException | MailSendException e) {
            log.error("", e);
        }
    }

    private MailDto createMail(String address, MailType mailType) {
        if (mailType.equals(MailType.ACCEPT)) {
            return new MailDto(address, ACCEPT_TITLE, ACCEPT_CONTENT);
        }

        if (mailType.equals(MailType.DENY)) {
            return new MailDto(address, DENY_TITLE, DENY_CONTENT);
        }

        throw new UnableToProcessException(ErrorCode.UNABLE_TO_PROCESS);
    }
}
