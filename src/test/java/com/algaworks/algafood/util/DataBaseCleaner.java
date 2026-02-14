package com.algaworks.algafood.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataBaseCleaner {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DataSource dataSource;
    private Connection connection;

    public DataBaseCleaner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void clearTables() {
        try (var connection = dataSource.getConnection()) {
            this.connection = connection;
            checkTestDatabase();
            tryToClearTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            this.connection = null;
        }
    }

    private void checkTestDatabase() throws SQLException {
        var catalog = connection.getCatalog();
        if (catalog == null || !catalog.endsWith("test")) {
            throw new RuntimeException("Cannot clear database tables because '" + catalog
                    + "' is not a test database (suffix 'test' not found).");
        }
    }

    private void tryToClearTables() throws SQLException {
        List<String> tableNames = getTableNames();
        clear(tableNames);
    }

    private List<String> getTableNames() throws SQLException {
        List<String> tableNames = new ArrayList<>();
        var metaData = connection.getMetaData();
        var rs = metaData.getTables(connection.getCatalog(), null, null, new String[]{"TABLE"});
        while (rs.next()) {
            tableNames.add(rs.getString("TABLE_NAME"));
        }
        tableNames.remove("flyway_schema_history");
        return tableNames;
    }

    private void clear(List<String> tableNames) throws SQLException {
        var statement = buildSqlStatement(tableNames);
        logger.debug("Executing SQL");
        statement.executeBatch();
    }

    private Statement buildSqlStatement(List<String> tableNames) throws SQLException {
        var statement = connection.createStatement();
        statement.addBatch(sql("SET FOREIGN_KEY_CHECKS = 0"));
        addTruncateSatements(tableNames, statement);
        statement.addBatch(sql("SET FOREIGN_KEY_CHECKS = 1"));
        return statement;
    }

    private void addTruncateSatements(List<String> tableNames, Statement statement) {
        tableNames.forEach(tableName -> {
            try {
                statement.addBatch(sql("TRUNCATE TABLE " + tableName));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String sql(String sql) {
        logger.debug("Adding SQL: {}", sql);
        return sql;
    }
}
