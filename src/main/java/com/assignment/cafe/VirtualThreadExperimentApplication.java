package com.assignment.cafe;

import com.assignment.cafe.model.CafeOrder;
import com.assignment.cafe.model.NoDiscountPolicy;
import com.assignment.cafe.model.OrderItem;
import com.assignment.cafe.model.TakeOutDiscountPolicy;
import com.assignment.cafe.model.VirtualThreadLogEntry;
import com.assignment.cafe.repository.OrderRepository;
import com.assignment.cafe.repository.VirtualThreadLogRepository;
import com.assignment.cafe.service.MenuService;
import com.assignment.cafe.service.OrderService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class VirtualThreadExperimentApplication {
    private static final String EXPERIMENT_NAME = "java21-virtual-thread-db-io";

    public static void main(String[] args) {
        MenuService menuService = new MenuService();
        OrderService orderService = new OrderService(new OrderRepository());
        VirtualThreadLogRepository logRepository = new VirtualThreadLogRepository();

        logRepository.deleteByExperimentName(EXPERIMENT_NAME);

        List<Integer> orderIds = createSampleOrders(menuService, orderService);

        ThreadFactory virtualThreadFactory = Thread.ofVirtual()
                .name("vt-order-worker-", 1)
                .factory();

        System.out.println("Java 21 가상 스레드 DB I/O 실험을 시작합니다.");
        System.out.println("생성된 주문 수: " + orderIds.size());

        try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(virtualThreadFactory)) {
            List<Future<?>> futures = new ArrayList<>();

            for (Integer orderId : orderIds) {
                futures.add(executorService.submit(() -> processOrder(orderService, logRepository, orderId)));
            }

            for (Future<?> future : futures) {
                future.get();
            }
        } catch (Exception e) {
            throw new IllegalStateException("가상 스레드 실험 실행 중 오류가 발생했습니다.", e);
        }

        System.out.println("가상 스레드 실험이 종료되었습니다.");
        System.out.println("MySQL에서 다음 SQL로 결과를 확인하세요.");
        System.out.println("SELECT * FROM virtual_thread_log ORDER BY log_id;");
    }

    private static List<Integer> createSampleOrders(MenuService menuService, OrderService orderService) {
        List<Integer> orderIds = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            List<OrderItem> items = List.of(
                    new OrderItem(menuService.findById(i), i),
                    new OrderItem(menuService.findById(8), 1)
            );

            CafeOrder order = orderService.createOrder(
                    "VT고객" + i,
                    items,
                    i % 2 == 0 ? new TakeOutDiscountPolicy() : new NoDiscountPolicy()
            );

            orderIds.add(order.getId());
        }

        return orderIds;
    }

    private static void processOrder(
            OrderService orderService,
            VirtualThreadLogRepository logRepository,
            int orderId
    ) {
        String taskName = "order-task-" + orderId;
        Thread currentThread = Thread.currentThread();
        String threadName = currentThread.toString();
        boolean virtual = currentThread.isVirtual();
        LocalDateTime startedAt = LocalDateTime.now();
        int simulatedWaitingMs = 150 + (orderId * 70);

        try {
            Thread.sleep(30);

            LocalDateTime waitingStartedAt = LocalDateTime.now();
            CafeOrder order = orderService.findById(orderId);
            Thread.sleep(simulatedWaitingMs);

            LocalDateTime waitingEndedAt = LocalDateTime.now();
            Thread.sleep(20);
            LocalDateTime finishedAt = LocalDateTime.now();

            long waitingTimeMs = Duration.between(waitingStartedAt, waitingEndedAt).toMillis();
            long totalTimeMs = Duration.between(startedAt, finishedAt).toMillis();
            long processingTimeMs = totalTimeMs - waitingTimeMs;

            logRepository.save(new VirtualThreadLogEntry(
                    orderId,
                    EXPERIMENT_NAME,
                    taskName,
                    threadName,
                    "VIRTUAL",
                    startedAt,
                    waitingStartedAt,
                    waitingEndedAt,
                    finishedAt,
                    waitingTimeMs,
                    totalTimeMs,
                    "SUCCESS",
                    "주문 조회와 I/O 대기 흐름 확인: " + order.getCustomerName()
                            + ", isVirtual=" + virtual
            ));

            System.out.printf(
                    "[%s] %s | isVirtual=%s | orderId=%d | waiting=%dms | processing=%dms | total=%dms%n",
                    currentThread,
                    taskName,
                    virtual,
                    orderId,
                    waitingTimeMs,
                    processingTimeMs,
                    totalTimeMs
            );
        } catch (Exception e) {
            LocalDateTime failedAt = LocalDateTime.now();
            long totalTimeMs = Duration.between(startedAt, failedAt).toMillis();

            logRepository.save(new VirtualThreadLogEntry(
                    orderId,
                    EXPERIMENT_NAME,
                    taskName,
                    threadName,
                    "VIRTUAL",
                    startedAt,
                    startedAt,
                    failedAt,
                    failedAt,
                    totalTimeMs,
                    totalTimeMs,
                    "FAIL",
                    e.getMessage() + ", isVirtual=" + virtual
            ));

            System.out.printf("[%s] %s 실패: %s%n", currentThread, taskName, e.getMessage());
        }
    }
}
