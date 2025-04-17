package br.com.erick.sms.model;

public class Product {

	private String name;

	private double unitValue;

	private int salesQuantity;

	private long id;

	private int inStock;

	private boolean isActive;

	public Product(String name, double unitValue, int inStock, int salesQuantity, long id, boolean isActive) {
		this.id = id;
		this.name = name;
		this.unitValue = unitValue;
		this.salesQuantity = salesQuantity;
		this.inStock = inStock;
		this.isActive = isActive;
	}

	public Product(String name, double unitValue, int inStock) {
		this.name = name;
		this.unitValue = unitValue;
		this.inStock = inStock;
	}

	public String getName() {
		return this.name;
	}

	public double getUnitValue() {
		return this.unitValue;
	}

	public int getSalesQuantity() {
		return this.salesQuantity;
	}

	public void setSalesQuantity(int qt) {
		this.salesQuantity = qt;
	}

	public long getId() {
		return this.id;
	}
	
	public int getInStock() {
		return inStock;
	}

	public boolean isActive() {
		return isActive;
	}
}
