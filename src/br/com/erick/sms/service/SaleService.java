package br.com.erick.sms.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.erick.sms.model.Item;
import br.com.erick.sms.model.Product;
import br.com.erick.sms.model.Sale;
import br.com.erick.sms.utils.DBConnection;

public class SaleService {

	private DBConnection dbc;

	private ItemService is;

	private ProductService ps;

	public void setPS(ProductService ps) {
		this.ps = ps;
	}

	public void setIS(ItemService is) {
		this.is = is;
	}

	public SaleService(DBConnection dbc) {
		this.dbc = dbc;
	}

	public void addSale(Sale c) {
		String sql = "INSERT INTO sale (date, total) values (?, ?)";
		String sql2 = "SELECT last_value FROM sale_id_seq";

		try {
			PreparedStatement stmt = this.dbc.getConnection().prepareStatement(sql);
			stmt.setString(1, c.getTimestamp().toString());
			stmt.setDouble(2, c.getTotal());
			stmt.execute();

			Statement stmt2 = this.dbc.getConnection().createStatement();
			ResultSet rs = stmt2.executeQuery(sql2);

			rs.next();

			long id = rs.getLong("last_value");

			c.setId(id);

			if (c.getItens() != null) {
				for (Item i : c.getItens()) {
					is.addItem(c, i);
				}
			}

		} catch (Exception e) {
			System.err.println("Could not add the new sale");
			e.printStackTrace();
		}
	}

	public List<Sale> getAllSales() {
		String sql = "SELECT * FROM sale ORDER BY id DESC;";

		List<Sale> list = new ArrayList<>();

		ResultSet sales = null;
		try {
			Statement stmt = this.dbc.getConnection().createStatement();
			sales = stmt.executeQuery(sql);
			while (sales.next()) {
				list.add(new Sale(sales.getDouble("total"), sales.getLong("id"), sales.getString("date")));
			}
		} catch (SQLException e) {
			System.err.println("Could not get the sales");
		}

		return list;
	}

	public Sale getSaleById(int id) {
		String sql1 = "SELECT * FROM item i JOIN product p ON p.id = i.product_id WHERE i.sale_id = ?";

		String sql2 = "SELECT * FROM sale WHERE id = ?";

		Sale c = null;

		List<Item> itens = new ArrayList<>();
		ResultSet q = null;
		try {
			PreparedStatement stmt = this.dbc.getConnection().prepareStatement(sql1);
			stmt.setInt(1, id);
			q = stmt.executeQuery();
			while (q.next()) {
				itens.add(new Item(
						new Product(q.getString("name"), q.getDouble("unit_value"), q.getInt("in_stock"),
								q.getInt("sales_quantity"), q.getLong("id"), q.getBoolean("active")),
						q.getInt("quantity")));
			}
			stmt = dbc.getConnection().prepareStatement(sql2);
			stmt.setInt(1, id);
			q = stmt.executeQuery();
			if (!q.next())
				return null;
			c = new Sale(itens, id, q.getString("date"));
		} catch (SQLException e) {
			System.err.println("Could not get the sale");
		}
		return c;
	}
}
