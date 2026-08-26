# 브랜치 전략

본 프로젝트는 GitHub Flow를 기준으로 브랜치 전략을 구성한다.

GitHub Flow는 `main` 브랜치를 항상 제출 가능한 안정 버전으로 유지하고, 새로운 작업은 `feature/*` 브랜치에서 진행한 뒤 Pull Request를 통해 병합하는 방식이다.

본 프로젝트는 1인 프로젝트이지만, 실제 협업 프로젝트처럼 작업 단위를 나누고 변경 사항을 검토하기 위해 이 방식을 적용한다.

## 브랜치 구조

```text
main
├─ feature/project-design
├─ feature/db-schema
├─ feature/jdbc-crud
├─ feature/virtual-thread-experiment
└─ feature/final-docs
```

## 브랜치별 역할

| 브랜치 | 작업 범위 |
| --- | --- |
| `main` | 최종 제출 가능한 안정 버전 |
| `feature/project-design` | Ground Rule, 일정, R&R + To-Do, GitHub Flow, Java 21 기술 선택 문서 |
| `feature/db-schema` | ERD 설계 의사결정, FK 설계 논리, MySQL 스키마 |
| `feature/jdbc-crud` | 기존 카페 주문 관리 시스템에 JDBC/MySQL 연동 적용 |
| `feature/virtual-thread-experiment` | Java 21 가상 스레드 실행 추적 실험 구현 |
| `feature/final-docs` | 발표자료, Notion 문서, 최종 점검표 정리 |

## main 브랜치를 따로 유지하는 이유

`main` 브랜치는 최종 제출 가능한 안정 버전이다.

아직 작성 중인 문서나 테스트 중인 코드를 바로 `main`에 넣으면 제출본이 불안정해질 수 있다.

따라서 `main`에는 검토가 끝난 내용만 병합한다.

```text
main = 제출 가능한 최종본
feature 브랜치 = 작업 중인 공간
```

## feature 브랜치를 나누는 이유

본 프로젝트에는 성격이 다른 작업들이 있다.

```text
프로젝트 운영 기준 정리
ERD와 DB 설계
JDBC/MySQL CRUD 구현
Java 21 가상 스레드 실험
최종 문서 정리
```

이 작업들을 하나의 브랜치에서 섞어서 진행하면 어떤 변경이 어떤 목적의 작업인지 확인하기 어렵다.

그래서 작업 성격별로 `feature/*` 브랜치를 나누었다.

## feature/project-design

이 브랜치는 프로젝트 운영 기준을 정리하기 위한 브랜치이다.

포함 내용:

```text
Ground Rule
Daily Schedule
R&R + To-Do
GitHub Flow
Java 21 기술 선택 이유
```

이 브랜치를 따로 둔 이유는 구현을 시작하기 전에 프로젝트 방향과 작업 기준을 먼저 확정하기 위해서이다.

## feature/db-schema

이 브랜치는 DB 설계와 관련된 작업을 관리한다.

포함 내용:

```text
ERD 설계 의사결정 과정
FK 설계 논리
schema.sql
```

이 브랜치를 따로 둔 이유는 DB 구조가 JDBC 코드와 직접 연결되기 때문이다.

먼저 DB 구조를 확정해야 DAO/DTO와 CRUD 구현 방향이 흔들리지 않는다.

## feature/jdbc-crud

이 브랜치는 기존 카페 주문 관리 시스템에 JDBC/MySQL을 연결하는 작업을 담당한다.

포함 내용:

```text
JDBC 연결 설정
DAO/DTO 구현
주문 등록, 조회, 수정, 삭제
```

이 브랜치를 따로 둔 이유는 기존 `ArrayList` 기반 저장소를 MySQL 기반 DAO로 전환하는 작업이 핵심 구현 단계이기 때문이다.

## feature/virtual-thread-experiment

이 브랜치는 본 프로젝트의 핵심 학습 목표인 Java 21 가상 스레드 실험을 담당한다.

포함 내용:

```text
여러 주문 처리 작업 생성
Virtual Thread 실행
DB I/O 대기 구간 기록
스레드 이름, 시작/종료 시간 로그 저장
VIRTUAL_THREAD_LOG 연동
```

이 브랜치를 따로 둔 이유는 가상 스레드 실험이 단순 CRUD 구현과 목적이 다르기 때문이다.

CRUD는 필수 요구사항 충족을 위한 기능이고, 가상 스레드 실험은 Java 21 백엔드 동시성 구조를 이해하기 위한 핵심 학습 부분이다.

## feature/final-docs

이 브랜치는 최종 제출 자료를 정리하는 브랜치이다.

포함 내용:

```text
README 정리
Notion 문서 보완
발표 스크립트
예상 질문 답변
최종 요구사항 체크리스트
```

이 브랜치를 따로 둔 이유는 구현이 끝난 뒤 제출물과 발표 자료를 한 번 더 검토하기 위해서이다.

## Pull Request를 사용하는 이유

각 `feature/*` 브랜치 작업이 끝나면 Pull Request를 생성한다.

PR에는 다음 내용을 기록한다.

```text
무엇을 변경했는가
왜 변경했는가
어떻게 확인했는가
관련 문서는 무엇인가
```

1인 프로젝트이지만 PR을 사용하는 이유는 다음과 같다.

```text
작업 단위를 명확히 기록하기 위해
main 브랜치에 바로 반영하지 않기 위해
설계, 구현, 문서화 과정을 단계별로 남기기 위해
협업 프로젝트의 GitHub Flow를 연습하기 위해
```

## 최종 요약

본 프로젝트는 `main` 브랜치를 최종 제출 가능한 안정 버전으로 유지하고, 작업 성격에 따라 `feature/*` 브랜치를 나누어 진행한다.

각 feature 브랜치는 Pull Request를 통해 `main`에 병합하며, 이를 통해 1인 프로젝트에서도 협업 프로젝트와 같은 방식으로 작업 과정을 기록한다.

## 발표용 핵심 문장

```text
main은 제출 가능한 최종본으로 유지하고,
설계, DB, JDBC, 가상 스레드 실험, 최종 문서 작업을 각각 feature 브랜치로 나누었습니다.

작업이 끝날 때마다 Pull Request를 만들어 변경 내용과 확인 사항을 기록함으로써,
1인 프로젝트에서도 GitHub Flow 기반 협업 방식을 적용했습니다.
```
