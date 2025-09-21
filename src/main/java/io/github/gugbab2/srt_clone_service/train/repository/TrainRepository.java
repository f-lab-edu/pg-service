package io.github.gugbab2.srt_clone_service.train.repository;

import io.github.gugbab2.srt_clone_service.train.domain.Train;

import java.util.List;

public interface TrainRepository {
    List<Train> findAll();
}
