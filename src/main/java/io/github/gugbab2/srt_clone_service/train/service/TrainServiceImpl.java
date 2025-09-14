package io.github.gugbab2.srt_clone_service.train.service;

import io.github.gugbab2.srt_clone_service.train.dto.TrainSearchRequest;
import io.github.gugbab2.srt_clone_service.train.dto.TrainSearchResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainServiceImpl implements TrainService {

    @Override
    public List<TrainSearchResponse> searchTrainSchedules(TrainSearchRequest request) {
        // TODO: Implement actual logic to search train schedules
        return List.of(); // Return an empty list for now to make the test compile
    }
}
