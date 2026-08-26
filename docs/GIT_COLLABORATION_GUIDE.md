# GitHub Flow 협업 전략

이 프로젝트는 1인 프로젝트이지만, 실제 협업 프로젝트처럼 Git과 GitHub 기록을 남긴다.

고용노동부 과정 산출물과 외부 점검 가능성을 고려하여, Notion 문서화뿐 아니라 GitHub에도 브랜치, 커밋, PR 기록을 남긴다.

GitHub Flow의 핵심은 `main` 브랜치를 항상 제출 가능한 상태로 유지하고, 작업은 `feature/*` 브랜치에서 진행한 뒤 Pull Request를 통해 병합하는 것이다.

## 1. 1인 팀 역할 정의

| 역할 | 담당 내용 |
| --- | --- |
| Project Manager | 프로젝트 범위, 일정, 산출물 관리 |
| Java 21 Backend Developer | 기존 카페 주문 관리 시스템 기반 백엔드 구현 |
| DB Designer | ERD, FK 설계 논리, MySQL 스키마 작성 |
| JDBC/DAO Developer | JDBC/MySQL 연동 및 DAO 구현 |
| Virtual Thread Researcher | Java 21 가상 스레드 개념 학습과 실험 설계 |
| GitHub Manager | GitHub Flow, 브랜치, 커밋, PR 관리 |
| QA Tester | 실행 확인, 오류 기록, 피드백 보완 |

발표에서는 다음과 같이 설명한다.

```text
1인 프로젝트이지만 실제 협업 프로젝트 흐름을 연습하기 위해 GitHub Flow를 적용했습니다.
main 브랜치는 제출 가능한 상태로 유지하고, 작업은 feature 브랜치와 Pull Request 단위로 관리했습니다.
```

## 2. GitHub Flow 브랜치 전략

```text
main
├─ feature/project-design
├─ feature/db-schema
├─ feature/jdbc-crud
├─ feature/virtual-thread-experiment
└─ feature/final-docs
```

| 브랜치 | 역할 |
| --- | --- |
| `main` | 최종 제출 가능한 안정 버전 |
| `feature/project-design` | Ground Rule, 일정, R&R, GitHub Flow 정리 |
| `feature/db-schema` | ERD 의사결정, FK 설계 논리, MySQL 스키마 작업 |
| `feature/jdbc-crud` | 기존 cafe management system에 JDBC/MySQL 연동 적용 |
| `feature/virtual-thread-experiment` | Java 21 가상 스레드 실행 추적 실험 구현 |
| `feature/final-docs` | 발표자료, Notion 문서, 최종 점검표 정리 |

## 3. 작업 흐름

```text
1. main 브랜치는 항상 제출 가능한 상태로 유지한다.
2. 작업 단위마다 feature 브랜치를 생성한다.
3. 기능 또는 문서 작업을 작은 단위로 커밋한다.
4. 작업이 끝나면 Pull Request를 작성한다.
5. PR에서 변경 내용과 확인한 내용을 기록한다.
6. 확인 후 main 브랜치에 병합한다.
```

## 4. 커밋 메시지 규칙

커밋 메시지는 다음 형식을 사용한다.

```text
type: subject
```

예시는 다음과 같다.

| type | 의미 |
| --- | --- |
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `docs` | 문서 추가 또는 수정 |
| `refactor` | 기능 변화 없는 코드 구조 개선 |
| `test` | 테스트 코드 추가 또는 수정 |
| `chore` | 빌드, 설정, 기타 작업 |

커밋 예시:

```text
docs: ERD 설계 의사결정 과정 정리
docs: ERD FK 설계 논리 정리
feat: 카페 주문 및 가상 스레드 로그 스키마 추가
feat: JDBC 기반 메뉴 조회 DAO 구현
feat: 가상 스레드 주문 처리 실험 구현
docs: 가상 스레드 실험 결과와 발표 설명 추가
```

## 5. 커밋 메시지 템플릿

`.gitmessage.txt`에 다음 템플릿을 둘 수 있습니다.

```text
# type: subject
#
# type:
#   feat     새로운 기능
#   fix      버그 수정
#   docs     문서
#   refactor 리팩토링
#   test     테스트
#   chore    기타 설정
#
# body:
# - 무엇을 변경했는가
# - 왜 변경했는가
# - 어떻게 검증했는가
```

설정 명령:

```bash
git config commit.template .gitmessage.txt
```

## 6. PR 템플릿

PR은 혼자 개발하더라도 작업 단위를 설명하는 기록으로 사용한다.

```md
## 작업 내용
- 

## 변경 이유
- 

## 확인한 기능
- [ ] 빌드 또는 실행 확인
- [ ] ERD와 코드 구조 일치 확인
- [ ] DB 연결 확인
- [ ] 등록 기능 확인
- [ ] 조회 기능 확인
- [ ] 수정 기능 확인
- [ ] 삭제 기능 확인
- [ ] 가상 스레드 로그 확인

## 리뷰 포인트
- 

## 관련 문서
- Notion:
- README:
```

## 7. 오늘의 설계 커밋 목표

오늘은 구현보다 설계 기반을 잡는 커밋을 남긴다.

권장 커밋 단위:

```text
docs: ERD 설계 의사결정 과정 정리
docs: ERD FK 설계 논리 정리
feat: 카페 주문 및 가상 스레드 로그 스키마 추가
```

이 커밋들은 기능 구현이 시작되기 전, ERD와 DB 구조를 먼저 고민했다는 증거가 된다.

## 8. AI 협업 기록 전략

AI를 사용했다는 사실을 숨기기보다, 개발 도구로 통제하며 사용했다는 점을 문서화한다.

기록 예시:

```text
AI 활용 범위
- 요구사항 분석 보조
- DB 테이블 후보 정리
- DAO 설계 검토
- 코드 오류 원인 분석
- README와 발표 설명 초안 작성

개발자 수행 범위
- 요구사항 최종 판단
- DB 구조 선택
- 코드 실행 및 검증
- 오류 수정 방향 결정
- 최종 문서 정리
```

발표용 문장:

```text
AI는 단순 결과물 생성 도구가 아니라 요구사항 정리, 설계 초안 작성, 코드 검토, 문서 보완을 빠르게 반복하기 위한 협업 도구로 활용했습니다.
최종 방향과 반영 여부는 직접 판단했으며, 이해하지 못한 코드나 설명은 프로젝트에 포함하지 않는 것을 원칙으로 했습니다.
```
