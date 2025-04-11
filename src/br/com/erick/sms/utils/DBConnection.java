package br.com.erick.sms.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	private String DBUserName;

	private String DBPassword;

	private String DBUrl;

	private Connection conn = null;

	public DBConnection(String usr, String pwk, String DBUrl) {
		this.DBUserName = usr;
		this.DBPassword = pwk;
		this.DBUrl = DBUrl;
		open();
		initializeDatabase();
	}
	
	public Connection getConnection() {
		try {
			if(this.conn.isClosed()) {
				return null;
			}
			return this.conn;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean open() {

		if (this.conn != null) {
			try {
				if (!this.conn.isClosed())
					throw new RuntimeException("A connect is open already");
			} catch (SQLException e) {
				System.err.println("Error: Could not check the database connection");
				e.printStackTrace();
			}
		}

		try {

			this.conn = DriverManager.getConnection(DBUrl, DBUserName, DBPassword);
			return true;

		} catch (SQLException e) {
			System.err.println("Error: Could not connect to database");
			e.printStackTrace();
			return false;
		}
	}

	public boolean close() {
		try {
			if (!this.conn.isClosed())
				this.conn.close();
			return true;
		} catch (SQLException e) {
			System.err.println("Error closing the connection");
		}
		return false;
	}

	private void initializeDatabase() {

		String produtoInitializer = "CREATE TABLE IF NOT EXISTS produto ( " + "id serial primary key,"
				+ " name varchar(100) not null," + " unit_value real not null," + " sales_quantity integer default 0);";

		String compraInitializer = "CREATE TABLE IF NOT EXISTS compra (id serial primary key, date varchar(100) not null, total real);";

		String itemInitializer = "CREATE TABLE IF NOT EXISTS item ( compra_id serial, produto_id serial, quantity integer not null, primary key(compra_id, produto_id), foreign key(compra_id) references compra(id), foreign key(produto_id) references produto(id));";

		try {
			Statement stmt = conn.createStatement();
			stmt.execute(produtoInitializer);
			stmt.execute(compraInitializer);
			stmt.execute(itemInitializer);

			System.out.println("[ INFO ] Schema initializer UP");
		} catch (SQLException e) {
			System.err.println("[ ERR ] Could not initialize the database");
			e.printStackTrace();
		}
	}

	public void resetDatabase() {
		String sql = "DROP TABLE IF EXISTS item;";
		String sq2 = "DROP TABLE IF EXISTS compra;";
		String sq3 = "DROP TABLE IF EXISTS produto;";
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
			stmt.execute(sq2);
			stmt.execute(sq3);
			
			initializeDatabase();
			
			System.out.println("Database restarted");
		} catch (SQLException e) {
			System.err.println("Error: Could not restart the database");
			e.printStackTrace();
		}
	}
}
