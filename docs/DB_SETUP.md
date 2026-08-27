# DB 실행 환경 설정

## 1. 문서 목적

이 문서는 Java 애플리케이션과 MySQL을 연결하기 위한 DB 실행 환경을 정리하기 위해 작성하였다.

본 프로젝트는 기존 카페 주문 관리 CRUD를 JDBC/MySQL 기반으로 전환하는 것을 목표로 한다.

따라서 Java 코드 실행 전에 MySQL 데이터베이스와 테이블이 먼저 준비되어야 한다.

## 2. 사용 환경

| 항목 | 내용 |
| --- | --- |
| Java 버전 | Java 21 LTS |
| DBMS | MySQL |
| DB 이름 | `cafe_order_db` |
| SQL 스키마 파일 | `sql/schema.sql` |
| JDBC 드라이버 | MySQL Connector/J |
| DB 연결 클래스 | `ConnectionManager` |
| 실행 도구 | IntelliJ Database Tool |

## 3. schema.sql의 역할

`schema.sql`은 ERD를 실제 MySQL 데이터베이스에 반영하기 위한 SQL 파일이다.

ERD가 데이터 구조를 그림으로 표현한 설계도라면, `schema.sql`은 그 설계도를 MySQL에서 실행 가능한 명령어로 옮긴 파일이다.

본 프로젝트의 `schema.sql`에는 다음 내용이 포함된다.

| 항목 | 내용 |
| --- | --- |
| 데이터베이스 생성 | `cafe_order_db` 생성 |
| 테이블 생성 | `menu`, `cafe_order`, `order_item`, `virtual_thread_log` |
| FK 관계 설정 | 주문-주문상세, 메뉴-주문상세, 주문-가상스레드로그 연결 |
| 초기 데이터 입력 | 카페 메뉴 8개 입력 |

## 4. IntelliJ에서 schema.sql 실행

수업 시간에 사용한 방식과 동일하게 IntelliJ Database Tool에서 MySQL 연결을 설정하고 `schema.sql`을 실행하였다.

실행 순서는 다음과 같다.

1. IntelliJ에서 최종 프로젝트 폴더를 연다.

```text
C:\myLectureWs\java21-cafe-order-virtual-thread
```

2. Database Tool에서 MySQL 연결을 설정한다.

```text
Host: localhost
Port: 3306
User: root
Password: 개인 MySQL 비밀번호
```

3. 연결 테스트를 실행하여 성공 여부를 확인한다.

4. 프로젝트의 `sql/schema.sql` 파일을 연다.

5. MySQL 연결을 선택한 뒤 전체 SQL을 실행한다.

6. 실행 결과 메시지를 확인한다.

```text
cafe_order_db schema created
```

## 5. 실행 확인 SQL

`schema.sql` 실행 후 다음 SQL로 DB 생성과 초기 데이터 입력을 확인하였다.

```sql
USE cafe_order_db;
SHOW TABLES;
SELECT * FROM menu;
```

## 6. 확인 결과

`SELECT * FROM menu;` 실행 결과 초기 메뉴 데이터 8건이 조회되었다.

| menu_id | name | category | price |
| --- | --- | --- | --- |
| 1 | 아메리카노 | COFFEE | 3000 |
| 2 | 카페라떼 | COFFEE | 4200 |
| 3 | 바닐라라떼 | COFFEE | 4800 |
| 4 | 캐모마일 | TEA | 3900 |
| 5 | 레몬에이드 | ADE | 5200 |
| 6 | 자몽에이드 | ADE | 5400 |
| 7 | 치즈케이크 | DESSERT | 6200 |
| 8 | 초코쿠키 | DESSERT | 2800 |

이를 통해 `schema.sql`이 정상 실행되었고, MySQL에 초기 메뉴 데이터가 정상 입력된 것을 확인하였다.

## 7. Java 코드와 DB 연결

Java 코드에서는 `ConnectionManager`가 DB 연결을 담당한다.

기본 연결 정보는 다음과 같다.

```text
jdbc:mysql://localhost:3306/cafe_order_db?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
```

DB 계정과 비밀번호는 다음 방식으로 설정할 수 있도록 구성하였다.

| 설정 방식 | 이름 |
| --- | --- |
| 환경 변수 | `CAFE_DB_URL`, `CAFE_DB_USER`, `CAFE_DB_PASSWORD` |
| JVM 옵션 | `cafe.db.url`, `cafe.db.user`, `cafe.db.password` |

이를 통해 개발 환경에 따라 DB 연결 정보를 조정할 수 있다.

## 8. 정리

이번 작업에서는 ERD로 설계한 DB 구조를 `schema.sql`로 옮기고, IntelliJ에서 MySQL에 직접 실행하여 DB 생성과 초기 데이터 입력을 확인하였다.

이 작업은 수업에서 배운 SQL 파일 실행, 테이블 생성, `SELECT` 조회 확인 과정을 본 프로젝트의 카페 주문 도메인에 적용한 것이다.
