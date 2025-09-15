package io.github.gugbab2.srt_clone_service.train.service;

import io.github.gugbab2.srt_clone_service.train.dto.TrainSearchRequest;
import io.github.gugbab2.srt_clone_service.train.dto.TrainSearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TrainServiceImplTest {

    private TrainServiceImpl trainService;

    @BeforeEach
    void setUp() {
        trainService = new TrainServiceImpl();
    }

    @Test
    void 유효한_출발역과_도착역_출발시간이_주어졌을_때_열차_시간표를_조회하면_조건에_맞는_열차_목록이_반환된다() {
        // given
        TrainSearchRequest request = new TrainSearchRequest("수서", "부산", LocalDateTime.of(2025, 9, 15, 9, 0));

        // when
        List<TrainSearchResponse> result = trainService.searchTrainSchedules(request);

        // then
        assertThat(result).hasSize(2);
        assertAll(
                () -> assertThat(result.get(0).trainType()).isEqualTo("SRT101"),
                () -> assertThat(result.get(0).departureStation()).isEqualTo("수서"),
                () -> assertThat(result.get(0).arrivalStation()).isEqualTo("부산"),
                () -> assertThat(result.get(0).departureTime()).isEqualTo(LocalDateTime.of(2025, 9, 15, 9, 0)),
                () -> assertThat(result.get(0).arrivalTime()).isEqualTo(LocalDateTime.of(2025, 9, 15, 11, 30)),
                () -> assertThat(result.get(0).seatAvailability().standard()).isEqualTo("100"),

                () -> assertThat(result.get(1).trainType()).isEqualTo("SRT102"),
                () -> assertThat(result.get(1).departureStation()).isEqualTo("수서"),
                () -> assertThat(result.get(1).arrivalStation()).isEqualTo("부산"),
                () -> assertThat(result.get(1).departureTime()).isEqualTo(LocalDateTime.of(2025, 9, 15, 10, 0)),
                () -> assertThat(result.get(1).arrivalTime()).isEqualTo(LocalDateTime.of(2025, 9, 15, 12, 30)),
                () -> assertThat(result.get(1).seatAvailability().standard()).isEqualTo("80")
        );
    }

    @Test
    void 일치하는_열차가_없는_조건이_주어졌을_때_열차_시간표를_조회하면_빈_목록이_반환된다() {
        // given
        TrainSearchRequest request = new TrainSearchRequest("서울", "부산", LocalDateTime.of(2025, 9, 15, 9, 0)); // No trains from Seoul

        // when
        List<TrainSearchResponse> result = trainService.searchTrainSchedules(request);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void 출발_시간보다_이른_시간으로_조회했을_때_열차_시간표를_조회하면_해당_열차는_포함되지_않는다() {
        // given
        TrainSearchRequest request = new TrainSearchRequest("수서", "부산", LocalDateTime.of(2025, 9, 15, 10, 0)); // Should exclude SRT101

        // when
        List<TrainSearchResponse> result = trainService.searchTrainSchedules(request);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).trainType()).isEqualTo("SRT102");
    }
}