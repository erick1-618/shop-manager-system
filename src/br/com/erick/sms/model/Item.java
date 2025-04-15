package br.com.erick.sms.model;

public class Item {
	private Product product;
	private int quantity;

	public Item(Product produto, int quantity) {
		this.product = produto;
		this.quantity = quantity;
	}

	public Product getProduto() {
		return this.product;
	}

	public int getQuantity() {
		return this.quantity;
	}
}
