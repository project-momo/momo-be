package com.example.momobe.maill.application;

import com.example.momobe.maill.domain.MailSender;
import com.example.momobe.maill.dto.MailDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.maill.enums.MailType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MailSendServiceTest {
    @InjectMocks
    MailSendService mailSendService;

    @Mock
    MailSender mailSender;

    @Test
    @DisplayName("mailType이 deny일 때 mailDto 생성 테스트")
    void createMailTest() {
        //given //when
        MailDto mail = mailSendService.createMail(EMAIL1, DENY);

        //then
        assertThat(mail.getAddress()).isEqualTo(EMAIL1);
        assertThat(mail.getContent()).isEqualTo(MailSendService.DENY_CONTENT);
        assertThat(mail.getTitle()).isEqualTo(MailSendService.DENY_TITLE);
    }

    @Test
    @DisplayName("mailType이 accept일 때 mailDto 생성 테스트")
    void createMailTest2() {
        //given //when
        MailDto mail = mailSendService.createMail(EMAIL1, ACCEPT);

        //then
        assertThat(mail.getAddress()).isEqualTo(EMAIL1);
        assertThat(mail.getContent()).isEqualTo(MailSendService.ACCEPT_CONTENT);
        assertThat(mail.getTitle()).isEqualTo(MailSendService.ACCEPT_TITLE);
    }

    @Test
    @DisplayName("sendTo 실행 시 mailSender.sendTo()메서드가 1회 실행된다")
    void sendTest() {
        //given
        //when
        mailSendService.sendTo(EMAIL1, ACCEPT);

        //then
        BDDMockito.verify(mailSender, Mockito.times(1)).send(any(MailDto.class));
    }
}