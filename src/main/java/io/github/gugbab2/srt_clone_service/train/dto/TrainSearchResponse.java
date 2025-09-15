package io.github.gugbab2.srt_clone_service.train.dto;

import java.time.LocalDateTime;

public record TrainSearchResponse(
    String trainId,
    String trainType,
    String departureStation,
    String arrivalStation,
    LocalDateTime departureTime,
    LocalDateTime arrivalTime,
    SeatAvailabilityResponse seatAvailability
) {
}
