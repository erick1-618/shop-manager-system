package br.com.erick.sms.model;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import java.time.LocalDateTime;

public class Compra {

    private List<Item> itens;

    private double total = 0;

    private String id;

    private String timestamp;

    public Compra(List<Item> itens){
        this.id = UUID.randomUUID().toString();
        double prodSum = 0;
        this.itens = itens;
        if(this.itens == null) this.total = 0;
        else{
            for(Item i : itens){
                prodSum = i.getProduto().getUnitValue() * i.getQuantity();
                this.total += prodSum;
            }
        } 
        this.timestamp = LocalDateTime.now().toString();
    }

    public Compra(List<Item> itens, String id, String date){
        this.id = id;
        double prodSum = 0;
        this.itens = itens;
        if(this.itens == null) this.total = 0;
        else{
            for(Item i : itens){
                prodSum = i.getProduto().getUnitValue() * i.getQuantity();
                this.total += prodSum;
            }
        } 
        this.timestamp = date;
    }

    public double getTotal(){
        return this.total;    
    }

    public List<Item> getItens(){
        return this.itens;
    }

    public String getTimestamp(){
        return this.timestamp;
    }

    public String getId(){
        return this.id.toString();
    }
}
