# 📝 주요 상태 관리 (Key Status Management)

본 시스템은 '예매'와 '결제'의 상태를 별도로 관리하며, 두 상태는 아래와 같은 흐름으로 상호작용합니다.

#### 예매 (Reservation) 상태
*   `PENDING_PAYMENT`: 결제 대기 중. (좌석 임시 선점)
*   `RESERVED`: 예약 확정. (결제 완료)
*   `CANCELLED`: 사용자에 의해 취소됨.
*   `EXPIRED`: 결제 시간 초과로 시스템에 의해 자동 취소됨.

#### 결제 (Payment) 상태
*   `PAID`: 결제 완료.
*   `FAILED`: 결제 실패.
*   `REFUNDED`: 환불 완료.

#### 상태 변경 흐름
1.  **임시 예약 (`POST /api/reservations`):** `Reservation`이 `PENDING_PAYMENT` 상태로 생성됩니다.
2.  **결제 (`POST /api/reservations/{id}/confirm`):** `Payment`가 `PAID` 상태로 생성되면, 연결된 `Reservation`의 상태가 `RESERVED`로 변경됩니다.
3.  **취소 (`POST /api/reservations/{id}/cancel`):** `Reservation`의 상태가 `CANCELLED`로 변경되면, 환불 절차 후 연결된 `Payment`의 상태가 `REFUNDED`로 변경될 수 있습니다.