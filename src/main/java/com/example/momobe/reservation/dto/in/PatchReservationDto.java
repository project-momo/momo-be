package com.example.momobe.reservation.dto.in;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.*;

@Getter
public class PatchReservationDto {
    @NotBlank
    @Pattern(regexp = "^(?i)(true|false)$", message = "입력값은 대소문자 관계 없이 True 혹은 False여야 합니다.")
    private final String isAccepted;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PatchReservationDto(@JsonProperty("isAccepted") String isAccepted) {
        this.isAccepted = isAccepted;
    }
}
