package br.com.erick.sms.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.erick.sms.model.Product;
import br.com.erick.sms.utils.DBConnection;

public class ProductService {

	private DBConnection dbc;
	private SaleService ss;
	private ItemService is;

	public ProductService(DBConnection dbc) {
		this.dbc = dbc;
	}

	public void addNewProduct(Product p) {
		String sql = "INSERT INTO product (name, unit_value, in_stock) values (?, ?, ?);";

		try {
			PreparedStatement stmt = this.dbc.getConnection().prepareStatement(sql);
			stmt.setDouble(2, p.getUnitValue());
			stmt.setString(1, p.getName());
			stmt.setInt(3, p.getInStock());
			stmt.execute();
		} catch (SQLException e) {
			System.err.println("Could not add the new product");
			e.printStackTrace();
		}
	}

	public Product selectProdutoById(long id) {
		String sql = "SELECT * FROM product WHERE product.id = ?";

		try {
			PreparedStatement stmt = this.dbc.getConnection().prepareStatement(sql);
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();

			rs.next();

			String name = rs.getString("name");
			double unitValue = rs.getDouble("unit_value");
			int salesQt = rs.getInt("sales_quantity");
			int stock = rs.getInt("in_stock");
			boolean active = rs.getBoolean("active");

			return new Product(name, unitValue, stock, salesQt, id, active);

		} catch (SQLException e) {
			return null;
		}
	}

	public void updateQuantity(Product p) {
		String sql1 = "SELECT SUM(item.quantity) AS soma FROM item WHERE item.product_id = ?;";
		String sql2 = "UPDATE product SET sales_quantity = ? WHERE product.id = ?";

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

	public List<Product> getProducts() {
		String sql = "SELECT * FROM product;";

		List<Product> list = new ArrayList<>();

		ResultSet prods = null;
		try {

			Statement stmt = this.dbc.getConnection().createStatement();

			prods = stmt.executeQuery(sql);

			while (prods.next()) {
				if(prods.getBoolean("active") == false) continue;
				list.add(new Product(prods.getString("name"), prods.getDouble("unit_value"), prods.getInt("in_stock"),
						prods.getInt("sales_quantity"), prods.getLong("id"), prods.getBoolean("active")));
			}

		} catch (SQLException e) {
			System.err.println("Could not get the products");
		}

		return list;
	}

	public void subtractStock(int qt, long id) {

		String sql = "UPDATE product SET in_stock = in_stock - ? WHERE id = ?";

		try {
			PreparedStatement stmt = this.dbc.getConnection().prepareStatement(sql);

			stmt.setInt(1, qt);
			stmt.setLong(2, id);

			stmt.execute();

		} catch (SQLException e) {
			System.err.println("Could not update the quantity");
		}
	}

	public void addStock(int qt, long id) {

		String sql = "UPDATE product SET in_stock = in_stock + ? WHERE id = ?";

		try {
			PreparedStatement stmt = this.dbc.getConnection().prepareStatement(sql);

			stmt.setInt(1, qt);
			stmt.setLong(2, id);

			stmt.execute();

		} catch (SQLException e) {
			System.err.println("Could not update the quantity");
		}
	}

	public void setSS(SaleService ss) {
		this.ss = ss;
	}

	public void setIS(ItemService is) {
		this.is = is;
	}

	public void deleteProduct(long prodID) {
		String sql = "UPDATE product SET active = false where id = ?";

		try {
			PreparedStatement stmt = this.dbc.getConnection().prepareStatement(sql);
			stmt.setLong(1, prodID);

			stmt.execute();

		} catch (SQLException e) {
			System.err.println("Could not delete the product");
		}
	}
}
