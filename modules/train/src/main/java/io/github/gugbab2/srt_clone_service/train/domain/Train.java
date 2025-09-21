package io.github.gugbab2.srt_clone_service.train.domain;

import java.time.LocalDateTime;

public record Train(
        String id,
        String trainNumber,
        String departureStation,
        String arrivalStation,
        LocalDateTime departureDateTime,
        LocalDateTime arrivalDateTime,
        int availableSeats
) {}
