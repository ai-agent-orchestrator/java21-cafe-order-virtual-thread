package com.assignment.cafe.repository;

import com.assignment.cafe.db.ConnectionManager;
import com.assignment.cafe.model.CafeMenu;
import com.assignment.cafe.model.CafeOrder;
import com.assignment.cafe.model.DiscountPolicy;
import com.assignment.cafe.model.MenuCategory;
import com.assignment.cafe.model.NoDiscountPolicy;
import com.assignment.cafe.model.OrderItem;
import com.assignment.cafe.model.OrderStatus;
import com.assignment.cafe.model.TakeOutDiscountPolicy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/*
 * 주문 데이터를 보관하는 저장소 클래스입니다.
 * 화면 출력이나 메뉴 선택 판단은 하지 않고, 오직 저장/조회/삭제만 담당합니다.
 *
 * 요구사항 4번(chap12 CRUD)
 * - save(): 등록(Create)
 * - findAll(), findById(): 조회(Read)
 * - deleteById(): 삭제(Delete)
 * - updateCustomerName(), updateStatus(): 수정(Update)
 *
 * 이번 프로젝트에서는 기존 ArrayList 저장 방식을 JDBC/MySQL 저장 방식으로 바꿉니다.
 */
public class OrderRepository {
    public CafeOrder save(CafeOrder order) {
        String orderSql = """
                INSERT INTO cafe_order (order_id, customer_name, status, discount_type, ordered_at)
                VALUES (?, ?, ?, ?, ?)
                """;
        String itemSql = """
                INSERT INTO order_item (order_id, menu_id, quantity)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = ConnectionManager.getConnection()) {
            connection.setAutoCommit(false);

            try (
                    PreparedStatement orderStatement = connection.prepareStatement(orderSql);
                    PreparedStatement itemStatement = connection.prepareStatement(itemSql)
            ) {
                orderStatement.setInt(1, order.getId());
                orderStatement.setString(2, order.getCustomerName());
                orderStatement.setString(3, order.getStatus().name());
                orderStatement.setString(4, toDiscountType(order.getDiscountPolicy()));
                orderStatement.setTimestamp(5, Timestamp.valueOf(order.getOrderedAt()));
                orderStatement.executeUpdate();

                for (OrderItem item : order.getItems()) {
                    itemStatement.setInt(1, order.getId());
                    itemStatement.setInt(2, item.getMenu().getId());
                    itemStatement.setInt(3, item.getQuantity());
                    itemStatement.addBatch();
                }

                itemStatement.executeBatch();
                connection.commit();
                return order;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("주문 저장 중 데이터베이스 오류가 발생했습니다.", e);
        }
    }

    public int nextOrderId() {
        String sql = "SELECT COALESCE(MAX(order_id), 0) + 1 AS next_order_id FROM cafe_order";

        try (
                Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                return resultSet.getInt("next_order_id");
            }

            return 1;
        } catch (SQLException e) {
            throw new IllegalStateException("다음 주문번호 조회 중 데이터베이스 오류가 발생했습니다.", e);
        }
    }

    public List<CafeOrder> findAll() {
        String sql = """
                SELECT order_id, customer_name, status, discount_type, ordered_at
                FROM cafe_order
                ORDER BY order_id
                """;
        List<CafeOrder> orders = new ArrayList<>();

        try (
                Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                orders.add(mapOrder(resultSet, findItemsByOrderId(connection, resultSet.getInt("order_id"))));
            }

            return orders;
        } catch (SQLException e) {
            throw new IllegalStateException("전체 주문 조회 중 데이터베이스 오류가 발생했습니다.", e);
        }
    }

    public CafeOrder findById(int id) {
        String sql = """
                SELECT order_id, customer_name, status, discount_type, ordered_at
                FROM cafe_order
                WHERE order_id = ?
                """;

        try (
                Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapOrder(resultSet, findItemsByOrderId(connection, id));
                }

                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("주문 단건 조회 중 데이터베이스 오류가 발생했습니다.", e);
        }
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM cafe_order WHERE order_id = ?";

        try (
                Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("주문 삭제 중 데이터베이스 오류가 발생했습니다.", e);
        }
    }

    public boolean updateCustomerName(int id, String customerName) {
        String sql = "UPDATE cafe_order SET customer_name = ? WHERE order_id = ?";

        try (
                Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, customerName);
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("고객명 수정 중 데이터베이스 오류가 발생했습니다.", e);
        }
    }

    public boolean updateStatus(int id, OrderStatus status) {
        String sql = "UPDATE cafe_order SET status = ? WHERE order_id = ?";

        try (
                Connection connection = ConnectionManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, status.name());
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("주문 상태 수정 중 데이터베이스 오류가 발생했습니다.", e);
        }
    }

    private List<OrderItem> findItemsByOrderId(Connection connection, int orderId) throws SQLException {
        String sql = """
                SELECT m.menu_id, m.name, m.category, m.price, oi.quantity
                FROM order_item oi
                JOIN menu m ON oi.menu_id = m.menu_id
                WHERE oi.order_id = ?
                ORDER BY oi.order_item_id
                """;
        List<OrderItem> items = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    CafeMenu menu = new CafeMenu(
                            resultSet.getInt("menu_id"),
                            resultSet.getString("name"),
                            MenuCategory.valueOf(resultSet.getString("category")),
                            resultSet.getInt("price")
                    );
                    items.add(new OrderItem(menu, resultSet.getInt("quantity")));
                }
            }
        }

        return items;
    }

    private CafeOrder mapOrder(ResultSet resultSet, List<OrderItem> items) throws SQLException {
        return new CafeOrder(
                resultSet.getInt("order_id"),
                resultSet.getString("customer_name"),
                items,
                toDiscountPolicy(resultSet.getString("discount_type")),
                OrderStatus.valueOf(resultSet.getString("status")),
                resultSet.getObject("ordered_at", LocalDateTime.class)
        );
    }

    private String toDiscountType(DiscountPolicy discountPolicy) {
        if (discountPolicy instanceof TakeOutDiscountPolicy) {
            return "TAKE_OUT";
        }

        return "NONE";
    }

    private DiscountPolicy toDiscountPolicy(String discountType) {
        if ("TAKE_OUT".equals(discountType)) {
            return new TakeOutDiscountPolicy();
        }

        return new NoDiscountPolicy();
    }
}
