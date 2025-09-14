# ✨ 주요 기능 (Core Features)

### 1. 열차 시간표 조회

*   **시나리오:**
    모든 사용자(회원 또는 비회원)는 클라이언트를 통해 출발역, 도착역, 출발일과 시간을 지정하여 운행하는 열차 시간표를 조회할 수 있습니다. API 서버는 지정된 시간 이후에 출발하는 열차 목록을 좌석 등급별 예매 가능 상태와 함께 응답합니다. 만약 조건에 맞는 열차가 없으면 빈 목록을 반환하고, 요청 값이 잘못된 경우에는 에러를 응답합니다.

*   **API 명세:**
    *   **Endpoint:** `GET /api/trains`
    *   **Request (Query Params):**
        ```
        - departureStation (string): "수서"
        - arrivalStation (string): "부산"
        - departureDateTime (string: ISO 8601): "2025-10-20T14:00:00"
        ```
    *   **Success Response (200 OK):**
        ```json
        [{
          "trainId": "SRT-337",
          "trainType": "SRT",
          "departureStation": "수서",
          "arrivalStation": "부산",
          "departureTime": "2025-10-20T14:00:00",
          "arrivalTime": "2025-10-20T16:30:00",
          "seatAvailability": {
            "standard": "AVAILABLE",
            "first": "SOLD_OUT"
          }
        }]
        ```
    *   **Error Response (400 Bad Request):**
        ```json
        {
          "errorCode": "INVALID_INPUT_VALUE",
          "message": "요청 값이 올바르지 않습니다.",
          "description": "departureStation과 arrivalStation은 같을 수 없습니다."
        }
        ```

### 2. 좌석 배치도 조회

*   **시나리오:**
    사용자가 특정 열차를 선택하면, 클라이언트는 먼저 해당 열차의 **호차(Car) 목록**을 조회합니다. 그 후, 사용자가 특정 호차를 선택하면 해당 호차의 좌석 배치도를 요청합니다. 이처럼 API를 두 단계로 분리함으로써, 한 번에 너무 많은 좌석 정보를 보내 발생하는 성능 저하를 방지합니다. 각 좌석은 별도의 고유 ID 없이 좌석 번호 자체로 식별됩니다.

*   **API 명세:**
    *   **2-1. 호차 목록 조회:**
        *   **Endpoint:** `GET /api/trains/{trainId}/cars`
        *   **Request (Path Params):**
            ```
            - trainId (string): "SRT-337"
            ```
        *   **Success Response (200 OK):**
            ```json
            [
              {"carNumber": 1, "seatType": "FIRST"},
              {"carNumber": 2, "seatType": "STANDARD"}
            ]
            ```
        *   **Error Response (400 Bad Request):**
            ```json
            {
              "errorCode": "TRAIN_NOT_FOUND",
              "message": "열차 정보를 찾을 수 없습니다.",
              "description": "trainId 'SRT-337'에 해당하는 열차가 없습니다."
            }
            ```
    *   **2-2. 특정 호차의 좌석 배치도 조회:**
        *   **Endpoint:** `GET /api/trains/{trainId}/cars/{carNumber}/seats`
        *   **Request (Path Params):**
            ```
            - trainId (string): "SRT-337"
            - carNumber (integer): 3
            ```
        *   **Success Response (200 OK):**
            ```json
            [
              {"seatNumber": "1A", "isAvailable": true},
              {"seatNumber": "1B", "isAvailable": false}
            ]
            ```
        *   **Error Response (400 Bad Request):**
            ```json
            {
              "errorCode": "CAR_NOT_FOUND",
              "message": "호차 정보를 찾을 수 없습니다.",
              "description": "3번 호차는 해당 열차에 존재하지 않습니다."
            }
            ```

### 3. 좌석 예매 (임시 예약)

*   **시나리오:**
    사용자가 좌석을 선택하면, 클라이언트는 **임시 예약을 생성**하기 위해 API를 호출합니다. 서버는 해당 좌석을 약 10분간 `결제 대기(PENDING_PAYMENT)` 상태로 선점하고, 생성된 `reservationId`와 만료 시간을 응답합니다. 이 시간 안에 결제가 완료되지 않으면 임시 예약은 자동으로 취소됩니다.

*   **API 명세:**
    *   **Endpoint:** `POST /api/reservations`
    *   **Request (Header):** `Authorization: Bearer <JWT>`
    *   **Request (Body):**
        ```json
        {
          "trainId": "SRT-337",
          "carNumber": 3,
          "seatNumber": "5A"
        }
        ```
    *   **Success Response (200 OK):**
        ```json
        {
          "reservationId": "R-20250915-1A2B3C",
          "status": "PENDING_PAYMENT",
          "expiresAt": "2025-10-20T14:10:00"
        }
        ```
    *   **Error Responses:** `401 Unauthorized`,
        ```json
        // 400 Bad Request의 경우
        {
          "errorCode": "SEAT_ALREADY_RESERVED",
          "message": "이미 선택된 좌석입니다.",
          "description": "요청하신 3호차 5A 좌석은 다른 사용자에 의해 선점되었습니다."
        }
        ```

### 4. 예매 확정

*   **시나리오:**
    클라이언트는 임시 예약 후 받은 `reservationId`로 결제를 진행하고, 결제가 성공하면 서버에 **예매 확정**을 요청합니다. 서버는 해당 예매의 상태를 `예약 완료(RESERVED)`로 변경하고, 최종 티켓 정보를 응답합니다.

*   **API 명세:**
    *   **Endpoint:** `POST /api/reservations/{reservationId}/confirm`
    *   **Request (Header):** `Authorization: Bearer <JWT>`
    *   **Request (Path Params):**
        ```
        - reservationId (string): "R-20250915-1A2B3C"
        ```
    *   **Request (Body):** (결제 관련 정보, 지금은 비워둠) `{}`
    *   **Success Response (200 OK):**
        ```json
        {
          "reservationId": "R-20250915-1A2B3C",
          "status": "RESERVED",
          "trainInfo": {
            "trainNumber": "SRT 337",
            "departureStation": "수서",
            "arrivalStation": "부산",
            "departureTime": "2025-10-20T14:00:00",
            "arrivalTime": "2025-10-20T16:30:00"
          },
          "seatInfo": {
            "carNumber": 3,
            "seatNumber": "5A"
          }
        }
        ```
    *   **Error Responses:** `401 Unauthorized`,
        ```json
        // 400 Bad Request의 경우 (예: 예약 만료)
        {
          "errorCode": "RESERVATION_NOT_FOUND",
          "message": "예약을 찾을 수 없습니다.",
          "description": "요청하신 예약 ID가 존재하지 않거나 만료되었습니다."
        }
        ```

### 5. 예매 확인 및 취소

*   **시나리오:**
    로그인한 회원은 자신의 예매 내역을 조회하거나 취소할 수 있습니다. '예매 목록' 조회를 요청하면, API 서버는 해당 회원의 모든 예매 내역(`결제 대기`, `예약 완료`, `취소` 등 모든 상태 포함)을 반환합니다. '취소' 요청 시, 서버는 예매의 현재 상태를 확인합니다.
    - **`RESERVED` (결제 완료) 상태일 경우:** 환불 절차를 진행하고, `Payment` 상태를 `REFUNDED`로 변경합니다.
    - **`PENDING_PAYMENT` (결제 대기) 상태일 경우:** 결제된 내역이 없으므로, 즉시 예약을 `CANCELLED` 상태로 변경하고 좌석을 다시 '예매 가능' 상태로 만듭니다.

*   **API 명세:**
    *   **예매 목록 조회:**
        *   **Endpoint:** `GET /api/reservations`
        *   **Request (Header):** `Authorization: Bearer <JWT>`
        *   **Success Response (200 OK):**
            ```json
            [{
              "reservationId": "R-20250915-1A2B3C",
              "status": "RESERVED", // PENDING_PAYMENT, CANCELLED, EXPIRED 등
              "trainInfo": {
                "trainNumber": "SRT 337",
                "departureStation": "수서",
                "arrivalStation": "부산",
                "departureTime": "2025-10-20T14:00:00",
                "arrivalTime": "2025-10-20T16:30:00"
              },
              "seatInfo": {
                "carNumber": 3,
                "seatNumber": "5A"
              }
            }]
            ```
        *   **Error Response:** `401 Unauthorized`
    *   **예매 취소:**
        *   **Endpoint:** `POST /api/reservations/{reservationId}/cancel`
        *   **Request (Header):** `Authorization: Bearer <JWT>`
        *   **Request (Path Params):**
            ```
            - reservationId (string): "R-20250915-1A2B3C"
            ```
        *   **Success Response (200 OK):**
            ```json
            // Case 1: RESERVED 상태에서 취소한 경우
            {
              "reservationId": "R-20250915-1A2B3C",
              "reservationStatus": "CANCELLED",
              "paymentStatus": "REFUNDED",
              "cancellationFee": 1200,
              "refundAmount": 50800
            }
            ```
            ```json
            // Case 2: PENDING_PAYMENT 상태에서 취소한 경우
            {
              "reservationId": "R-20250915-1A2B3C",
              "reservationStatus": "CANCELLED",
              "paymentStatus": null,
              "cancellationFee": 0,
              "refundAmount": 0
            }
            ```
        *   **Error Responses:** `401 Unauthorized`,
            ```json
            // 400 Bad Request의 경우 (예: 취소 불가)
            {
              "errorCode": "CANCELLATION_NOT_ALLOWED",
              "message": "예매를 취소할 수 없습니다.",
              "description": "열차 출발 시간이 지나 취소가 불가능합니다."
            }
            ```