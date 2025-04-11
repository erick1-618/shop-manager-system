package br.com.erick.sms.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.erick.sms.model.Compra;
import br.com.erick.sms.model.Item;
import br.com.erick.sms.model.Produto;
import br.com.erick.sms.utils.DBConnection;

public class SaleService {

	private DBConnection dbc;

	private ItemService is;

	private ProdutoService ps;

	public void setPS(ProdutoService ps) {
		this.ps = ps;
	}

	public void setIS(ItemService is) {
		this.is = is;
	}

	public SaleService(DBConnection dbc) {
		this.dbc = dbc;
	}

	public void addCompra(Compra c) {
		String sql = "INSERT INTO compra (date, total) values (?, ?)";
		String sql2 = "SELECT last_value FROM compra_id_seq";

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

	public List<Compra> getAllSales() {
		String sql = "SELECT * FROM compra;";

		List<Compra> list = new ArrayList<>();

		ResultSet sales = null;
		try {
			Statement stmt = this.dbc.getConnection().createStatement();
			sales = stmt.executeQuery(sql);
			while (sales.next()) {
				list.add(new Compra(sales.getDouble("total"), sales.getLong("id"), sales.getString("date")));
			}
		} catch (SQLException e) {
			System.err.println("Could not get the sales");
		}

		return list;
	}

	public Compra getSaleById(int id) {
		String sql1 = "SELECT * FROM item i JOIN produto p ON p.id = i.produto_id WHERE i.compra_id = ?";

		String sql2 = "SELECT * FROM compra WHERE id = ?";
		
		Compra c = null;
		
		List<Item> itens = new ArrayList<>();
		ResultSet q = null;
		try {
			PreparedStatement stmt = this.dbc.getConnection().prepareStatement(sql1);
			stmt.setInt(1, id);
			q = stmt.executeQuery();
			while(q.next()) {
				itens.add(new Item(new Produto(q.getString("name"), q.getDouble("unit_value"), q.getInt("sales_quantity"), q.getLong("id")),q.getInt("quantity")));
			}
			stmt = dbc.getConnection().prepareStatement(sql2);
			stmt.setInt(1, id);
			q = stmt.executeQuery();
			q.next();
			c = new Compra(itens, id, q.getString("date"));
		}catch(SQLException e) {
			System.err.println("Could not get the sale");
		}
		return c;
	}
}
