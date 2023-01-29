package com.example.momobe.reservation.dto.in;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.*;

@Getter
@Builder
@NoArgsConstructor(access = PRIVATE)
public class PatchReservationDto {
    @NotBlank
    @Pattern(regexp = "^(?i)(true|false)$", message = "입력값은 대소문자 관계 없이 True 혹은 False여야 합니다.")
    private String isAccepted;

    private String message;

    public PatchReservationDto(String isAccepted,
                               String message) {
        this.isAccepted = isAccepted;
        this.message = message;
    }

    public PatchReservationDto(String isAccepted) {
        this.isAccepted = isAccepted;
    }
}
