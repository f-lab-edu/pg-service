package io.github.gugbab2.srt_clone_service.train.service;

import io.github.gugbab2.srt_clone_service.train.dto.TrainSearchRequest;
import io.github.gugbab2.srt_clone_service.train.dto.TrainSearchResponse;

import java.util.List;

public interface TrainService {
    List<TrainSearchResponse> searchTrainSchedules(TrainSearchRequest request);
}
