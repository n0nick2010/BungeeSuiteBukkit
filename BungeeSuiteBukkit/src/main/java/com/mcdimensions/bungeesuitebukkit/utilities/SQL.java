package com.mcdimensions.bungeesuitebukkit.utilities;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL{
	private static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
	
	private String host, database, username, password, port;
	private Connection connection;
	
	public SQL(String host, String database, String port, String username, String password) {
		this.host = host;
		this.database = database;
		this.username = username;
		this.password = password;
		this.port = port;
	}
	public void closeConnection() throws SQLException {
			this.connection.close();
	}
	
	public void refreshConnection() {
		if (connection == null) {
			initialise();
		}
	}
	
	public boolean initialise() {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + host +":" + port + "/" + database, username, password);
				return true;
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		
		return false;
	}
	
	public void standardQuery(String query) throws SQLException {
		this.refreshConnection();
		standardQuery(query, this.connection);
	}
	public boolean existenceQuery(String query) throws SQLException {
		this.refreshConnection();
		return sqlQuery(query, this.connection).next();
	}
	public ResultSet sqlQuery(String query) throws SQLException {
		this.refreshConnection();
		return sqlQuery(query, this.connection);
	}
	public boolean doesTableExist(String table) throws SQLException {
		this.refreshConnection();
		return checkTable(table, this.connection);
	}
	
	
	protected synchronized int standardQuery(String query, Connection connection) throws SQLException{
		Statement statement = connection.createStatement();
		int rowsUpdated = statement.executeUpdate(query);
		statement.close();
		return rowsUpdated;
	}

	protected synchronized ResultSet sqlQuery(String query, Connection connection) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(query);
		return result;
	}

	protected synchronized boolean checkTable(String table, Connection connection) throws SQLException {
		DatabaseMetaData dbm;
		dbm = connection.getMetaData();
		ResultSet tables = dbm.getTables(null, null, table, null);
		return tables.next();
	}
}
