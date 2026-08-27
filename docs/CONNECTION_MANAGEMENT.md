# Connection 객체 관리 및 자원 반환

## 1. 문서 목적

이 문서는 JDBC 기반 DB 연동에서 Connection 객체를 어떻게 관리하고, 사용한 자원을 어떻게 반환하는지 설명하기 위해 작성하였다.

본 프로젝트는 복잡한 Connection Pool 구현보다 JDBC의 기본 연결 흐름과 자원 반환 구조를 이해하는 데 초점을 둔다.

## 2. JDBC에서 사용하는 주요 객체

JDBC CRUD 구현에서는 다음 객체들이 사용된다.

| 객체 | 역할 |
| --- | --- |
| `Connection` | Java 애플리케이션과 DB 사이의 연결 |
| `PreparedStatement` | SQL을 안전하게 실행하기 위한 객체 |
| `ResultSet` | `SELECT` 실행 결과를 담는 객체 |

이 객체들은 DB 연결 자원을 사용하므로, 작업이 끝나면 반드시 반환되어야 한다.

## 3. Connection 객체란 무엇인가

`Connection`은 Java 코드와 MySQL 사이의 연결 통로이다.

Java에서 DB에 SQL을 보내려면 먼저 DB와 연결되어야 한다.

본 프로젝트에서는 `ConnectionManager`가 `DriverManager.getConnection()`을 사용해 `Connection` 객체를 생성한다.

```text
Java Application
-> ConnectionManager
-> DriverManager.getConnection()
-> MySQL
```

## 4. ConnectionManager의 역할

`ConnectionManager`는 DB 연결 정보를 한 곳에서 관리한다.

주요 역할은 다음과 같다.

| 역할 | 설명 |
| --- | --- |
| DB URL 관리 | `cafe_order_db` 연결 주소 관리 |
| DB 계정 관리 | 사용자명과 비밀번호 설정 |
| Connection 생성 | JDBC 연결 객체 생성 |
| 설정 확장 | 환경 변수 또는 JVM 옵션으로 DB 정보 변경 가능 |

이를 통해 Repository가 DB 연결 세부 정보를 직접 관리하지 않도록 분리하였다.

## 5. try-with-resources를 사용한 자원 반환

JDBC 객체는 사용 후 반드시 닫아야 한다.

직접 `close()`를 호출할 수도 있지만, 실수로 닫지 않으면 DB 연결 자원이 낭비될 수 있다.

그래서 본 프로젝트에서는 `try-with-resources` 문법을 사용한다.

```text
try-with-resources
= try 블록이 끝나면 사용한 자원을 자동으로 닫아주는 Java 문법
```

예를 들어 `Connection`, `PreparedStatement`, `ResultSet`을 try-with-resources 안에서 생성하면 작업이 끝난 뒤 자동으로 반환된다.

## 6. 적용 위치

본 프로젝트에서는 `OrderRepository`에서 JDBC 자원 반환 구조를 적용하였다.

| 메소드 | 사용 자원 |
| --- | --- |
| `save()` | `Connection`, `PreparedStatement` |
| `nextOrderId()` | `Connection`, `PreparedStatement`, `ResultSet` |
| `findAll()` | `Connection`, `PreparedStatement`, `ResultSet` |
| `findById()` | `Connection`, `PreparedStatement`, `ResultSet` |
| `updateCustomerName()` | `Connection`, `PreparedStatement` |
| `updateStatus()` | `Connection`, `PreparedStatement` |
| `deleteById()` | `Connection`, `PreparedStatement` |

이를 통해 DB 작업 후 연결 객체와 SQL 실행 객체가 자동으로 반환되도록 구성하였다.

## 7. 주문 저장과 트랜잭션

주문 저장은 한 테이블만 사용하는 작업이 아니다.

하나의 주문을 저장할 때는 다음 두 작업이 함께 일어난다.

```text
1. cafe_order 테이블에 주문 기본 정보 저장
2. order_item 테이블에 주문 상세 정보 저장
```

이 두 작업 중 하나만 성공하고 하나가 실패하면 데이터가 어긋날 수 있다.

예를 들어 주문 기본 정보는 저장되었는데 주문 상세가 저장되지 않으면 주문 내용이 없는 주문이 생긴다.

그래서 `OrderRepository.save()`에서는 트랜잭션을 사용한다.

```text
connection.setAutoCommit(false)
-> 주문 기본 정보 INSERT
-> 주문 상세 정보 INSERT
-> 둘 다 성공하면 commit
-> 중간에 실패하면 rollback
```

이를 통해 주문과 주문 상세가 함께 저장되거나, 함께 취소되도록 구성하였다.

## 8. Connection Pool 적용 범위

과제 요구사항에는 Connection 객체 관리와 Connection Pool 개념이 포함되어 있다.

다만 본 프로젝트는 Java 21, JDBC, DAO/DTO, MySQL CRUD의 기본 흐름을 이해하는 것이 우선 목표이다.

따라서 이번 구현에서는 HikariCP 같은 외부 Connection Pool 라이브러리를 직접 적용하지 않고, 다음 범위까지 구현한다.

| 구분 | 적용 여부 |
| --- | --- |
| JDBC Connection 생성 | 적용 |
| PreparedStatement 사용 | 적용 |
| ResultSet 사용 | 적용 |
| try-with-resources 자원 반환 | 적용 |
| 트랜잭션 처리 | 적용 |
| HikariCP Connection Pool | 추후 확장 가능 항목으로 정리 |

## 9. Connection Pool을 바로 적용하지 않은 이유

Connection Pool은 DB 연결 객체를 미리 만들어두고 재사용하는 구조이다.

실무에서는 성능과 안정성을 위해 자주 사용된다.

하지만 이번 프로젝트는 콘솔 기반 미니 프로젝트이며, 목표는 복잡한 라이브러리 적용보다 JDBC 연결 흐름을 직접 이해하는 것이다.

따라서 먼저 기본 JDBC 연결과 자원 반환 구조를 구현하고, 이후 Spring Boot 또는 더 큰 백엔드 프로젝트에서 HikariCP 같은 Connection Pool을 적용하는 방향으로 확장할 수 있다.

## 10. 발표용 정리

Connection은 Java와 MySQL 사이의 연결 객체이다.

본 프로젝트에서는 `ConnectionManager`가 Connection 생성을 담당하고, `OrderRepository`가 try-with-resources를 사용해 Connection, PreparedStatement, ResultSet을 자동 반환하도록 구현하였다.

또한 주문 저장 시 `cafe_order`와 `order_item`이 함께 저장되어야 하므로 트랜잭션을 적용하였다.

Connection Pool은 이번 범위에서는 직접 구현하지 않고, JDBC 기본 연결과 자원 반환 구조를 먼저 이해하는 방향으로 정리하였다.
