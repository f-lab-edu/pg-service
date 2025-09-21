package io.github.gugbab2.srt_clone_service.train.service;

import io.github.gugbab2.srt_clone_service.train.domain.Train;
import io.github.gugbab2.srt_clone_service.train.dto.SeatAvailabilityResponse;
import io.github.gugbab2.srt_clone_service.train.dto.TrainSearchRequest;
import io.github.gugbab2.srt_clone_service.train.dto.TrainSearchResponse;
import io.github.gugbab2.srt_clone_service.train.repository.TrainRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainService {

    private final TrainRepository trainRepository;

    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public List<TrainSearchResponse> searchTrainSchedules(TrainSearchRequest request) {
        // Filter trains based on request
        return trainRepository.findAll().stream()
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
