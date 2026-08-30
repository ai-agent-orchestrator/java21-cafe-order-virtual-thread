package com.assignment.cafe.repository;

import com.assignment.cafe.db.ConnectionManager;
import com.assignment.cafe.model.VirtualThreadLogEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class VirtualThreadLogRepository {
    public void save(VirtualThreadLogEntry log) {
        String sql = """
                INSERT INTO virtual_thread_log (
                    order_id,
                    experiment_name,
                    task_name,
                    thread_name,
                    thread_type,
                    started_at,
                    waiting_started_at,
                    waiting_ended_at,
                    finished_at,
                    waiting_time_ms,
                    total_time_ms,
                    result_status,
                    message
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            if (log.getOrderId() == null) {
                statement.setNull(1, java.sql.Types.INTEGER);
            } else {
                statement.setInt(1, log.getOrderId());
            }

            statement.setString(2, log.getExperimentName());
            statement.setString(3, log.getTaskName());
            statement.setString(4, log.getThreadName());
            statement.setString(5, log.getThreadType());
            statement.setTimestamp(6, Timestamp.valueOf(log.getStartedAt()));
            statement.setTimestamp(7, Timestamp.valueOf(log.getWaitingStartedAt()));
            statement.setTimestamp(8, Timestamp.valueOf(log.getWaitingEndedAt()));
            statement.setTimestamp(9, Timestamp.valueOf(log.getFinishedAt()));
            statement.setLong(10, log.getWaitingTimeMs());
            statement.setLong(11, log.getTotalTimeMs());
            statement.setString(12, log.getResultStatus());
            statement.setString(13, log.getMessage());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("가상 스레드 실행 로그 저장 중 데이터베이스 오류가 발생했습니다.", e);
        }
    }

    public void deleteByExperimentName(String experimentName) {
        String sql = "DELETE FROM virtual_thread_log WHERE experiment_name = ?";

        try (
                Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, experimentName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("가상 스레드 실행 로그 삭제 중 데이터베이스 오류가 발생했습니다.", e);
        }
    }
}
