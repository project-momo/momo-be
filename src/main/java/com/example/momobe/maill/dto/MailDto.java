package com.example.momobe.maill.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class MailDto {
    private final String address;
    private final String title;
    private final String content;
}
