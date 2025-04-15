package br.com.erick.sms.service;

import java.sql.PreparedStatement;

import br.com.erick.sms.model.Item;
import br.com.erick.sms.model.Sale;
import br.com.erick.sms.utils.DBConnection;

public class ItemService {

	private DBConnection dbc;

	private ProductService ps;

	private SaleService ss;

	public ItemService(DBConnection dbc) {
		this.dbc = dbc;
	}

	public void setPS(ProductService ps) {
		this.ps = ps;
	}

	public void setSS(SaleService ss) {
		this.ss = ss;
	}

	public void addItem(Sale c, Item i) {
		String sql = "INSERT INTO item (sale_id, product_id, quantity) values (?, ?, ?)";

		try {
			PreparedStatement stmt = this.dbc.getConnection().prepareStatement(sql);
			stmt.setLong(1, c.getId());
			stmt.setLong(2, i.getProduto().getId());
			stmt.setInt(3, i.getQuantity());
			stmt.execute();

			ps.updateQuantity(i.getProduto());
		} catch (Exception e) {
			System.err.println("Could not add the new item");
			e.printStackTrace();
		}
	}
}
