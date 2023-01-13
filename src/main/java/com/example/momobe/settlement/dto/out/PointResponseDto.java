package com.example.momobe.settlement.dto.out;

import com.example.momobe.settlement.domain.PointHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointResponseDto {
    private Long amount;
    private String state;
    private String type;
    private LocalDate date;

    public static Page<PointResponseDto> of(Page<PointHistory> pointHistory){
        Page<PointResponseDto> pageDto =
        pointHistory.map( x ->
                PointResponseDto.builder()
                        .amount(x.getRequestPoint())
                        .date(x.getHistoryDate())
                        .state(x.getState().toString())
                        .type(x.getType().toString())
                        .build());
        return pageDto;
    }
}
