package br.com.erick.sms.model;

import java.util.Objects;

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

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getQuantity() {
		return this.quantity;
	}

	@Override
	public int hashCode() {
		return Objects.hash(product);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		return Objects.equals(product.getId(), other.product.getId());
	}
}
