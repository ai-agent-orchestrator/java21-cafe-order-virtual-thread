# ERD FK 설계 논리

## FK 설계를 따로 검토한 이유

ERD에서 PK는 각 테이블의 데이터를 구분하기 위한 고유 식별자이다.

하지만 ERD 설계에서 더 중요한 부분은 FK이다.

FK는 단순히 테이블을 연결하는 선이 아니라, 데이터가 어떤 업무 흐름 안에서 서로 연결되는지를 보여주는 설계 논리이다.

따라서 본 프로젝트에서는 ERD를 작성할 때 각 FK가 왜 필요한지, 어떤 흐름을 표현하는지 따로 검토했다.

## 최종 FK 설계 요약

| FK | 참조 대상 | 연결 이유 |
| --- | --- | --- |
| `ORDER_ITEM.order_id` | `CAFE_ORDER.order_id` | 주문 상세가 어떤 주문에 속하는지 알기 위해 |
| `ORDER_ITEM.menu_id` | `MENU.menu_id` | 주문 상세가 어떤 메뉴를 주문했는지 알기 위해 |
| `VIRTUAL_THREAD_LOG.order_id` | `CAFE_ORDER.order_id` | 가상 스레드 실행 로그가 어떤 주문 처리 작업과 관련되는지 알기 위해 |

## 1. ORDER_ITEM.order_id -> CAFE_ORDER.order_id

`ORDER_ITEM`은 주문 상세 테이블이다.

주문 상세는 독립적으로 존재할 수 없다.

예를 들어 다음과 같은 데이터만 있으면 의미가 부족하다.

```text
아메리카노 2잔
치즈케이크 1개
```

이 정보만으로는 다음 내용을 알 수 없다.

```text
이 메뉴들이 어떤 주문에 포함된 것인지
누구의 주문인지
주문 상태가 무엇인지
언제 생성된 주문인지
```

따라서 `ORDER_ITEM`은 반드시 하나의 `CAFE_ORDER`에 속해야 한다.

```text
CAFE_ORDER = 주문서
ORDER_ITEM = 주문서 안의 상세 줄
```

그래서 `ORDER_ITEM.order_id`를 `CAFE_ORDER.order_id`와 FK로 연결했다.

## 2. ORDER_ITEM.menu_id -> MENU.menu_id

`ORDER_ITEM`은 어떤 메뉴가 주문되었는지 알아야 한다.

하지만 주문 상세에 메뉴명과 가격을 매번 직접 저장하면 중복이 발생한다.

예를 들어 아메리카노가 여러 번 주문될 경우 다음과 같은 정보가 반복된다.

```text
아메리카노 / 3000
아메리카노 / 3000
아메리카노 / 3000
```

이런 중복을 줄이기 위해 메뉴 기준 정보는 `MENU` 테이블에 한 번만 저장한다.

```text
MENU = 메뉴 기준표
ORDER_ITEM = 어떤 메뉴를 몇 개 주문했는지 기록
```

따라서 `ORDER_ITEM`은 `menu_id`를 통해 `MENU`를 참조한다.

그래서 `ORDER_ITEM.menu_id`를 `MENU.menu_id`와 FK로 연결했다.

## 3. CAFE_ORDER와 MENU를 직접 연결하지 않은 이유

`CAFE_ORDER`와 `MENU`는 직접 연결하지 않았다.

이유는 주문과 메뉴가 다대다 관계이기 때문이다.

```text
한 주문에는 여러 메뉴가 들어갈 수 있다.
하나의 메뉴는 여러 주문에서 반복적으로 주문될 수 있다.
```

예를 들어:

```text
1번 주문: 아메리카노, 치즈케이크
2번 주문: 아메리카노, 자몽에이드
```

이 경우 아메리카노는 여러 주문에서 사용되고, 하나의 주문에도 여러 메뉴가 포함된다.

따라서 `CAFE_ORDER`와 `MENU`를 직접 연결하지 않고, 중간 테이블인 `ORDER_ITEM`을 두어 관계를 풀었다.

```text
CAFE_ORDER 1 : N ORDER_ITEM
MENU 1 : N ORDER_ITEM
```

즉, `ORDER_ITEM`은 주문과 메뉴를 연결하는 주문 상세 엔티티이다.

## 4. VIRTUAL_THREAD_LOG.order_id -> CAFE_ORDER.order_id

`VIRTUAL_THREAD_LOG`는 본 프로젝트의 핵심 목표인 Java 21 가상 스레드 실험을 기록하기 위한 실행 추적 로그 엔티티이다.

이 테이블은 단순히 스레드 이름만 저장하는 로그가 아니다.

가상 스레드가 I/O 대기 구간을 어떻게 지나갔는지 확인하기 위한 로그이다.

따라서 로그는 다음 질문에 답할 수 있어야 한다.

```text
이 로그는 어떤 주문 처리 작업에서 발생했는가?
어떤 주문을 처리하다가 DB I/O 대기 구간이 발생했는가?
```

그래서 `VIRTUAL_THREAD_LOG`는 `CAFE_ORDER`와 연결된다.

```text
CAFE_ORDER = 실제 주문 데이터
VIRTUAL_THREAD_LOG = 그 주문을 처리한 가상 스레드 실행 흐름
```

따라서 `VIRTUAL_THREAD_LOG.order_id`를 `CAFE_ORDER.order_id`와 FK로 연결했다.

## 5. VIRTUAL_THREAD_LOG를 MENU와 직접 연결하지 않은 이유

가상 스레드 실험의 단위는 메뉴 하나가 아니라 주문 처리 작업이다.

예를 들어 가상 스레드가 실행하는 작업은 다음과 같다.

```text
1번 주문 등록
2번 주문 조회
3번 주문 상태 수정
```

즉, 실험의 기준은 개별 메뉴가 아니라 주문이다.

따라서 `VIRTUAL_THREAD_LOG`는 `MENU`와 직접 연결하지 않고 `CAFE_ORDER`와 연결한다.

필요하다면 다음 관계를 통해 주문에 포함된 메뉴 정보까지 확인할 수 있다.

```text
VIRTUAL_THREAD_LOG
-> CAFE_ORDER
-> ORDER_ITEM
-> MENU
```

이 구조를 통해 가상 스레드 실행 로그에서 시작해, 해당 로그가 어떤 주문과 연결되어 있고, 그 주문에 어떤 메뉴가 포함되었는지도 추적할 수 있다.

## 최종 정리

본 프로젝트의 FK 설계는 단순히 테이블을 연결하기 위한 것이 아니다.

`ORDER_ITEM`의 FK는 주문과 메뉴의 관계를 표현하기 위한 연결이다.

`VIRTUAL_THREAD_LOG`의 FK는 Java 21 가상 스레드 실험 로그와 실제 주문 처리 작업을 연결하기 위한 연결이다.

즉, 이 ERD의 FK는 다음 두 흐름을 함께 설명한다.

```text
1. 카페 주문 데이터 흐름
2. Java 21 가상 스레드 실행 추적 흐름
```

## 발표용 핵심 문장

```text
PK는 각 테이블의 데이터를 구분하기 위한 번호이고,
FK는 이 데이터가 어떤 업무 흐름 안에서 연결되는지를 보여주는 설계 논리입니다.

본 프로젝트에서는 ORDER_ITEM을 통해 주문과 메뉴의 관계를 풀었고,
VIRTUAL_THREAD_LOG를 통해 Java 21 가상 스레드 실험 로그와 실제 주문 처리 작업을 연결했습니다.
```
