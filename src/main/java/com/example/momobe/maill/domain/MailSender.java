package com.example.momobe.maill.domain;

import com.example.momobe.maill.dto.MailDto;

public interface MailSender {
    void send(String email, String title, String content);
    void send(MailDto mailDto);
}
