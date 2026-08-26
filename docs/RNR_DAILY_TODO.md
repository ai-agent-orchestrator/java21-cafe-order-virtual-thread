# R&R + Daily Schedule + To-Do

본 프로젝트는 1인 프로젝트이지만, 실제 협업 프로젝트의 흐름을 따라 작업을 관리한다.

각 역할은 별도 팀원이 아니라 프로젝트 수행에 필요한 관점을 의미하며, To-Do List와 함께 기록하여 기획, 설계, 구현, 검증, 문서화 과정을 단계적으로 관리한다.

## R&R + Daily To-Do

| Day | 역할 및 작업 | 산출물 |
| --- | --- | --- |
| Day 1 | Project Manager: 프로젝트 기준과 초기 설계 관리 | Ground Rule, Daily Schedule, R&R + To-Do 통합표, ERD 설계 draft, 필수 요구사항 체크리스트 초안 |
| Day 1 | Java 21 Researcher: Java 21 선택 이유 정리 | Java 21 LTS 선택 이유, 가상 스레드 학습 목표, 기술 선택 정리 |
| Day 1 | GitHub Manager: GitHub Flow 전략 정리 | `main`, `feature/*` 브랜치 전략, PR 단위, 커밋 메시지 규칙 |
| Day 2 | DB Designer: MySQL 테이블 구조 확정 | 논리 ERD 보완, DB 테이블 명세, `schema.sql` |
| Day 2 | JDBC/DAO Developer: Java <-> MySQL 연동 준비 | JDBC 연결 계획, DAO/DTO 구조 초안, MySQL Connector 설정 |
| Day 3 | JDBC/DAO Developer: JDBC 기반 CRUD 구현 | MenuDAO, OrderDAO, OrderItemDAO, 주문 CRUD 연동 코드 |
| Day 3 | QA Tester: CRUD 기능 실행 확인 | 콘솔 실행 결과, DB 반영 확인, 오류 수정 기록 |
| Day 4 | Java 21 Researcher: 가상 스레드 개념 학습 | 플랫폼 스레드와 가상 스레드 차이, I/O 중심 작업에서의 장점 정리 |
| Day 4 | Virtual Thread Developer: 가상 스레드 실험 설계 | 주문 처리/조회 작업을 가상 스레드로 실행하는 실험 계획 |
| Weekend | Virtual Thread Developer / GitHub Manager: 가상 스레드 실험 구현, 실행 확인, 단계별 커밋 및 feedback 보완 | 실험 코드, 콘솔 실행 로그, `feature/virtual-thread-experiment` 커밋, PR 기록, 보완 메모 |
| Day 5 | QA Tester: 전체 기능 최종 검증 | 전체 CRUD 실행 캡처, JDBC 적용 전후 비교, 예상 오류 점검 |
| Day 5 | Project Manager: 최종 제출 자료 정리 | Notion 최종 문서, 발표 스크립트, 예상 질문 답변, 필수 요구사항 최종 점검표 |

## 역할 정의

| 역할 | 의미 |
| --- | --- |
| Project Manager | 프로젝트 범위, 일정, 산출물, 최종 제출 자료를 관리한다. |
| Java 21 Researcher | Java 21 선택 이유와 가상 스레드 개념을 정리한다. |
| DB Designer | ERD, FK 설계 논리, MySQL 테이블 구조를 설계한다. |
| JDBC/DAO Developer | 기존 카페 주문 관리 시스템에 JDBC/MySQL 연동을 적용한다. |
| Virtual Thread Developer | Java 21 가상 스레드 실험 코드를 설계하고 구현한다. |
| GitHub Manager | GitHub Flow, 브랜치, 커밋, PR 기록을 관리한다. |
| QA Tester | 실행 결과를 확인하고 오류 및 보완 사항을 기록한다. |

## 일정 운영 원칙

본 프로젝트는 기존에 개발한 `cafe management system`을 기반으로 진행한다.

새로운 CRUD 기능을 처음부터 다시 만드는 것보다, 기존 구조를 복습하고 JDBC/MySQL 데이터 연동과 Java 21 가상 스레드 실험을 적용하는 데 집중한다.

기본 주문 CRUD는 필수 요구사항 충족을 위한 범위로 유지하고, 추가 코딩은 Java 21 가상 스레드 이해와 직접 연결되는 부분에 집중한다.

## Weekend 보완 계획

Day 4에는 Java 21 가상 스레드 개념 학습과 실험 설계를 진행한다.

Weekend에는 해당 내용을 바탕으로 가상 스레드 실험 구현, 실행 확인, 단계별 커밋, feedback 보완을 진행한다.

이 과정을 통해 가상 스레드 실험이 단순 설명으로 끝나지 않고, 실행 코드와 콘솔 로그, GitHub 기록으로 남도록 한다.
