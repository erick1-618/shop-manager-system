package br.com.erick.sms.model;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Compra {

    private List<Item> itens;

    private double total = 0;

    private LocalDateTime timestamp;

    public Compra(List<Item> itens){
        double prodSum = 0;
        this.itens = itens;
        for(Item i : itens){
            prodSum = i.getProduto().getUnitValue() * i.getQuantity();
            this.total += prodSum;
        }
        this.timestamp = LocalDateTime.now();
    }

    public double getTotal(){
        return this.total;    
    }

    public LocalDateTime getTimestamp(){
        return this.timestamp;
    }
}
