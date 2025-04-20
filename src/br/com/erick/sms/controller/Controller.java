package br.com.erick.sms.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import br.com.erick.sms.model.Sale;
import br.com.erick.sms.model.Item;
import br.com.erick.sms.model.Product;
import br.com.erick.sms.service.ItemService;
import br.com.erick.sms.service.ProductService;
import br.com.erick.sms.service.SaleService;
import br.com.erick.sms.utils.CartUtils;
import br.com.erick.sms.utils.DBConnection;
import br.com.erick.sms.utils.DataExamples;

public class Controller {

	private static Controller instance;

	private ProductService ps;

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

		this.ps = new ProductService(dbc);

		this.is = new ItemService(dbc);

		this.ss = new SaleService(dbc);

		interConnectServices();

		System.out.println("[ INFO ] Services initialized");

		this.cart = new ArrayList<>();
	}

	public static Controller getInstance() {
		if (instance == null) {
			return new Controller();
		}
		return instance;
	}

	public void restartDB() {
		this.dbc.resetDatabase();
	}

	public void closeConn() {
		this.dbc.close();
		System.out.println("[ INFO ] Connection with DB closed");
	}

//---------------------------- CART

	public int getCartQt() {
		return cart.size();
	}

	public List<String> getCart() {
		List<String> cartStr = new ArrayList<>();

		if (cart.isEmpty()) {
			cartStr.add("The cart is empty.");
		}
		
		List<Item> compressed = CartUtils.compressCart(cart);
		cart = compressed;

		for (int i = 0; i < compressed.size(); i++) {
			cartStr.add(String.format("%12d | %-50s | R$ %-20.2f | %-7d", i, compressed.get(i).getProduto().getName(),
					compressed.get(i).getProduto().getUnitValue() * compressed.get(i).getQuantity(), compressed.get(i).getQuantity()));
		}
		cartStr.add("===== TOTAL: R$ " + getCartTotal());

		return cartStr;
	}

	public double getCartTotal() {
		double sum = 0;
		for (Item i : cart) {
			sum += i.getQuantity() * i.getProduto().getUnitValue();
		}
		return sum;
	}

	public void addToCart(String id, String qt) {

		try {

			long prodID = Long.parseLong(id);

			Product p = ps.selectProdutoById(prodID);

			int qtprod = Integer.parseInt(qt);

			if (p == null)
				throw new NullPointerException();
			
			if(!p.isActive())
				throw new NullPointerException();

			if (qtprod <= 0)
				throw new NumberFormatException();

			if (p.getInStock() < qtprod) {
				throw new InvalidParameterException();
			}

			this.ps.subtractStock(qtprod, prodID);

			this.cart.add(new Item(p, qtprod));

		} catch (Exception e) {
			if(e instanceof InvalidParameterException)
				throw new InvalidParameterException();
			throw new RuntimeException();
		}
	}

	public void removeFromCart(String id) {

		try {

			int prodID = Integer.parseInt(id);

			this.ps.addStock(cart.get(prodID).getQuantity(), cart.get(prodID).getProduto().getId());

			this.cart.remove(prodID);

		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			throw new RuntimeException();
		}
	}

	public void closeCart() {

		if (cart.isEmpty())
			throw new NullPointerException();

		List<Item> compressed = CartUtils.compressCart(cart);
 		
		Sale c = new Sale(compressed);

		ss.addSale(c);

		cart.clear();
	}

	public void clearCart() {
		cart = CartUtils.compressCart(cart);
		for(int i = 0; i < cart.size(); i++) {
			this.ps.addStock(cart.get(i).getQuantity(), cart.get(i).getProduto().getId());
		}
		cart.clear();
	}

// ================== PRODUCTS

	public void addNewProduct(String name, String value, String qt) {

		try {

			double v = Double.parseDouble(value.replace(',', '.'));

			int stock = Integer.parseInt(qt);

			if (!name.matches("^[A-Za-zÀ-ÿ][A-Za-zÀ-ÿ0-9 ]*$") || v <= 0 || name.length() > 50 || stock < 0)
				throw new RuntimeException();

			Product p = new Product(name, v, stock);
			this.ps.addNewProduct(p);

		} catch (RuntimeException e) {
			System.err.println(
					"Error -> Name must be not null and alphanumeric, " + "\nstarting with a letter, and product value"
							+ "\nmust be decimal positive. Product max lenght is 100");
		}
	}

	public List<String> getAllProducts() {
		List<String> products = new ArrayList<>();

		List<Product> prodQ = this.ps.getProducts();

		String line;

		if (prodQ == null)
			return products;

		for (Product p : prodQ) {
			line = String.format("%-10d | %-50s | R$ %12.2f | %12d | %12d", p.getId(), p.getName(), p.getUnitValue(),
					p.getSalesQuantity(), p.getInStock());
			products.add(line);
		}

		if (products.isEmpty())
			products.add("There's no product registered");

		return products;
	}
	
	public void addStock(String id, String qt) {
		try {

			long prodID = Long.parseLong(id);

			Product p = ps.selectProdutoById(prodID);

			int qtprod = Integer.parseInt(qt);

			if (p == null)
				throw new NullPointerException();
			
			if(!p.isActive())
				throw new NullPointerException();

			if (qtprod <= 0)
				throw new NumberFormatException();

			this.ps.addStock(qtprod, prodID);

		} catch (NumberFormatException | NullPointerException e) {
			throw new RuntimeException();
		}
	}
	
	public void removeProduct(String id) {
		try {

			long prodID = Long.parseLong(id);

			Product p = ps.selectProdutoById(prodID);
			
			if(cart.stream().anyMatch(prod -> prod.getProduto().getId() == p.getId()))
				throw new InvalidParameterException();
			
			if (p == null)
				throw new NullPointerException();
			
			if(!p.isActive())
				throw new NullPointerException();
			
			this.ps.deleteProduct(prodID);

		} catch (NumberFormatException | NullPointerException | InvalidParameterException e) {
			if(e instanceof InvalidParameterException)
				throw new InvalidParameterException();
			throw new RuntimeException();
		}
	}

	public void addExamples() {
		List<Product> prods = DataExamples.productExamples();
		prods.forEach(p -> addNewProduct(p.getName(), "" + p.getUnitValue(), "" + p.getInStock()));
		System.out.println("[ INFO ] Examples added");
	}
	
//================================ SALES

	public List<String> getAllSales() {
		List<String> sales = new ArrayList<>();

		List<Sale> salesQ = this.ss.getAllSales();

		String line;

		if (salesQ == null)
			return sales;

		for (Sale c : salesQ) {
			line = String.format("%-5d | %-25s | R$ %-20.2f", c.getId(),
					c.getTimestamp().substring(0, 10).replace('-', '/'), c.getTotal());
			sales.add(line);
		}

		if (sales.isEmpty())
			sales.add("There's no sale registered");

		return sales;
	}

	public List<String> getSaleDet(String id) {
		List<String> sale = new ArrayList<>();
		try {
			Sale c = ss.getSaleById(Integer.parseInt(id));
			if (c == null)
				throw new RuntimeException();
			String date = c.getTimestamp().substring(0, 10).replace('-', '/');
			for (Item i : c.getItens()) {
				sale.add(String.format("%-40s | R$ %-20.2f | %-10d", i.getProduto().getName(),
						i.getProduto().getUnitValue() * i.getQuantity(), i.getQuantity()));
			}
		} catch (RuntimeException e) {
			System.err.println("Enter a valid id");
		}
		return sale;
	}

	private void interConnectServices() {
		this.is.setPS(ps);
		this.is.setSS(ss);
		this.ps.setIS(is);
		this.ps.setSS(ss);
		this.ss.setIS(is);
		this.ss.setPS(ps);
	}
}
