package com.algaworks.algafood.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private DataSource dataSource;
	private Connection connection;

	public void clearTables() {
		try (Connection connection = this.dataSource.getConnection()) {
			this.connection = connection;
			this.checkTestDatabase();
			this.tryToClearTables();
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		} finally {
			this.connection = null;
		}
	}

	private void checkTestDatabase() throws SQLException {
		String catalog = this.connection.getCatalog();
		if (catalog == null || !catalog.endsWith("test")) {
			throw new RuntimeException("Cannot clear database tables because '" + catalog
					+ "' is not a test database (suffix 'test' not found).");
		}
	}

	private void tryToClearTables() throws SQLException {
		List<String> tableNames = this.getTableNames();
		this.clear(tableNames);
	}

	private List<String> getTableNames() throws SQLException {
		List<String> tableNames = new ArrayList<>();
		DatabaseMetaData data = this.connection.getMetaData();
		ResultSet resultSet = data.getTables(this.connection.getCatalog(), null, null, new String[] { "TABLE" });
		while (resultSet.next()) {
			tableNames.add(resultSet.getString("TABLE_NAME"));
		}
		tableNames.remove("flyway_schema_history");
		return tableNames;
	}

	private void clear(List<String> tableNames) throws SQLException {
		Statement statement = this.buildSqlStatement(tableNames);
		this.logger.debug("Executing SQL");
		statement.executeBatch();
	}

	private Statement buildSqlStatement(List<String> tableNames) throws SQLException {
		Statement statement = this.connection.createStatement();
		statement.addBatch(this.sql("SET FOREIGN_KEY_CHECKS = 0"));
		this.addTruncateSatements(tableNames, statement);
		statement.addBatch(this.sql("SET FOREIGN_KEY_CHECKS = 1"));
		return statement;
	}

	private void addTruncateSatements(List<String> tableNames, Statement statement) {
		tableNames.forEach(tableName -> {
			try {
				statement.addBatch(this.sql("TRUNCATE TABLE " + tableName));
			} catch (SQLException e) {
				// TODO: handle exception
				throw new RuntimeException(e);
			}
		});
	}

	private String sql(String sql) {
		this.logger.debug("Adding SQL: {}", sql);
		return sql;
	}
}
