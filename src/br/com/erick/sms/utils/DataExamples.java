package br.com.erick.sms.utils;

import java.util.ArrayList;
import java.util.List;

import br.com.erick.sms.model.Product;

public class DataExamples {

	private DataExamples() {}
	
	private static List<Product> products = new ArrayList<>();
	
	static {
		products.add(new Product("Placa mãe", 500));
		products.add(new Product("Memória RAM 4GB", 250));
		products.add(new Product("RTX 4090", 5000));
		products.add(new Product("Mouse", 120));
		products.add(new Product("Teclado", 100));
		products.add(new Product("SSD 1TB", 450));
		products.add(new Product("Processador Intel i5", 700));
		products.add(new Product("Processador AMD Ryzen5", 500));
		products.add(new Product("Monitor", 300));
		products.add(new Product("Fone bluetooth", 100));
	}
	
	public static List<Product> productExamples(){
		return new ArrayList<Product>(products);
	}
}
