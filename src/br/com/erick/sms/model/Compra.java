package br.com.erick.sms.model;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import java.time.LocalDateTime;

public class Compra {

	private List<Item> itens;

	private double total = 0;

	private long id;

	private String timestamp;

	public Compra(List<Item> itens) {
		double prodSum = 0;
		this.itens = itens;
		if (this.itens == null)
			this.total = 0;
		else {
			for (Item i : itens) {
				prodSum = i.getProduto().getUnitValue() * i.getQuantity();
				this.total += prodSum;
			}
		}
		this.timestamp = LocalDateTime.now().toString();
	}

	public Compra(List<Item> itens, long id, String date) {
		this.id = id;
		double prodSum = 0;
		this.itens = itens;
		if (this.itens == null)
			this.total = 0;
		else {
			for (Item i : itens) {
				prodSum = i.getProduto().getUnitValue() * i.getQuantity();
				this.total += prodSum;
			}
		}
		this.timestamp = date;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getTotal() {
		return this.total;
	}

	public List<Item> getItens() {
		return this.itens;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	public long getId() {
		return this.id;
	}
}
