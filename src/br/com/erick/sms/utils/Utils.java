package br.com.erick.sms.utils;

import java.util.ArrayList;
import java.util.List;

import br.com.erick.sms.model.Item;
import br.com.erick.sms.model.Product;

public class Utils {
	
	private Utils() {}
	
	private static List<Product> products = new ArrayList<>();
	
	static {
		products.add(new Product("Placa mãe", 500, 10));
		products.add(new Product("Memória RAM 4GB", 250, 10));
		products.add(new Product("RTX 4090", 5000, 10));
		products.add(new Product("Mouse", 120, 10));
		products.add(new Product("Teclado", 100, 10));
		products.add(new Product("SSD 1TB", 450, 10));
		products.add(new Product("Processador Intel i5", 700, 10));
		products.add(new Product("Processador AMD Ryzen5", 500, 10));
		products.add(new Product("Monitor", 300, 10));
		products.add(new Product("Fone bluetooth", 100, 10));
	}

	public static List<Item> compressCart(List<Item> cart) {
		List<Item> compressed = new ArrayList<>();
		cart.forEach(i -> {
			if (compressed.contains(i)) {
				Item it = compressed.get(compressed.indexOf(i));
				it.setQuantity(it.getQuantity() + i.getQuantity());
			} else {
				compressed.add(i);
			}
		});
		return compressed;
	}

	public static List<Product> productExamples() {
		return new ArrayList<Product>(products);
	}
}
