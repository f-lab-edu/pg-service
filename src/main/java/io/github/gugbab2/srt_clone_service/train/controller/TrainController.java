package io.github.gugbab2.srt_clone_service.train.controller;

import io.github.gugbab2.srt_clone_service.train.dto.TrainSearchRequest;
import io.github.gugbab2.srt_clone_service.train.dto.TrainSearchResponse;
import io.github.gugbab2.srt_clone_service.train.service.TrainService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
public class TrainController {

    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping
    public ResponseEntity<List<TrainSearchResponse>> searchTrainSchedules(@Valid TrainSearchRequest request) {
        List<TrainSearchResponse> schedules = trainService.searchTrainSchedules(request);
        return ResponseEntity.ok(schedules);
    }
}
