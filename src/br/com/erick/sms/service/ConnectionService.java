package br.com.erick.sms.service;

import br.com.erick.sms.model.Produto;
import br.com.erick.sms.model.Compra;
import br.com.erick.sms.model.Item;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class ConnectionService {

	private String DBUserName;

	private String DBPassword;

	private String DBUrl;

	private Connection conn = null;

	public ConnectionService(String usr, String pwk, String DBUrl) {
		this.DBUserName = usr;
		this.DBPassword = pwk;
		this.DBUrl = DBUrl;
	}

	public Connection open() {

		try {
			if (this.conn != null) {
				if (!this.conn.isClosed())
					System.out.println("Redundant open");
			}
		} catch (SQLException e) {
			System.err.println("Erro checking the connection");
		}

		try {

			this.conn = DriverManager.getConnection(DBUrl, DBUserName, DBPassword);

		} catch (SQLException e) {
			System.err.println("Error: Could not connect to database");
			e.printStackTrace();
		}

		return this.conn;
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

	public boolean testConnection() {
		Connection openTest = open();
		boolean closeTest = close();
		if (openTest != null && closeTest)
			return true;
		return false;
	}

	public void initializeDatabase() {

		open();

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
		} finally {
			close();
		}
	}

	public void restartDatabase() {
		open();

		String sql = "DROP TABLE IF EXISTS item, compra, produto";
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.err.println("Error: Could not restart the database");
		}

		close();

		initializeDatabase();

		System.out.println("Database restarted");
	}

	//---------------------------- PRODUCT
	
	public void addNewProduct(Produto p) {
		String sql = "INSERT INTO produto (name, unit_value) values (?, ?);";

		open();

		try {
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			stmt.setDouble(2, p.getUnitValue());
			stmt.setString(1, p.getName());
			stmt.execute();
		} catch (SQLException e) {
			System.err.println("Could not add the new product");
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public Produto selectProdutoById(long id) {
		String sql = "SELECT * FROM produto WHERE produto.id = ?";
		
		open();
		
		try {
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
			
			String name = rs.getString("name");
			double unitValue = rs.getDouble("unit_value");
			int salesQt = rs.getInt("sales_quantity");
			
			return new Produto(name, unitValue, salesQt, id);
			
		}catch(SQLException e) {
			System.err.println("Could select the product");
			e.printStackTrace();
			return null;
		}finally {
			close();
		}
	}
	
	public void updateQuantity(Produto p) {
		String sql1 = "SELECT SUM(item.quantity) AS soma FROM item WHERE item.produto_id = ?;";
		String sql2 = "UPDATE produto SET sales_quantity = ? WHERE produto.id = ?";
		
		open();
		
		try {
			PreparedStatement stmt = this.conn.prepareStatement(sql1);
			stmt.setLong(1, p.getId());
			ResultSet sum = stmt.executeQuery();
			
			sum.next();
			
			PreparedStatement stmt2 = this.conn.prepareStatement(sql2);
			stmt2.setInt(1, sum.getInt("soma"));
			stmt2.setLong(2, p.getId());
			stmt2.execute();
		} catch (Exception e) {
			System.err.println("Could not update the item qt");
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public List<Produto> getProducts() {
		String sql = "SELECT * FROM produto;";
		
		List<Produto> list = new ArrayList<>();
		
		ResultSet prods = null;
		
		open();
		
		try {
			Statement stmt = this.conn.createStatement();
			prods = stmt.executeQuery(sql);
			while (prods.next()) {
				list.add(new Produto(prods.getString("name"), prods.getDouble("unit_value"),
						prods.getInt("sales_quantity"), prods.getLong("id")));
			}
			close();
		} catch (SQLException e) {
			System.err.println("Could not get the products");
		}
		
		return list;
	}
	
	//----------------------------- SALE

	public void addCompra(Compra c) {
		String sql = "INSERT INTO compra (date, total) values (?, ?)";
		String sql2 = "SELECT id FROM compra WHERE compra.date = ? AND compra.total = ?";
		
		open();

		try {
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			stmt.setString(1, c.getTimestamp().toString());
			stmt.setDouble(2, c.getTotal());
			stmt.execute();
			
			PreparedStatement stmt2 = this.conn.prepareStatement(sql2);
			stmt2.setString(1, c.getTimestamp().toString());
			stmt2.setDouble(2, c.getTotal());
			ResultSet rs = stmt2.executeQuery();

			rs.next();
			
			long id = rs.getLong("id");
			
			c.setId(id);
			
			if (c.getItens() != null) {
				for (Item i : c.getItens()) {
					addItem(c, i);
				}
			}

		} catch (Exception e) {
			System.err.println("Could not add the new sale");
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	//----------------------------- ITEM

	public void addItem(Compra c, Item i) {
		String sql = "INSERT INTO item (compra_id, produto_id, quantity) values (?, ?, ?)";

		open();

		try {
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			stmt.setLong(1, c.getId());
			stmt.setLong(2, i.getProduto().getId());
			stmt.setInt(3, i.getQuantity());
			stmt.execute();

			updateQuantity(i.getProduto());
		} catch (Exception e) {
			System.err.println("Could not add the new item");
			e.printStackTrace();
		} finally {
			close();
		}
	}

}
