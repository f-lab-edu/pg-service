package io.github.gugbab2.srt_clone_service.train.controller;

import io.github.gugbab2.srt_clone_service.train.dto.TrainSearchRequest;
import io.github.gugbab2.srt_clone_service.train.dto.TrainSearchResponse;
import io.github.gugbab2.srt_clone_service.train.service.TrainService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TrainController.class)
class TrainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainService trainService;

    @Test
    void 요청_값이_유효하지_않은_형식일_경우_400_Bad_Request_에러를_응답한다() throws Exception {
        // given
        // departureDateTime is intentionally malformed
        String invalidDepartureDateTime = "invalid-date-format"; 

        // when & then
        mockMvc.perform(get("/api/trains")
                .param("departureStation", "수서")
                .param("arrivalStation", "부산")
                .param("departureDateTime", invalidDepartureDateTime))
            .andExpect(status().isBadRequest());
    }

    @Test
    void 유효한_조회_조건이_주어졌을_때_열차_시간표를_조회하면_열차_시간표_목록이_반환된다() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        TrainSearchResponse mockResponse = new TrainSearchResponse(
            "SRT-337",
            "SRT",
            "수서",
            "부산",
            now,
            now.plusHours(2),
            null
        );
        given(trainService.searchTrainSchedules(any(TrainSearchRequest.class)))
            .willReturn(List.of(mockResponse));

        // when & then
        mockMvc.perform(get("/api/trains")
                .param("departureStation", "수서")
                .param("arrivalStation", "부산")
                .param("departureDateTime", now.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].trainId").value("SRT-337"))
            .andExpect(jsonPath("$[0].trainType").value("SRT"));
    }
}
