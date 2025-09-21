package io.github.gugbab2.srt_clone_service.train.service;

import io.github.gugbab2.srt_clone_service.train.dto.TrainSearchRequest;
import io.github.gugbab2.srt_clone_service.train.dto.TrainSearchResponse;
import io.github.gugbab2.srt_clone_service.train.dto.SeatAvailabilityResponse;
import org.springframework.stereotype.Service;

import io.github.gugbab2.srt_clone_service.train.domain.Train;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TrainServiceImpl implements TrainService {

    private final List<Train> trains = new ArrayList<>();

    public TrainServiceImpl() {
        trains.add(new Train(UUID.randomUUID().toString(), "SRT101", "수서", "부산", LocalDateTime.of(2025, 9, 15, 9, 0), LocalDateTime.of(2025, 9, 15, 11, 30), 100));
        trains.add(new Train(UUID.randomUUID().toString(), "SRT102", "수서", "부산", LocalDateTime.of(2025, 9, 15, 10, 0), LocalDateTime.of(2025, 9, 15, 12, 30), 80));
        trains.add(new Train(UUID.randomUUID().toString(), "SRT201", "부산", "수서", LocalDateTime.of(2025, 9, 15, 13, 0), LocalDateTime.of(2025, 9, 15, 15, 30), 120));
        trains.add(new Train(UUID.randomUUID().toString(), "SRT202", "수서", "동대구", LocalDateTime.of(2025, 9, 15, 11, 0), LocalDateTime.of(2025, 9, 15, 12, 50), 50));
    }

    @Override
    public List<TrainSearchResponse> searchTrainSchedules(TrainSearchRequest request) {
        if (request.departureStation().equals(request.arrivalStation())) {
            throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
        }

        // Filter trains based on request
        return trains.stream()
                .filter(train -> train.departureStation().equals(request.departureStation()))
                .filter(train -> train.arrivalStation().equals(request.arrivalStation()))
                .filter(train -> !train.departureDateTime().isBefore(request.departureDateTime()))
                .map(this::mapToTrainSearchResponse)
                .collect(Collectors.toList());
    }

    private TrainSearchResponse mapToTrainSearchResponse(Train train) {
        return new TrainSearchResponse(
                train.id(),
                train.trainNumber(),
                train.departureStation(),
                train.arrivalStation(),
                train.departureDateTime(),
                train.arrivalDateTime(),
                new SeatAvailabilityResponse(String.valueOf(train.availableSeats()), "N/A")
        );
    }
}
