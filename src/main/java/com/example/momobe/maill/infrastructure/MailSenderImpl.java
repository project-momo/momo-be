package com.example.momobe.maill.infrastructure;

import com.example.momobe.maill.domain.MailSender;
import com.example.momobe.maill.dto.MailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {
    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;

    @Override
    public void send(String address, String title, String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(mailProperties.getUsername());
        simpleMailMessage.setTo(address);
        simpleMailMessage.setSubject(title);
        simpleMailMessage.setText(content);
        javaMailSender.send(simpleMailMessage);
    }

    @Override
    public void send(MailDto mailDto) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(mailProperties.getUsername());
        simpleMailMessage.setTo(mailDto.getAddress());
        simpleMailMessage.setSubject(mailDto.getTitle());
        simpleMailMessage.setText(mailDto.getContent());
        javaMailSender.send(simpleMailMessage);
    }
}
