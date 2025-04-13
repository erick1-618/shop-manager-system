package br.com.erick.sms.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import br.com.erick.sms.model.Compra;
import br.com.erick.sms.model.Item;
import br.com.erick.sms.model.Produto;
import br.com.erick.sms.service.ItemService;
import br.com.erick.sms.service.ProdutoService;
import br.com.erick.sms.service.SaleService;
import br.com.erick.sms.utils.DBConnection;
import br.com.erick.sms.utils.DataExamples;

public class Controller {

	private static Controller instance;

	private ProdutoService ps;

	private ItemService is;

	private SaleService ss;

	private List<Item> cart;

	private DBConnection dbc;

	private Controller() {

		Properties props = new Properties();

		try (FileInputStream fis = new FileInputStream("resources/config.properties")) {
			props.load(fis);
			System.out.println("[ INFO ] Enviroment files UP");
		} catch (IOException e) {
			System.err.println("[ ERR ] Could not load enviroment files");
		}

		String url = props.getProperty("db_url") + props.getProperty("db");

		this.dbc = new DBConnection(props.getProperty("user"), props.getProperty("pwk"), url);

		this.ps = new ProdutoService(dbc);

		this.is = new ItemService(dbc);

		this.ss = new SaleService(dbc);

		interConnectServices();

		System.out.println("[ INFO ] Services initialized");

		this.cart = new ArrayList<>();

		System.out.println("[ INFO ] Cart initialized");
	}

	public static Controller getInstance() {
		if (instance == null) {
			return new Controller();
		}

		return instance;
	}

	public int getCartQt() {
		return cart.size();
	}

	public List<String> getCart() {
		List<String> cartStr = new ArrayList<>();
		for (int i = 0; i < cart.size(); i++) {
			cartStr.add(String.format("%-5d | %-50s | R$ %12.2f | %-5d", i, cart.get(i).getProduto().getName(),
					cart.get(i).getProduto().getUnitValue(), cart.get(i).getQuantity()));
		}
		return cartStr;
	}

	public double getCartTotal() {
		double sum = 0;
		for (Item i : cart) {
			sum += i.getQuantity() * i.getProduto().getUnitValue();
		}
		return sum;
	}

	public void restartDB() {
		this.dbc.resetDatabase();
	}

	public void closeConn() {
		this.dbc.close();
		System.out.println("[ INFO ] Connection with DB closed");
	}

	public void addToCart(String id, String qt) {

		try {

			long prodID = Long.parseLong(id);

			Produto p = ps.selectProdutoById(prodID);

			if (p == null)
				throw new NullPointerException();

			int qtprod = Integer.parseInt(qt);

			this.cart.add(new Item(p, qtprod));

		} catch (NumberFormatException | NullPointerException e) {
			if (e instanceof NullPointerException)
				System.err.println("Product with id: " + id + " doesn't exists");
			else if (e instanceof NumberFormatException)
				System.err.println("Invalid quantity values");
		}
	}

	public void removeFromCart(String id) {

		try {

			int prodID = Integer.parseInt(id);

			this.cart.remove(prodID);

		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			System.err.println("Enter a valid ID");
		}
	}

	public void closeCart() {
		if (cart.isEmpty()) {
			System.err.println("The cart is empty");
			return;
		}

		Compra c = new Compra(cart);

		ss.addCompra(c);

		cart.clear();
	}

	public void clearCart() {
		// TODO Auto-generated method stub

	}

// ================== PRODUCTS

	public void addNewProduct(String name, String value) {

		try {
			double v = Double.parseDouble(value.replace(',', '.'));

			if (name == null)
				throw new RuntimeException("Name is null");

			Produto p = new Produto(name, v);
			this.ps.addNewProduct(p);
		} catch (RuntimeException e) {
			System.err.println("Error -> Name must be not null and alphanumeric, and product value must be decimal");
		}
	}

	public List<String> getAllProducts() {
		List<String> products = new ArrayList<>();

		List<Produto> prodQ = this.ps.getProducts();

		String line;

		if (prodQ == null)
			return products;

		for (Produto p : prodQ) {
			line = String.format("%-5s | %-50s | R$ %12.2f", p.getId(), p.getName(), p.getUnitValue());
			products.add(line);
		}

		return products;
	}

	public void updateQuantity(Produto p) {
		this.ps.updateQuantity(p);
	}

//================================ SALES

	public void addCompra(Compra c) {
		this.ss.addCompra(c);
	}

	public List<String> getAllSales() {
		List<String> sales = new ArrayList<>();

		List<Compra> salesQ = this.ss.getAllSales();

		String line;

		if (salesQ == null)
			return sales;

		for (Compra c : salesQ) {
			line = String.format("%-5d | %-50s | R$ %12.2f", c.getId(), c.getTimestamp(), c.getTotal());
			sales.add(line);
		}

		return sales;
	}

	public String getSaleDet(String id) {
		Compra c = ss.getSaleById(Integer.parseInt(id));
		String date = c.getTimestamp().substring(0, 10).replace('-', '/');
		String sale = "";
		sale += "Sale " + c.getId() + " realized in: " + date + "\n-------------------- ITENS -----------------------";
		for (Item i : c.getItens()) {
			sale += "\n" + String.format("%-40s | R$ %-20.2f | %-10d", i.getProduto().getName(),
					i.getProduto().getUnitValue() * i.getQuantity(), i.getQuantity());
		}
		sale += "\n=====TOTAL: R$ " + String.format("%.2f", c.getTotal());
		return sale;
	}

//================================ ITEM

	public void addItem(Compra c, Item i) {
		this.is.addItem(c, i);
		updateQuantity(i.getProduto());
	}

	private void interConnectServices() {
		this.is.setPS(ps);
		this.is.setSS(ss);
		this.ps.setIS(is);
		this.ps.setSS(ss);
		this.ss.setIS(is);
		this.ss.setPS(ps);
	}

	// General tests;
	public static void main(String[] args) {

		Controller c = Controller.getInstance();

		c.restartDB();

		List<Produto> prods = DataExamples.productExamples();

		prods.forEach(p -> c.addNewProduct(p.getName(), "" + p.getUnitValue()));
		System.out.println("Example products added");

//
//		Produto p = new Produto("ABC", 2.0);
//
//		Item i = new Item(p, 12);
//
//		ArrayList<Item> list = new ArrayList<>();
//
//		list.add(i);
//
//		Compra comp = new Compra(list);
//
//		c.addNewProduct("Produto A", 15);
//
//		c.addCompra(comp);
	}
}
