package repository;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetProcessor<T> {
    void process(ResultSet resultSet) throws SQLException;

    T getResult();
}