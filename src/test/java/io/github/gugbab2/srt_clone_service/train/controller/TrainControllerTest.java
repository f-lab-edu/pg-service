package io.github.gugbab2.srt_clone_service.train.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TrainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("출발역, 도착역, 출발일시를 올바르게 입력하면, 지정된 시간 이후에 출발하는 열차 목록을 정상적으로 조회한다")
    void whenValidSearchConditionsGiven_thenReturnsMatchingTrainScheduleList() throws Exception {
        // given
        LocalDateTime departureDateTime = LocalDateTime.of(2025, 9, 15, 9, 0);

        // when & then
        mockMvc.perform(get("/api/trains")
                        .param("departureStation", "수서")
                        .param("arrivalStation", "부산")
                        .param("departureDateTime", departureDateTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].trainType").value("SRT101"))
                .andExpect(jsonPath("$[1].trainType").value("SRT102"));
    }

    @Test
    @DisplayName("조건에 맞는 열차가 없으면 빈 목록을 반환한다")
    void whenNoMatchingTrainsExist_thenReturnsEmptyList() throws Exception {
        // given
        LocalDateTime departureDateTime = LocalDateTime.of(2025, 9, 15, 9, 0);

        // when & then
        mockMvc.perform(get("/api/trains")
                        .param("departureStation", "미국") // 미국 출발 열차는 없다.
                        .param("arrivalStation", "부산")
                        .param("departureDateTime", departureDateTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("출발 시간보다 이른 시간으로 조회했을 때, 해당 열차는 결과에 포함되지 않는다")
    void whenDepartureTimeIsLater_thenExcludesEarlierTrains() throws Exception {
        // given
        LocalDateTime departureDateTime = LocalDateTime.of(2025, 9, 15, 10, 0);

        // when & then
        mockMvc.perform(get("/api/trains")
                        .param("departureStation", "수서")
                        .param("arrivalStation", "부산")
                        .param("departureDateTime", departureDateTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].trainType").value("SRT102"));
    }

    @Test
    @DisplayName("요청 값이 유효하지 않은 형식(예: 잘못된 날짜)일 경우 400 Bad Request 에러를 응답한다")
    void whenRequestFormatIsInvalid_thenReturns400BadRequest() throws Exception {
        // given
        String invalidDepartureDateTime = "invalid-date-format";

        // when & then
        mockMvc.perform(get("/api/trains")
                        .param("departureStation", "수서")
                        .param("arrivalStation", "부산")
                        .param("departureDateTime", invalidDepartureDateTime))
                .andExpect(status().isBadRequest());
    }
}
