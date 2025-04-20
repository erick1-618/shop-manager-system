package br.com.erick.sms.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import br.com.erick.sms.model.Item;

public class CartUtils {
	
	public static List<Item> compressCart(List<Item> cart){
			List<Item> compressed = new ArrayList<>();
			cart.forEach(i -> {
				if(compressed.contains(i)) {
					Item it = compressed.get(compressed.indexOf(i));
					it.setQuantity(it.getQuantity() + i.getQuantity());
				}else {
					compressed.add(i);
				}
			});
			return compressed;
	}
}
