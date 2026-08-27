# DAO/DTO 설계

## 1. DAO/DTO 설계 목적

본 프로젝트는 기존 카페 주문 관리 CRUD를 새로 다시 만드는 것이 아니라, 기존 콘솔 기반 주문 관리 흐름을 JDBC/MySQL 기반으로 전환하는 것을 목표로 한다.

따라서 DAO/DTO 설계도 과하게 복잡한 계층을 추가하기보다, 현재 프로젝트 구조 안에서 DB 접근 역할과 데이터 전달 역할을 명확히 구분하는 방향으로 설계하였다.

## 2. DAO란 무엇인가

DAO는 Data Access Object의 약자이다.

쉽게 말하면 DAO는 DB에 직접 접근하는 객체이다.

DAO는 SQL을 실행하여 데이터를 저장, 조회, 수정, 삭제하는 역할을 담당한다.

예를 들어 주문 데이터를 DB에 저장하거나, 주문번호로 주문을 조회하거나, 고객명과 주문 상태를 수정하는 작업은 DAO의 책임이다.

## 3. 본 프로젝트의 DAO 역할

본 프로젝트에서는 `OrderRepository`가 DAO 역할을 담당한다.

기존 프로젝트에서는 `OrderRepository`가 `ArrayList`를 사용해 주문 데이터를 메모리에 저장했다.

이번 JDBC/MySQL 연동 작업에서는 `OrderRepository`가 JDBC를 사용해 MySQL 테이블에 직접 접근하도록 변경하였다.

즉, 클래스 이름은 Repository이지만 실제 역할은 DAO에 해당한다.

## 4. OrderRepository의 주요 책임

`OrderRepository`는 다음 DB 작업을 담당한다.

| 메소드 | CRUD | 역할 |
| --- | --- | --- |
| `save()` | Create | 주문 정보와 주문 상세 정보를 DB에 저장 |
| `findAll()` | Read | 전체 주문 목록 조회 |
| `findById()` | Read | 주문번호로 주문 한 건 조회 |
| `updateCustomerName()` | Update | 주문 고객명 수정 |
| `updateStatus()` | Update | 주문 상태 수정 |
| `deleteById()` | Delete | 주문 삭제 |

`OrderRepository`는 Service나 View가 직접 SQL을 알 필요가 없도록 DB 접근 코드를 한 곳에 모은다.

이를 통해 화면 흐름, 비즈니스 로직, DB 접근 책임을 분리할 수 있다.

## 5. DTO란 무엇인가

DTO는 Data Transfer Object의 약자이다.

쉽게 말하면 DTO는 데이터를 계층 사이에서 전달하기 위한 객체이다.

DB에서 조회한 데이터를 Java 코드 안에서 사용하려면 여러 값을 하나의 객체로 묶어 전달하는 것이 좋다.

예를 들어 주문번호, 고객명, 주문 상태, 주문 항목 목록을 따로따로 전달하는 것보다 `CafeOrder` 객체 하나로 묶어 전달하면 코드 흐름을 이해하기 쉽다.

## 6. 본 프로젝트의 DTO/Model 역할

본 프로젝트는 콘솔 기반 미니 프로젝트이므로 별도의 Request/Response DTO 클래스를 과하게 추가하지 않았다.

대신 기존 도메인 모델 객체를 DTO/Model 역할로 함께 사용한다.

| Java 클래스 | 역할 | 연결되는 DB 테이블 |
| --- | --- | --- |
| `CafeOrder` | 주문 한 건의 데이터 표현 | `cafe_order` |
| `OrderItem` | 주문에 포함된 메뉴 항목과 수량 표현 | `order_item` |
| `CafeMenu` | 메뉴 정보 표현 | `menu` |

이 객체들은 DB에서 조회한 데이터를 Java 계층 사이에서 전달하는 역할을 한다.

## 7. DB 테이블과 Java 객체 매핑

ERD의 테이블은 Java 객체와 다음처럼 연결된다.

| DB 테이블 | Java 객체 | 설명 |
| --- | --- | --- |
| `menu` | `CafeMenu` | 메뉴명, 카테고리, 가격을 가진 메뉴 데이터 |
| `cafe_order` | `CafeOrder` | 고객명, 주문 상태, 할인 유형, 주문 시각을 가진 주문 데이터 |
| `order_item` | `OrderItem` | 어떤 주문에 어떤 메뉴가 몇 개 포함되었는지 나타내는 주문 상세 데이터 |
| `virtual_thread_log` | 추후 별도 로그 객체 예정 | Java 21 가상 스레드 실행 추적 로그 |

현재 JDBC CRUD 1차 구현 범위에서는 주문 CRUD에 필요한 `CafeOrder`, `OrderItem`, `CafeMenu` 매핑을 우선 적용한다.

`virtual_thread_log`는 이후 `feature/virtual-thread-experiment` 브랜치에서 Java 21 가상 스레드 실험 코드와 함께 확장할 예정이다.

## 8. 데이터 흐름

주문 등록 흐름은 다음과 같다.

```text
사용자 입력
-> CafeOrderApplication
-> CafeOrderController
-> OrderService
-> OrderRepository
-> JDBC
-> MySQL
```

조회 흐름은 반대 방향으로 진행된다.

```text
MySQL
-> JDBC ResultSet
-> OrderRepository
-> CafeOrder / OrderItem / CafeMenu 객체 변환
-> OrderService
-> CafeOrderController
-> 화면 출력
```

즉, `OrderRepository`는 DB의 행 데이터를 Java 객체로 변환하고, Java 객체를 다시 SQL 실행에 필요한 값으로 변환하는 역할을 담당한다.

## 9. DTO를 과하게 분리하지 않은 이유

실무에서는 웹 API 구조에 따라 Request DTO, Response DTO, Entity 등을 더 세분화하기도 한다.

하지만 본 프로젝트는 콘솔 기반 미니 프로젝트이며, 핵심 목표는 JDBC, DAO/DTO, MySQL CRUD, Java 21 가상 스레드 이해이다.

따라서 DTO 클래스를 불필요하게 늘리기보다 기존 모델 객체를 데이터 전달 객체로 함께 활용하여 구조를 단순하게 유지하였다.

이는 Ground Rule의 "이해하지 못한 코드는 그대로 제출하지 않는다"는 원칙과도 연결된다.

설명할 수 없는 복잡한 계층을 추가하기보다, 현재 코드 구조 안에서 DAO/DTO 역할을 명확히 이해하고 설명할 수 있도록 설계하였다.

## 10. 발표용 정리

DAO는 DB 접근을 담당하는 객체이고, DTO는 데이터를 계층 사이에서 전달하기 위한 객체이다.

본 프로젝트에서는 `OrderRepository`가 DAO 역할을 담당하며, JDBC를 사용해 MySQL에 SQL을 실행한다.

DTO는 별도 클래스를 과하게 늘리지 않고, 기존 `CafeOrder`, `OrderItem`, `CafeMenu` 모델 객체를 데이터 전달 객체 역할로 사용하였다.

이를 통해 구조는 단순하게 유지하면서도, DB 테이블과 Java 객체가 어떻게 연결되는지 설명할 수 있도록 설계하였다.
