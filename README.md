# 💸 pg-service: 일일 정산 및 리포팅 자동화 시스템

PG(결제 대행사)의 결제 데이터를 실시간으로 수집하고, 일일 배치 프로세스를 통해 내부 데이터와 자동 대사(Reconciliation)하며, 그 결과를 리포팅 API로 제공하는 백엔드 시스템을 구축합니다.

## 🎯 프로젝트 목표 (Project Goals)

- PG(결제 대행사)의 다양한 결제 데이터를 안정적으로 수집하고 처리합니다.
- 실시간 데이터 처리와 대용량 배치 처리를 모두 경험할 수 있는 하이브리드 아키텍처를 구축합니다.
- 정산 결과를 명확하게 조회할 수 있는 리포팅 API를 제공합니다.
- TDD와 클린 코드를 통해 유지보수성이 높고 테스트 가능한 코드를 지향합니다.

## 🌊 워크플로우 (Workflow)

본 시스템은 실시간 데이터 처리와 일일 배치 정산을 결합한 하이브리드 아키텍처를 지향합니다.

### 실시간 결제 승인 (Real-time Flow)
1.  **Webhook 수신:** PG사로부터 결제 승인 이벤트가 발생하면, `PaymentWebhookController`가 HTTP POST 요청으로 Webhook을 수신합니다. 페이로드에는 `transactionId`, `amount`, `timestamp` 등의 정보가 포함됩니다.
2.  **이벤트 발행:** `Controller`는 수신된 데이터를 `PaymentEvent` DTO로 변환한 후, `Message Queue`(e.g., RabbitMQ)에 발행합니다. 이를 통해 Webhook 요청에 대한 응답 시간을 최소화하고 처리를 비동기화합니다.
3.  **데이터베이스 저장:** `PaymentEventConsumer`가 Message Queue를 구독하고 있다가 `PaymentEvent`를 수신합니다. Consumer는 이벤트를 `Payment` 엔티티로 변환하여, '미정산 (UNSETTLED)' 상태로 데이터베이스에 저장합니다.

### 일일 정산 배치 (Batch Flow)
1.  **배치 실행 (Job Trigger):** 매일 새벽, 스케줄러(e.g., Spring Scheduler Cron Job)가 `dailySettlementJob`이라는 이름의 Spring Batch Job을 실행시킵니다.
2.  **파일 읽기 (ItemReader):** Batch Job의 첫 단계인 `SettlementFileReader`가 SFTP 서버에서 전날의 정산 파일을 읽어옵니다. 파일은 CSV 형식이며, 각 라인은 하나의 거래를 나타냅니다.
3.  **데이터 대사 (ItemProcessor):** `ReconciliationProcessor`가 파일의 각 라인을 읽어, 데이터베이스에 저장된 '미정산' 상태의 `Payment` 데이터와 대조합니다. 거래 ID를 기준으로 데이터의 일치 여부와 누락 여부를 확인합니다.
4.  **상태 업데이트 (ItemWriter):** `PaymentStatusUpdateWriter`가 대사가 완료된 `Payment` 엔티티의 상태를 '정산 완료 (SETTLED)'로 변경하여 데이터베이스에 일괄 업데이트합니다. 불일치 내역은 별도의 로그나 테이블에 기록하여 추적합니다.

## ✨ 핵심 원칙 (Core Principles)

이 프로젝트는 아래의 원칙들을 지향하며 개발합니다.

- **Clean Code:** Google Java 스타일 가이드를 준수하여 가독성과 유지보수성을 최우선으로 합니다.
- **Test-Driven Development (TDD):** 새로운 비즈니스 로직은 실패하는 테스트를 먼저 작성하고, 이를 통과시키는 코드를 작성합니다.
- **Immutability:** 가능한 모든 곳에서 불변 객체를 사용하여 시스템의 복잡성을 낮추고 안정성을 높입니다.
- **Clear Commit Messages:** 커밋 메시지는 변경 사항의 '무엇'과 '왜'를 명확하게 설명합니다.

## 🛠️ 기술 스택 (Tech Stack)

- **Language/Framework:** Java 17+, Spring Boot 3, Spring Batch, JPA/Hibernate
- **Build Tool:** Gradle
- **Database:** H2 (개발용), MySQL (운영 고려)
- **Testing:** JUnit 5, Mockito
- **API Documentation:** Springdoc OpenAPI (Swagger UI)
- **Containerization:** Docker

## 🚀 시작하기 (Getting Started)

### Prerequisites

- Java 17+
- Gradle

### Installation & Run

1. **저장소 클론:**
   ```bash
   git clone https://github.com/f-lab-edu/pg-service.git
   cd pg-service
   ```

2. **프로젝트 빌드:**
   ```bash
   ./gradlew build
   ```

3. **애플리케이션 실행:**
   ```bash
   ./gradlew bootRun
   ```

## 📄 API 문서 (API Documentation)

애플리케이션 실행 후, 아래 주소에서 API 문서를 확인할 수 있습니다.

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)