package com.assignment.cafe.model;

import java.time.LocalDateTime;

public class VirtualThreadLogEntry {
    private final Integer orderId;
    private final String experimentName;
    private final String taskName;
    private final String threadName;
    private final String threadType;
    private final LocalDateTime startedAt;
    private final LocalDateTime waitingStartedAt;
    private final LocalDateTime waitingEndedAt;
    private final LocalDateTime finishedAt;
    private final long waitingTimeMs;
    private final long totalTimeMs;
    private final String resultStatus;
    private final String message;

    public VirtualThreadLogEntry(
            Integer orderId,
            String experimentName,
            String taskName,
            String threadName,
            String threadType,
            LocalDateTime startedAt,
            LocalDateTime waitingStartedAt,
            LocalDateTime waitingEndedAt,
            LocalDateTime finishedAt,
            long waitingTimeMs,
            long totalTimeMs,
            String resultStatus,
            String message
    ) {
        this.orderId = orderId;
        this.experimentName = experimentName;
        this.taskName = taskName;
        this.threadName = threadName;
        this.threadType = threadType;
        this.startedAt = startedAt;
        this.waitingStartedAt = waitingStartedAt;
        this.waitingEndedAt = waitingEndedAt;
        this.finishedAt = finishedAt;
        this.waitingTimeMs = waitingTimeMs;
        this.totalTimeMs = totalTimeMs;
        this.resultStatus = resultStatus;
        this.message = message;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getThreadName() {
        return threadName;
    }

    public String getThreadType() {
        return threadType;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getWaitingStartedAt() {
        return waitingStartedAt;
    }

    public LocalDateTime getWaitingEndedAt() {
        return waitingEndedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public long getWaitingTimeMs() {
        return waitingTimeMs;
    }

    public long getTotalTimeMs() {
        return totalTimeMs;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public String getMessage() {
        return message;
    }
}
