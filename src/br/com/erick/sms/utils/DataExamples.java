package br.com.erick.sms.utils;

import java.util.ArrayList;
import java.util.List;

import br.com.erick.sms.model.Produto;

public class DataExamples {

	private DataExamples() {}
	
	private static List<Produto> products = new ArrayList<>();
	
	static {
		products.add(new Produto("Placa mãe", 500));
		products.add(new Produto("Memória RAM 4GB", 250));
		products.add(new Produto("RTX 4090", 5000));
		products.add(new Produto("Mouse", 120));
		products.add(new Produto("Teclado", 100));
		products.add(new Produto("SSD 1TB", 450));
		products.add(new Produto("Processador Intel i5", 700));
		products.add(new Produto("Processador AMD Ryzen5", 500));
		products.add(new Produto("Monitor", 300));
		products.add(new Produto("Fone bluetooth", 100));
	}
	
	public static List<Produto> productExamples(){
		return new ArrayList<Produto>(products);
	}
}
