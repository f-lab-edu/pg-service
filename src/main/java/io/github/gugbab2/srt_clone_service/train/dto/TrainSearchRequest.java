package io.github.gugbab2.srt_clone_service.train.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record TrainSearchRequest(
    @NotBlank(message = "출발역은 필수입니다.")
    String departureStation,

    @NotBlank(message = "도착역은 필수입니다.")
    String arrivalStation,

    @NotNull(message = "출발일시는 필수입니다.")
    LocalDateTime departureDateTime
) {
}
