package com.example.momobe.settlement.dto.in;

import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiAccountRealNameDto {
    private String bank_tran_id;
    private String bank_code_std;
    private String rsp_code;
    private String account_num;
    private String account_holder_info_type;
    private String account_holder_info;
    private String tran_dtime;

}


