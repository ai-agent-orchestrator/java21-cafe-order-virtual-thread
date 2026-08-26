# Git/GitHub 요구사항 체크리스트

본 프로젝트는 1인 프로젝트이지만, 실제 협업 프로젝트의 흐름을 따르기 위해 GitHub Flow를 적용한다.

`main` 브랜치는 제출 가능한 안정 버전으로 유지하고, 설계, DB, JDBC, 가상 스레드 실험 작업은 `feature/*` 브랜치에서 진행한 뒤 Pull Request를 통해 병합한다.

이를 통해 프로젝트 산출물이 한 번에 생성되는 방식이 아니라, 요구사항 분석, 설계, 구현, 검증, 보완 과정을 단계적으로 거쳐 관리되도록 한다.

## 1. Git/GitHub 요구사항 체크리스트

| 요구사항 | 적용 계획 | 산출물 |
| --- | --- | --- |
| GitHub Repository 생성 | 이번 프로젝트 전용 Repository를 생성하여 독립적으로 관리한다. | `java21-cafe-order-virtual-thread` |
| main 브랜치 관리 | `main` 브랜치는 최종 제출 가능한 안정 버전으로 유지한다. | `main` branch |
| 브랜치 전략 수립 | 작업 단위별 `feature/*` 브랜치를 생성하여 개발한다. | `feature/project-design`, `feature/db-schema`, `feature/jdbc-crud`, `feature/virtual-thread-experiment`, `feature/final-docs` |
| 커밋 메시지 규칙 | 작업 내용을 명확히 구분할 수 있도록 커밋 메시지 타입을 사용한다. | `docs:`, `feat:`, `fix:`, `refactor:`, `test:`, `chore:` |
| 커밋 메시지 템플릿 | 커밋 작성 시 변경 내용, 변경 이유, 검증 내용을 확인할 수 있도록 템플릿을 사용한다. | `.gitmessage.txt` |
| PR 템플릿 | Pull Request마다 작업 내용, 변경 이유, 확인 항목을 기록한다. | `.github/pull_request_template.md` |
| 단계별 커밋 | 설계, ERD, DB 스키마, JDBC 연동, 가상 스레드 실험, 문서화를 나누어 커밋한다. | Git commit log |
| Pull Request 기록 | 기능 또는 문서 작업이 완료될 때마다 PR을 작성하고 병합한다. | GitHub Pull Request |
| ERD 설계 기록 | ERD를 바로 작성하지 않고, 설계 조건과 의사결정 과정을 문서화한다. | `docs/ERD_DESIGN_DECISION.md` |
| FK 설계 기록 | 각 FK가 어떤 데이터 흐름을 표현하는지 문서화한다. | `docs/ERD_FK_DESIGN.md` |
| GitHub Flow 문서화 | 브랜치 전략, 커밋 규칙, PR 관리 방식을 문서화한다. | `docs/GIT_COLLABORATION_GUIDE.md`, `docs/BRANCH_STRATEGY.md` |
| 최종 제출 링크 정리 | Notion에 GitHub Repository, 주요 PR, 주요 커밋 링크를 정리한다. | Notion 제출 문서 |

## 2. 브랜치 전략

```text
main
├─ feature/project-design
├─ feature/db-schema
├─ feature/jdbc-crud
├─ feature/virtual-thread-experiment
└─ feature/final-docs
```

| 브랜치 | 작업 범위 |
| --- | --- |
| `main` | 최종 제출 가능한 안정 버전 |
| `feature/project-design` | Ground Rule, 일정, R&R, GitHub Flow 정리 |
| `feature/db-schema` | ERD 의사결정, FK 설계 논리, MySQL 스키마 |
| `feature/jdbc-crud` | 기존 카페 주문 관리 시스템에 JDBC/MySQL 연동 적용 |
| `feature/virtual-thread-experiment` | Java 21 가상 스레드 실행 추적 실험 구현 |
| `feature/final-docs` | 발표자료, Notion 문서, 최종 점검표 정리 |

## 3. 커밋 메시지 규칙

커밋 메시지는 다음 형식을 사용한다.

```text
type: 작업 내용 요약
```

| type | 의미 |
| --- | --- |
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `docs` | 문서 추가 또는 수정 |
| `refactor` | 기능 변화 없는 코드 구조 개선 |
| `test` | 테스트 또는 실행 확인 코드 |
| `chore` | 프로젝트 설정, 초기 구성, 기타 작업 |

커밋 메시지 예시:

```text
chore: 카페 CRUD DB 연동 및 Java 21 가상 스레드 프로젝트
docs: 프로젝트 운영 기준과 GitHub Flow 정리
docs: ERD 설계 의사결정 과정 정리
docs: ERD FK 설계 논리 정리
feat: 카페 주문 및 가상 스레드 로그 스키마 추가
feat: JDBC 기반 메뉴 조회 DAO 구현
feat: 주문 CRUD MySQL 연동 구현
feat: 가상 스레드 주문 처리 실험 구현
docs: 가상 스레드 실험 결과와 발표 설명 추가
```

## 4. Pull Request 관리 방식

Pull Request에는 다음 항목을 기록한다.

```text
1. 작업 내용
2. 변경 이유
3. 확인한 내용
4. 리뷰 포인트
5. 관련 문서
```

PR 체크 항목 예시:

```text
- main 브랜치에서 직접 작업하지 않았는가
- feature 브랜치에서 작업했는가
- 커밋 메시지 규칙을 지켰는가
- 관련 문서를 업데이트했는가
- 빌드 또는 실행 확인을 했는가
- PR 설명을 작성했는가
```

JDBC/CRUD 작업에서는 다음 항목을 추가로 확인한다.

```text
- DB 연결 확인
- 등록 기능 확인
- 조회 기능 확인
- 수정 기능 확인
- 삭제 기능 확인
```

Java 21 가상 스레드 실험 작업에서는 다음 항목을 추가로 확인한다.

```text
- Virtual Thread 실행 확인
- 스레드 이름 출력 확인
- DB I/O 대기 구간 로그 확인
- 시작/종료 시간 기록 확인
- 실행 결과 문서화
```

## 5. 최종 정리

본 프로젝트는 1인 프로젝트이지만, GitHub Flow를 적용하여 협업 프로젝트와 같은 방식으로 작업 과정을 관리한다.

설계, DB 스키마, JDBC CRUD, Java 21 가상 스레드 실험을 각각 브랜치와 PR 단위로 나누어 기록함으로써 프로젝트 진행 과정을 GitHub에서 확인할 수 있도록 한다.
