# 🚄 srt-clone-service: SRT 예매 시스템 클론 API 서버

SRT(수서고속철도) 예매 시스템의 핵심 기능을 API 서버로 구현하는 프로젝트입니다. 이 프로젝트를 통해 대용량 트래픽 처리, 동시성 제어, 데이터 관리 등 백엔드 개발의 주요 과제들을 TDD와 클린 코드 원칙에 따라 해결하는 경험을 목표로 합니다.

- [✨ 주요 기능 (Core Features)](./docs/features.md)
- [📝 주요 상태 관리 (Key Status Management)](./docs/status_management.md)
- [🧪 테스트 케이스 (Test Cases)](./docs/test_cases.md)

## 🛠️ 기술 스택 (Tech Stack)

- **Language/Framework:** Java 17+, Spring Boot 3
- **Build Tool:** Gradle
- **Testing:** JUnit 5, Mockito

## 🚀 시작하기 (Getting Started)

### Installation & Run

1.  **저장소 클론:**
    ```bash
    git clone <repository-url>
    cd srt-clone-service
    ```

2.  **프로젝트 빌드:**
    ```bash
    ./gradlew build
    ```

3.  **애플리케이션 실행:**
    ```bash
    ./gradlew bootRun
    ```