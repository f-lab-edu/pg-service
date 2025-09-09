# 💸 pg-service: 일일 정산 및 리포팅 자동화 시스템

PG(결제 대행사)의 결제 데이터를 실시간으로 수집하고, 일일 배치 프로세스를 통해 내부 데이터와 자동 대사(Reconciliation)하며, 그 결과를 리포팅 API로 제공하는 백엔드 시스템을 구축합니다.

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