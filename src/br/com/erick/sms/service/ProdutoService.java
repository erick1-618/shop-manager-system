package br.com.erick.sms.service;

import br.com.erick.sms.model.Produto;
import br.com.erick.sms.utils.DBConnection;
import br.com.erick.sms.model.Compra;
import br.com.erick.sms.model.Item;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class ProdutoService {

	private DBConnection dbc;
	private SaleService ss;
	private ItemService is;

	public ProdutoService(DBConnection dbc) {
		this.dbc = dbc;
	}

	public void addNewProduct(Produto p) {
		String sql = "INSERT INTO produto (name, unit_value) values (?, ?);";

		try {
			PreparedStatement stmt = this.dbc.getConnection().prepareStatement(sql);
			stmt.setDouble(2, p.getUnitValue());
			stmt.setString(1, p.getName());
			stmt.execute();
		} catch (SQLException e) {
			System.err.println("Could not add the new product");
			e.printStackTrace();
		}
	}

	public Produto selectProdutoById(long id) {
		String sql = "SELECT * FROM produto WHERE produto.id = ?";

		try {
			PreparedStatement stmt = this.dbc.getConnection().prepareStatement(sql);
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();

			rs.next();

			String name = rs.getString("name");
			double unitValue = rs.getDouble("unit_value");
			int salesQt = rs.getInt("sales_quantity");

			return new Produto(name, unitValue, salesQt, id);

		} catch (SQLException e) {
			System.err.println("Could select the product");
			e.printStackTrace();
			return null;
		}
	}

	public void updateQuantity(Produto p) {
		String sql1 = "SELECT SUM(item.quantity) AS soma FROM item WHERE item.produto_id = ?;";
		String sql2 = "UPDATE produto SET sales_quantity = ? WHERE produto.id = ?";

		try {
			PreparedStatement stmt = this.dbc.getConnection().prepareStatement(sql1);
			stmt.setLong(1, p.getId());
			ResultSet sum = stmt.executeQuery();

			sum.next();

			PreparedStatement stmt2 = this.dbc.getConnection().prepareStatement(sql2);
			stmt2.setInt(1, sum.getInt("soma"));
			stmt2.setLong(2, p.getId());
			stmt2.execute();
		} catch (Exception e) {
			System.err.println("Could not update the item qt");
			e.printStackTrace();
		}
	}

	public List<Produto> getProducts() {
		String sql = "SELECT * FROM produto;";

		List<Produto> list = new ArrayList<>();

		ResultSet prods = null;
		try {
			Statement stmt = this.dbc.getConnection().createStatement();
			prods = stmt.executeQuery(sql);
			while (prods.next()) {
				list.add(new Produto(prods.getString("name"), prods.getDouble("unit_value"),
						prods.getInt("sales_quantity"), prods.getLong("id")));
			}
		} catch (SQLException e) {
			System.err.println("Could not get the products");
		}

		return list;
	}

	public void setSS(SaleService ss) {
		this.ss = ss;
	}
	
	public void setIS(ItemService is) {
		this.is = is;
	}
}
