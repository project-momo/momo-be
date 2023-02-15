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
public class PointHistoryResponseDto {
    private Long amount;
    private String state;
    private String type;
    private String description;
    private LocalDate date;

    public static Page<PointHistoryResponseDto> of(Page<PointHistory> pointHistory){
        Page<PointHistoryResponseDto> pageDto =
        pointHistory.map( x ->
                PointHistoryResponseDto.builder()
                        .amount(x.getRequestPoint())
                        .date(x.getHistoryDate())
                        .state(x.getState().toString())
                        .type(x.getType().toString())
                        .description(x.getType().getMessage())
                        .build());
        return pageDto;
    }
}
