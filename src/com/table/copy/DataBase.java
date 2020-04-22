package com.table.copy;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import oracle.jdbc.pool.OracleDataSource;

class DataBase  {
	private Connection conn;
	private DBUtil dbutil;

	DataBase() throws SQLException {
		OracleDataSource dataSource = new OracleDataSource();
		Map map = null;
		try {
			map = dbutil.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dataSource.setUser((String) map.get("USER"));
		dataSource.setPassword((String) map.get("PASSWORD"));
		dataSource.setURL((String) map.get("URL"));
		conn = dataSource.getConnection();
		conn.setAutoCommit(false);
		DatabaseMetaData metaData = conn.getMetaData();
	}

	ArrayList<String> selectFromTestBase()
			throws SQLException {
		ArrayList<String> clientList = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		String query = "SELECT T_NAME FROM TEST_1";
		Savepoint sp = conn.setSavepoint();
		try (Statement statement = conn.createStatement()) {
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				String table = resultSet.getString(1);
				sb.append("INSERT /*+APPEND*/  INTO ");
				sb.append(table);
				sb.append(" SELECT * FROM TEST_1");
				clientList.add(sb.toString());
				sb.setLength(0);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			conn.rollback(sp);
		} finally {
			conn.commit();
		}
		return clientList;
	}

	boolean executeSql(String sql) throws SQLException {
		boolean b = false;
		Savepoint sp = conn.setSavepoint();
		try (Statement statement = conn.createStatement()) {
			b = statement.execute(sql);
		} catch (SQLException ex) {
//			ex.printStackTrace();
			conn.rollback(sp);
			throw ex;
		} finally {
			conn.commit();
		}
		return b;
	}

}
