package repository.Repository;

import repository.ResultSetProcessor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface Repository {
    default void executeQuery(String sql, ResultSetProcessor<?> processor, Connection connection) {
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                processor.process(resultSet);
            } catch (SQLException e) {
                System.err.println("Fetching result set failed: " + e.getMessage());
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        System.err.println("Closing statement failed: " + e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Create statement failed: " + e.getMessage());
        }
    }

}
