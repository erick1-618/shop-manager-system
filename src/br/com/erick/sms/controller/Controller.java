package br.com.erick.sms.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import br.com.erick.sms.model.Compra;
import br.com.erick.sms.model.Item;
import br.com.erick.sms.model.Produto;
import br.com.erick.sms.service.ConnectionService;

public class Controller {

	private static Controller instance;

	private ConnectionService cc;

	private List<Item> cart;

	public static Controller getInstance() {
		if (instance == null) {
			return new Controller();
		}

		return instance;
	}

	public int getCartQt() {
		return cart.size();
	}

	private Controller() {

		Properties props = new Properties();

		try (FileInputStream fis = new FileInputStream("resources/config.properties")) {
			props.load(fis);
			System.out.println("[ INFO ] Enviroment files UP");
		} catch (IOException e) {
			System.err.println("[ ERR ] Could not load enviroment files");
		}

		String url = props.getProperty("db_url") + props.getProperty("db");

		this.cc = new ConnectionService(props.getProperty("user"), props.getProperty("pwk"), url);

		if (!cc.testConnection()) {
			System.err.println("[ ERR ] Coult not connect to database");
			return;
		}

		System.out.println("[ INFO ] First connection UP");

		cc.initializeDatabase();

		this.cart = new ArrayList<>();

		System.out.println("[ INFO ] Cart initialized");
	}

	public void restart() {
		this.cc.restartDatabase();
	}

	public void addNewProduct(String name, String value) {

		try {
			double v = Double.parseDouble(value.replace(',', '.'));

			if (name == null)
				throw new RuntimeException("Name is null");

			Produto p = new Produto(name, v);
			this.cc.addNewProduct(p);
		} catch (RuntimeException e) {
			System.err.println("Error -> Name must be not null and alphanumeric, and product value must be decimal");
		}
	}

	public void addCompra(Compra c) {
		this.cc.addCompra(c);
	}

	public void addItem(Compra c, Item i) {
		this.cc.addItem(c, i);
	}

	public void updateQuantity(Produto p) {
		this.cc.updateQuantity(p);
	}

	public List<String> getAllProducts() {
		List<String> products = new ArrayList<>();

		List<Produto> prodQ = this.cc.getProducts();

		String line;

		if (prodQ == null)
			return products;

		for (Produto p : prodQ) {
			line = String.format("%-5s | %-50s | R$ %12.2f", p.getId(), p.getName(), p.getUnitValue());
			products.add(line);
		}

		return products;
	}

	public void addToCart(String id, String qt) {

		try {

			long prodID = Long.parseLong(id);

			Produto p = cc.selectProdutoById(prodID);

			if (p == null)
				throw new NullPointerException();

			int qtprod = Integer.parseInt(qt);

			this.cart.add(new Item(p, qtprod));

		} catch (NumberFormatException | NullPointerException e) {
			if (e instanceof NullPointerException)
				System.err.println("Product with id: " + id + " doesn't exists");
			else if (e instanceof NullPointerException)
				System.err.println("Invalid quantity values");
		}
	}

	public void closeTheCart() {
		if (cart.isEmpty()) {
			System.err.println("The cart is empty");
			return;
		}
		
		Compra c = new Compra(cart);
		
		cc.addCompra(c);
		
		cart.clear();
	}

	// General tests;
	public static void main(String[] args) {

		Controller c = Controller.getInstance();

		c.restart();
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
