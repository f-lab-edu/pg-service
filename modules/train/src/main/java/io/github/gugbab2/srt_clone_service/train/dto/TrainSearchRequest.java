package io.github.gugbab2.srt_clone_service.train.dto;

import io.github.gugbab2.srt_clone_service.train.validation.NotSameStation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NotSameStation
public record TrainSearchRequest(
    @NotBlank(message = "출발역은 필수입니다.")
    String departureStation,

    @NotBlank(message = "도착역은 필수입니다.")
    String arrivalStation,

    @NotNull(message = "출발일시는 필수입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime departureDateTime
) {
}
